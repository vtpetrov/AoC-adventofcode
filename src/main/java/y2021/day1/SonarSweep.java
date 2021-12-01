package y2021.day1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class SonarSweep {

    private static final Logger logger = LoggerFactory.getLogger(SonarSweep.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2021/day1_input.txt";
    //        private static final String INPUT_FILE_NAME = "debug.txt";
    static int[] depths = new int[2001];
    static int inputSize = 0;

    public static void main(String[] args) {


        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + start);
        logger.info("                ---=== Day 001 ===---     ");
        logger.info("                  - Sonar Sweep -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        partOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("=========================================================================================");
        logger.info("    ---=== Part 2 ===---     ");

        partTwo();

        closeInput();

        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
    }

    private static void partOne() {

        int increases = 0;
        while (getMainIn().hasNextLine()) {
            depths[inputSize] = Integer.parseInt(getMainIn().nextLine());
            if (inputSize > 0) {
                if (depths[inputSize] > depths[inputSize - 1]) {
                    increases++;
                }
            }
            inputSize++;
        }

        logger.info("input size= {}", inputSize);

        logger.info("Part 1 solution:\nHow many measurements are larger than the previous measurement? = {}", increases);
    }

    private static void partTwo() {
        logger.info("Possible sums= {}", inputSize - 2);
        int sumIncreases = 0;
        for (int i = 3; i < inputSize; i++) {
            int sum1 = depths[i - 3] + depths[i - 2] + depths[i - 1];
            int sum2 = depths[i - 2] + depths[i - 1] + depths[i];
            if (sum2 > sum1) {
                sumIncreases++;
            }
        }
        logger.info("    Part 2 solution:   \nConsider sums of a three-measurement sliding window. How many sums are larger than the previous sum? \n= {}", sumIncreases);
    }

}
