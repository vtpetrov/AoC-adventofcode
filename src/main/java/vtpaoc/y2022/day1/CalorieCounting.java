package vtpaoc.y2022.day1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static vtpaoc.helper.InputLoader.*;

public class CalorieCounting {

    private static final Logger logger = LoggerFactory.getLogger(CalorieCounting.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2022/day01_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static int[] sums = new int[250];

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 01 ===---     ");
        logger.info("                - Calorie Counting -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        solvePartOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("================================");
        logger.info("    ---=== Part 2 ===---     ");

        solvePartTwo();

        closeInput();


        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========----------------==============");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOne() {

        AtomicInteger counter = new AtomicInteger(0);
        int maxSum = 0;
        int currSum = 0;

        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
//            logger.info("line {}: {}", counter.getAndIncrement(), line);

            if (!line.equals("")) {
                currSum += Integer.parseInt(line);
            } else {
                maxSum = Math.max(maxSum, currSum);
                sums[counter.getAndIncrement()] = currSum;
                currSum = 0;
            }
        }

        //final check for the last elf:
        maxSum = Math.max(maxSum, currSum);

        logger.info("    Part 1 solution:\n the most calories Elf is caring= [{}]\n", maxSum);

    }

    private static void solvePartTwo() {
        //sort the sums in ascending order
        Arrays.sort(sums);
        // get the last 3 (biggest) and make new array out of them:
        int[] topThree = Arrays.copyOfRange(sums, sums.length-3, sums.length);

        // sum the elements of the topThree array:
        int sumOfTopThree = Arrays.stream(topThree).sum();

//      solution: 207576
        logger.info("    Part 2 solution:\n the SUM of calories top three Elves are carrying= [{}]\n", sumOfTopThree);
    }

}
