package y2023.day5;

import base.BaseDay;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static helper.InputLoader.closeInput;
import static helper.Misc.prettyPrintNumber;

@Slf4j
public class SeedsAndFertilizers extends BaseDay {

    static {
        inputFileName = "year_2023/day05_input.txt";
        // inputFileName = "debug.txt";
    }

    static String year = "2023";
    static String day = "08";
    static String puzzleTitle = "If You Give A Seed A Fertilizer";

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();

        populateRangesFromInput();

        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void populateRangesFromInput() {

        for (String line : inputLines) {

            List<Range> seedToSoilRanges = new LinkedList<>();

            Range seedToSoil = Range.between(0, destinationStart - 1);
            Range seedToSoil2 = Range.between(destinationStart, destinationStart + rangeLength);

            Stream<Range> rangeStream = seedToSoilRanges.parallelStream();
        }
    }

    private static void solvePartOne() {

        log.info("""
                Part 1 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void solvePartTwo() {

        log.info("""
                Part 2 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }

}
