package vtpaoc.y2024.day11;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static vtpaoc.helper.InputLoader.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class PlutonianPebbles extends BaseDay {

        public static final int BLINK_COUNT_P1 = 25;
        public static final int BLINK_COUNT = 75;
//    public static final int BLINK_COUNT = 4;
    public static final int ELSE_MULTIPLY_BY = 2024;
    static String year = "2024";
    static String day = "11";
    static String puzzleTitle = "Plutonian Pebbles";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();
        solvePartOneAndTwo();
        logStartP2();
        closeInput();
        logEndP2();
    }

    static List<String> stones = new ArrayList<>();

    private static void solvePartOneAndTwo() {
        // load initial stones:
        stones.addAll(Arrays.stream(inputLines.getFirst().split(" ")).toList());

        for (int i = 1; i <= BLINK_COUNT; i++) {
            transformStonesP1();
            log.info("After {} blink(s): {}", i, stones.size());

            if (i == BLINK_COUNT_P1) {
                solutionP1 = stones.size();
                log.info("""
                Part 1 solution:
                 Consider the arrangement of stones in front of you.
                 How many stones will you have after blinking 25 times?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
            }
        }

        solutionP2 = stones.size();

        log.info("""
                Part 2 solution:
                 How many stones would you have after blinking a total of 75 times?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));
    }

    private static void transformStonesP1() {
        int stonesInitialCount = stones.size();
        for (int i = 0; i < stonesInitialCount; i++) {
            String stone = stones.get(i);
            // RULES:
            if (stone.equals("0")) { // 0 -> 1
                stones.set(i, "1");
            } else if (stone.length() % 2 == 0) { // even digits -> split into 2
                // left stone becomes the old stone
                String leftStone = stone.substring(0, stone.length() / 2);
                stones.set(i, leftStone);
                // right stone is new and inserted right after the old one
                String rightStone = valueOf(parseLong(stone.substring(stone.length() / 2)));
                stones.add(i + 1, rightStone);

                // adjust the indexes (if not LAST?):
                i++;
                stonesInitialCount++;
            } else {
                // else -> multiply by 2024
                stones.set(i, valueOf(parseLong(stone) * ELSE_MULTIPLY_BY));
            }
        }
    }
}
