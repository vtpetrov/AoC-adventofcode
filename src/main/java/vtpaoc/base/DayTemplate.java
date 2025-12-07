package vtpaoc.base;

import lombok.extern.slf4j.Slf4j;

import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class DayTemplate extends BaseDay {

    static String year = "2025";
    static String day = "XX";
    static String puzzleTitle = "<PuzzleTitle_here>";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
        // inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        //loadDayInputLines();
        //or
        //loadDayInputWhole();
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {

        log.info("""
                Part 1 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1));
    }

    private static void solvePartTwo() {

        log.info("""
                Part 2 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2));

    }
}
