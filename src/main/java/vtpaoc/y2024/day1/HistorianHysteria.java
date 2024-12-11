package vtpaoc.y2024.day1;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static vtpaoc.helper.InputLoader.*;

@Slf4j
public class HistorianHysteria {

    private static final String INPUT_FILE_NAME = "year_2024/day01_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    static List<Integer> letfList = new ArrayList<>();
    static List<Integer> rightList = new ArrayList<>();

    public static void main(String[] args) {
        log.info("----   ADVENT Of code   2024    ----");
        long start = new Date().getTime();
        log.info(":::START = {}", LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        log.info("                ---=== Day 01 ===---     ");
        log.info("                - Historian Hysteria -     ");

        log.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
        }

        final String SEPARATOR_3_SPACES = " {3}";

        for (String line : inputLines) {
            log.debug("line= [{}]", line);
            String[] split = line.split(SEPARATOR_3_SPACES);
            letfList.add(Integer.parseInt(split[0]));
            rightList.add(Integer.parseInt(split[1]));
        }
        log.debug("""
                Lists:
                            left ={}
                            right={}""", letfList, rightList);


        solvePartOne();

        long p2Start = new Date().getTime();
        log.info("P1 Duration: {}ms ({}s)", p2Start - start, (p2Start - start) / 1000);

        log.info("=========================================================================================");
        log.info("    ---=== Part 2 ===---     ");

        solvePartTwo();

        closeInput();

        long end = new Date().getTime();
        log.info("P2 Duration: {}ms ({}s)", end - p2Start, (end - p2Start) / 1000);
        log.info("==========");
        log.info("Total Duration: {}ms ({}s)", end - start, (end - start) / 1000);

        log.info(":::END = {}", end);
        log.info(":::END = {}", LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOne() {
        long totalDistance = 0;

        //sort lists, copy to local instance of lists
        List<Integer> sortedLeftList = new ArrayList<>(letfList);
        List<Integer> sortedRightList = new ArrayList<>(rightList);

        sortedLeftList.sort(Integer::compareTo);
        sortedRightList.sort(Integer::compareTo);

        log.debug("""
                Sorted Lists:
                                left ={}
                                right={}""", sortedLeftList, sortedRightList);
        int pairDistance = 0;

        for (int i = 0; i < inputLines.size(); i++) {
            // compare pairs in each list - 1st with 1st, 2nd with 2nd, etc. Find the difference between the values
            // in each pair:
            pairDistance = Math.abs(sortedLeftList.get(i) - sortedRightList.get(i));
            log.info("pair=[{}  {}], dist= {}", sortedLeftList.get(i), sortedRightList.get(i), pairDistance);
            totalDistance += pairDistance;
        }

        log.info("""
                Part 1 solution:
                What is the total distance between your lists? = [{}]
                """, totalDistance);
    }

    private static void solvePartTwo() {
        long totalSimilarityScore = 0;

        for (int i = 0; i < inputLines.size(); i++) {
            // This time, you'll need to figure out exactly how often each number from the left list appears in the right list.
            // Calculate a total similarity score by adding up each number in the left list after multiplying it
            // by the number of times that number appears in the right list.
            int countInRight = Collections.frequency(rightList, letfList.get(i));
            long currSimilarity = (long) letfList.get(i) * countInRight;
            log.info("left= {}, count in right= {}, similarity= {}", letfList.get(i), countInRight, currSimilarity);
            totalSimilarityScore += currSimilarity;
        }

        log.info("""
                Part 2 solution:
                What is the similarity score of the two lists = [{}]""", totalSimilarityScore);
    }
}
