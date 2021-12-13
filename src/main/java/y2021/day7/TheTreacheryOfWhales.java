package y2021.day7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class TheTreacheryOfWhales {

    private static final Logger logger = LoggerFactory.getLogger(TheTreacheryOfWhales.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2021/day7_input.txt";
//        private static final String INPUT_FILE_NAME = "debug.txt";
    private static final List<Integer> crabs = new ArrayList<>();

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 7 ===---     ");
        logger.info("                  - The Treachery of Whales -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, ",");
        do {
            final String readFromInput = getMainIn().next();
            crabs.add(Integer.parseInt(readFromInput.trim()));
        } while (getMainIn().hasNext());
//        Integer.MAX_VALUE = 2147483647
//        Short.MAX_VALUE = 32767
//        Byte.MAX_VALUE = 127
//        crabs.sort(Integer::compareTo);

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

    private static void solvePartOne() {

        Map<Integer, Integer> sumsToPositionX = new HashMap<>();
        int currPos = 0;
        int minSum = Integer.MAX_VALUE, minPosIndex = -1;
        do {
//            for each pos index, loop the whole list and sum the distances
//            store distances, finally find the smallest
            final Integer finalCurrPos = currPos;
            int sumForThisPos = crabs.stream().map(crab -> Math.abs(crab - finalCurrPos)).mapToInt(Integer::valueOf).sum();
            sumsToPositionX.put(currPos, sumForThisPos);
            if(sumForThisPos < minSum){
                minSum = sumForThisPos;
                minPosIndex = currPos;
            }
            currPos++;
        } while (currPos < 1000);

        logger.info("    Part 1 solution:" +
                "\n Best position= [{}] " +
                "\n How much fuel must they spend to align to that position?= [{}]", minPosIndex, minSum);

    }

    private static void solvePartTwo() {

        logger.info("    Part 2 solution:\n YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
    }

}
