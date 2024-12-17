package vtpaoc.y2022.day3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Pattern;

import static vtpaoc.helper.InputUtils.*;

public class RucksackReorganization {

    private static final Logger logger = LoggerFactory.getLogger(RucksackReorganization.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2022/day03_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static final List<String[]> rucksackItems = new ArrayList<>();

    static Pattern smallLett = Pattern.compile("[a-z]");
    static Pattern bigLett = Pattern.compile("[A-Z]");

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 03 ===---     ");
        logger.info("             - Rucksack Reorganization -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
            rucksackItems.add(line.split(""));
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

    private static void solvePartOne() {

        String[] pocket1;
        String[] pocket2;

        int totalSum = 0;
        //loop through all rucksacks:
        for (String[] currentRucksack : rucksackItems) {

            //set rucksack pockets content:
            pocket1 = Arrays.copyOfRange(currentRucksack, 0, currentRucksack.length / 2);
            pocket2 = Arrays.copyOfRange(currentRucksack, currentRucksack.length / 2, currentRucksack.length);


            Set<String> pocket1Set = new HashSet<>(Arrays.asList(pocket1));
            Set<String> pocket2Set = new HashSet<>(Arrays.asList(pocket2));

            //leave only duplicate items in collection 1:
            pocket1Set.retainAll(pocket2Set);

            totalSum += calcPriority(pocket1Set.stream().findFirst().orElse("0"));

        }

        logger.info("    Part 1 solution:\n the sum of the priorities of duplicate item types= [{}]\n", totalSum);
    }


    private static void solvePartTwo() {
        int totalBadgeSum = 0;
        for (int i = 0; i < rucksackItems.size() - 2; i += 3) {
            Set<String> rack1 = new HashSet<>(Arrays.asList(rucksackItems.get(i)));
            Set<String> rack2 = new HashSet<>(Arrays.asList(rucksackItems.get(i + 1)));
            Set<String> rack3 = new HashSet<>(Arrays.asList(rucksackItems.get(i + 2)));

            rack1.retainAll(rack2);
            rack1.retainAll(rack3);

            int groupBadgeSum = calcPriority(rack1.stream().findFirst().orElse("0"));
            totalBadgeSum += groupBadgeSum;
        }

        logger.info("    Part 2 solution:\n the sum of badge items= [{}]\n", totalBadgeSum);
    }

    /**
     * Return priority value for a letter based on the requirements:
     * <li/>Lowercase item types a through z have priorities 1 through 26.
     * <li/>Uppercase item types A through Z have priorities 27 through 52.
     * <br/>
     * This is done by calculating the numeric value for the ascii code of this letter:
     * <br/>{@code (int) letter.charAt(0) }
     * <br/>then subtracting a predefined constant (96 or 28) to calc the final value.
     *
     * @param letter the item to calculate priority for
     * @return the calculated priority (a number between 1-52)
     */
    private static int calcPriority(String letter) {

        int val = 0;
        if (smallLett.matcher(letter).find()) { //if small letter, subtract 96
            val = (int) letter.charAt(0) - 96;
        } else if (bigLett.matcher(letter).find()) { //if big letter, subtract 38
            val = (int) letter.charAt(0) - 38;
        }

        return val;
    }

}
