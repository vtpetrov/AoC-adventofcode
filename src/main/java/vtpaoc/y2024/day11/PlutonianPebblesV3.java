package vtpaoc.y2024.day11;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static vtpaoc.helper.InputLoader.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;
import static vtpaoc.helper.NumberUtilsVtp.findNumberLength;

/**
 * define Map and accumulate numbers count there
 */
@Slf4j
public class PlutonianPebblesV3 extends BaseDay {

    public static final int BLINK_COUNT_P1 = 25;
    public static final int BLINK_COUNT = 75;
    public static final int ELSE_MULTIPLY_BY = 2024;
    static String year = "2024";
    static String day = "11";
    static String puzzleTitle = "Plutonian Pebbles";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();
        solvePartOneAndTwo();
        logStartP2();
        closeInput();
        logEndP2();
    }

    static Map<Long, Long> stoneCounts = new TreeMap<>();

    /**
     * boxStats.merge(String.valueOf(box.charAt(i)), 1, Integer::sum);
     */
    private static void solvePartOneAndTwo() {
        // load initial stone counts:
        Arrays.stream(inputLines.getFirst().split(" ")).map(Long::parseLong)
                .forEach(stone -> stoneCounts.put(stone, 1L));

        for (int i = 1; i <= BLINK_COUNT; i++) {
            transformStonesP1();
            long currentCount = totalStones();
            log.info("After {} blink(s): {}   ({})", i, currentCount, prettyPrintNumber(currentCount, '\''));
            log.trace("{}", stoneCounts);

            if (i == BLINK_COUNT_P1) {
                solutionP1 = currentCount;
                log.info("""
                        Part 1 solution:
                         Consider the arrangement of stones in front of you.
                         How many stones will you have after blinking 25 times?
                         = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
            }
        }

        solutionP2 = totalStones();

        log.info("""
                Part 2 solution:
                 How many stones would you have after blinking a total of 75 times?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

        log.debug("Integer.MAX_VALUE= {}, ({})", Integer.MAX_VALUE, prettyPrintNumber(Integer.MAX_VALUE, '\''));
        log.debug("   Long.MAX_VALUE= {}, ({})", Long.MAX_VALUE, prettyPrintNumber(Long.MAX_VALUE, '\''));
    }

    private static long totalStones() {
        // sum all the values from the stoneCounts map:
        return stoneCounts.values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Spawn new stones from each entry, put them in a new map, then merge the entries
     */
    private static void transformStonesP1() {

        Map<Long, Long> childStonesToAdd = new TreeMap<>();
        Map<Long, Long> orphanedStonesToRemove = new TreeMap<>();

        for (Map.Entry<Long, Long> stoneEntry : stoneCounts.entrySet()) {
            long stone = stoneEntry.getKey();
            long stoneCount = stoneEntry.getValue();

            // prepare the "old" stones for removal:
            // orphanedStonesToRemove.put(stone, stoneCount);
            orphanedStonesToRemove.merge(stone, stoneCount, Long::sum);

            //RULES:
            if (stone == 0) { // 0 -> 1
                childStonesToAdd.merge(1L, stoneCount, Long::sum);
            } else {
                int stoneLen = findNumberLength(stone);
                if (stoneLen % 2 == 0) { // even digits -> split into 2
                    // left stone:
                    String stoneAsStr = valueOf(stone);
                    long leftStone = parseLong(stoneAsStr.substring(0, stoneAsStr.length() / 2));
                    childStonesToAdd.merge(leftStone, stoneCount, Long::sum);
                    // right stone:
                    long rightStone = parseLong(stoneAsStr.substring(stoneLen / 2));
                    childStonesToAdd.merge(rightStone, stoneCount, Long::sum);
                } else {
                    // else -> multiply by 2024
                    childStonesToAdd.merge(stone * ELSE_MULTIPLY_BY, stoneCount, Long::sum);
                }
            }
        }

        // remove the orphaned stones from the map:
        orphanedStonesToRemove.forEach(stoneCounts::remove);
        // add the new child stones to the map:
        stoneCounts.putAll(childStonesToAdd);
    }
}
