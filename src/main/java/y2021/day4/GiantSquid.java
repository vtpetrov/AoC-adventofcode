package y2021.day4;

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

public class GiantSquid {

    private static final Logger logger = LoggerFactory.getLogger(GiantSquid.class.getSimpleName());
    //    private static final String INPUT_FILE_NAME = "year_2021/day4_input.txt";
    private static final String INPUT_FILE_NAME = "debug.txt";
    private static String line;
    private static String drawnNumbers;
    private static List<BingoBoard> bingoBoards = new ArrayList<>();

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 4 ===---     ");
        logger.info("                   - Giant Squid -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, ",");
        drawnNumbers = getMainIn().nextLine();
        while (getMainIn().hasNextLine()) {
            line = getMainIn().nextLine();
            if (line.isEmpty()) {
                //start reading new board
                BingoBoard newBoard = new BingoBoard();
                for (int l = 0; l < 5; l++) {
                    line = getMainIn().nextLine();
                    newBoard.addLine(line);
                }
                bingoBoards.add(newBoard);
            }
        }

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
    }

    private static void solvePartOne() {


        logger.info("    Part 1 solution:   XXXXXX= [{}]", "<solution_goes_here>");

    }

    private static void solvePartTwo() {


        logger.info("    Part 2 solution:   YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
    }

}
