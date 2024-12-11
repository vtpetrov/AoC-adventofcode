package y2024.day11;

import base.BaseDay;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static helper.InputLoader.closeInput;
import static helper.Misc.prettyPrintNumber;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;

@Slf4j
public class PlutonianPebbles extends BaseDay {

        public static final int BLINK_COUNT = 25;
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
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    static List<String> stonesP1 = new ArrayList<>();

    private static void solvePartOne() {
        // load initial stones:
        stonesP1.addAll(Arrays.stream(inputLines.getFirst().split(" ")).toList());

        for (int i = 1; i <= BLINK_COUNT; i++) {
            transformStonesP1();
//            log.info("After {} blink(s): {}", i, stonesP1);
        }

        solutionP1 = stonesP1.size();

        log.info("""
                Part 1 solution:
                 Consider the arrangement of stones in front of you.
                 How many stones will you have after blinking 25 times?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void transformStonesP1() {
        int stonesInitialCount = stonesP1.size();
        for (int i = 0; i < stonesInitialCount; i++) {
            String stone = stonesP1.get(i);
            // RULES:
            if (stone.equals("0")) { // 0 -> 1
                stonesP1.set(i, "1");
            } else if (stone.length() % 2 == 0) { // even digits -> split into 2
                // left stone becomes the old stone
                String leftStone = stone.substring(0, stone.length() / 2);
                stonesP1.set(i, leftStone);
                // right stone is new and inserted right after the old one
                String rightStone = valueOf(parseLong(stone.substring(stone.length() / 2)));
                stonesP1.add(i + 1, rightStone);

                // adjust the indexes (if not LAST?):
                i++;
                stonesInitialCount++;
            } else {
                // else -> multiply by 2024
                stonesP1.set(i, valueOf(parseLong(stone) * ELSE_MULTIPLY_BY));
            }
        }
    }

    private static void solvePartTwo() {

        log.info("""
                Part 2 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }
}
