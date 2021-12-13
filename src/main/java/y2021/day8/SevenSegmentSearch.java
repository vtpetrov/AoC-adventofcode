package y2021.day8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Predicate;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class SevenSegmentSearch {

    private static final Logger logger = LoggerFactory.getLogger(SevenSegmentSearch.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2021/day8_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 8 ===---     ");
        logger.info("              - Seven Segment Search -     ");

        logger.info("    ---=== Part 1 ===---     ");

        solvePartOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("=========================================================================================");
        logger.info("    ---=== Part 2 ===---     ");

        solvePartTwo();

        closeInput();


        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    /**
     * In the output values, how many times do digits 1, 4, 7, or 8 appear?
     * Those are signals with length 2, 3, 4 and 7
     */
    private static void solvePartOne() {

        int easyCounter = 0;
        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            String inputSegmentsPart = line.substring(0, line.indexOf(" |"));
            String outputPart = line.substring(line.indexOf("| ") + 2);

//            length 2, 3, 4 and 7:
            Predicate<String> length2 = x -> x.length() == 2;
            Predicate<String> length3 = x -> x.length() == 3;
            Predicate<String> length4 = x -> x.length() == 4;
            Predicate<String> length7 = x -> x.length() == 7;
            Predicate<String> easyNumber = s -> length2.or(length3).or(length4).or(length7).test(s);

            String[] outputNumbers = outputPart.split(" ", 4);
            easyCounter += Arrays.stream(outputNumbers).filter(easyNumber).count();
        }

        logger.info("    Part 1 solution:" +
                "\n In the output values, how many times do digits 1, 4, 7, or 8 appear?= [{}]", easyCounter);

    }

    private static void solvePartTwo() {


        logger.info("    Part 2 solution:\n " +
                "For each entry, determine all of the wire/segment connections and decode the four-digit output values." +
                " What do you get if you add up all of the output values?= [{}]", "<solution_goes_here>");
    }

}
