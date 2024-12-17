package vtpaoc.y2022.day4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static vtpaoc.helper.InputUtils.*;

public class CampCleanup {

    private static final Logger logger = LoggerFactory.getLogger(CampCleanup.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2022/day04_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static Integer fullOverlapsCount = 0;
    private static Integer overlappingAssignmentsCount = 0;
    private static final List<Integer> elf1Area = new ArrayList<>();
    private static final List<Integer> elf2Area = new ArrayList<>();


    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 04 ===---     ");
        logger.info("                  - Camp Cleanup -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
            parseInput(line);
            determineOverlap();
            elf1Area.clear();
            elf2Area.clear();
        }

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

    private static void determineOverlap() {
        if (elf1Area.size() <= elf2Area.size()) {
            // 1 may be contained in 2:
            elf2Area.retainAll(elf1Area);
            if (elf1Area.equals(elf2Area)) { // full overlap
                fullOverlapsCount++;
            }
            if (elf2Area.size() > 0) { // partial overlap
                overlappingAssignmentsCount++;
            }
        } else {
            // 2 may be contained in 1:
            elf1Area.retainAll(elf2Area);
            if (elf1Area.equals(elf2Area)) { // full overlap
                fullOverlapsCount++;
            }
            if (elf1Area.size() > 0) { // partial overlap
                overlappingAssignmentsCount++;
            }
        }
    }

    private static void parseInput(String line) {
        String[] elfs = line.split(",");

        String[] elf1FromTo = elfs[0].split("-");
        String[] elf2FromTo = elfs[1].split("-");

        int elf1From = Integer.parseInt(elf1FromTo[0]);
        int elf1To = Integer.parseInt(elf1FromTo[1]);
        int elf2From = Integer.parseInt(elf2FromTo[0]);
        int elf2To = Integer.parseInt(elf2FromTo[1]);

        for (int i = elf1From; i <= elf1To; i++) {
            elf1Area.add(i);
        }

        for (int i = elf2From; i <= elf2To; i++) {
            elf2Area.add(i);
        }
    }

    private static void solvePartOne() {
        logger.info("    Part 1 solution:\n FullOverlapsCount= [{}]\n", fullOverlapsCount);
    }


    private static void solvePartTwo() {
        logger.info("    Part 2 solution:\n how many assignment pairs do the ranges overlap= [{}]\n", overlappingAssignmentsCount);
    }


}
