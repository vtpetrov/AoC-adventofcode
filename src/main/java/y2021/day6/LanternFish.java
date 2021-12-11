package y2021.day6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class LanternFish {

    private static final Logger logger = LoggerFactory.getLogger(LanternFish.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2021/day6_input.txt";
//        private static final String INPUT_FILE_NAME = "debug.txt";
    private static final List<Integer> fish = new ArrayList<>();


    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 06 ===---     ");
        logger.info("            - --- Day 6: Lanternfish ---      ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, ",");
        do {
            final String readFromInput = getMainIn().next();
            fish.add(Integer.parseInt(readFromInput.trim()));
        } while (getMainIn().hasNext());

        solvePartOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("=========================================================================================");
        logger.info("    ---=== Part 2 ===---     ");

//        solvePartTwo();

        closeInput();


        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOne() {
//        Each day, a 0 becomes a 6 and adds a new 8 to the end of the list, while each other
//        number decreases by 1 if it was present at the start of the day.
        logger.info("Initial state: {}", fish);
        int day = 1;
        do {
            final int lenAtDayStart = fish.size();
            for(int fIndex = 0; fIndex < lenAtDayStart; fIndex++){
                // at the beginning of the day:
                if(fish.get(fIndex) == 0){
                    // IF fish life is 0  -=>  reset it to 6 and spawn new fish with life 8 at the end of the list:
//                    logger.info("{}, spawn", fIndex);
                    fish.set(fIndex, 6);
                    fish.add(8);
                } else {
                    // ELSE  -=>  decrease fish life by 1:
//                    logger.info("{}, decrease life", fIndex);
                    fish.set(fIndex, fish.get(fIndex) - 1);
                }
            }
            logger.info("After {} days : {}", day, fish.size());
            day++;
        } while ( day <= 80);

        logger.info("    Part 1 solution:\n How many lanternfish would there be after 80 days= [{}]", fish.size());

    }

    private static void solvePartTwo() {


        logger.info("    Part 2 solution:\n YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
    }

}
