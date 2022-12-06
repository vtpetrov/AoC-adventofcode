package y2022.day6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class TuningTrouble {

    private static final Logger logger = LoggerFactory.getLogger(TuningTrouble.class.getSimpleName());
            private static final String INPUT_FILE_NAME = "year_2022/day06_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static String solution;
    private static int x;


    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 06 ===---     ");
        logger.info("                 - Tuning Trouble -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
        }

        solve(1);
        solveV2(1);

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("================================");
        logger.info("    ---=== Part 2 ===---     ");

        solve(2);
        solveV2(2);

        closeInput();
        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========----------------==============");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));

    }

    /**
     * How many characters need to be processed before the first start-of-packet marker is detected?
     */
    private static void solve(int part) {
        setChunkLength(part);

        AtomicInteger counter = new AtomicInteger(0);
        for (String line : inputLines) {
            int lineNumber = counter.incrementAndGet();
            logger.info("Processing line {}", lineNumber);

            List<Character> packOfX = new ArrayList<>();
            int elapsedChars = 0;

            boolean packFound = false;

            while (packOfX.size() < x) {
                Character currChar = line.charAt(0);
                line = line.substring(1);
                elapsedChars++;

                if (!packOfX.contains(currChar)) {
                    packOfX.add(currChar);
                } else {
                    // clear pack up until the matching char (inclusive)
                    int delToIdx = packOfX.indexOf(currChar);
                    if (delToIdx >= 0) {
                        packOfX.subList(0, delToIdx + 1).clear();
                    }
                    packOfX.add(currChar);
                }
            }
            logger.info("Solution for line {} is {} => {}", lineNumber, elapsedChars, packOfX);
            solution = String.valueOf(elapsedChars);
        }

        logger.info("    Part {} solution:\n How many characters need to be processed before the first start marker is detected?= [{}]\n"
                , part
                , solution);
    }

    private static void solveV2(int part) {
        setChunkLength(part);

        logger.info("Finding solution for y2022 day5 part [{}], v2 ..........", part);
        AtomicInteger counter = new AtomicInteger(0);
        for (String line : inputLines) {
            int lineNumber = counter.incrementAndGet();
            logger.info("Processing line {}", lineNumber);

            List<String> lineAsList = Arrays.asList(line.split(""));
            int lineSolution = findLineSolution(lineAsList);

            logger.info("Solution for line {} is {} => {}",
                    lineNumber, lineSolution, lineAsList.subList(lineSolution - x, lineSolution));
        }
        logger.info("****************************************************************************");
    }

    private static void setChunkLength(int part) {
        if (part == 1) {
            x = 4; // start of a packet
        } else if (part == 2) {
            x = 14; // start of a message
        } else {
            throw new Error("Invalid part number. Accepted are 1 or 2");
        }
    }

    private static int findLineSolution(List<String> line) {
        for (int i = 0; i < line.size(); i++) {
            int cursor = i + x;
            List<String> subStr = line.subList(i, cursor);
            if (subStr.size() == new HashSet<>(subStr).size()) {
                return cursor;
            }
        }
        return -1;
    }
}
