import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.loadInput;

public class Template {

    private static final Logger logger = LoggerFactory.getLogger(Template.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_201x/dayXX_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   201x    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + start);
        logger.info("                ---=== Day XX ===---     ");
        logger.info("                  - XXXXXXXXX -     ");

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


        logger.info("    Part 1 solution:   XXXXXX= [");

    }

    private static void partTwo() {


        logger.info("    Part 2 solution:   YYYYYYYYYYYY= [");
    }

}
