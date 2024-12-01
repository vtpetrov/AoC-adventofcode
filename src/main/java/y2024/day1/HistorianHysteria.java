package y2024.day1;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static helper.InputLoader.*;

@Slf4j
public class HistorianHysteria {

        private static final String INPUT_FILE_NAME = "year_2024/day01_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();

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

        solvePartOne();

        long p2Start = new Date().getTime();
        log.info("P1 Duration: {}ms ({}s)", p2Start - start, (p2Start - start) / 1000);

        log.info("=========================================================================================");
        log.info("    ---=== Part 2 ===---     ");

//        solvePartTwo();

        closeInput();

        long end = new Date().getTime();
        log.info("P2 Duration: {}ms ({}s)", end - p2Start, (end - p2Start) / 1000);
        log.info("==========");
        log.info("Total Duration: {}ms ({}s)", end - start, (end - start) / 1000);

        log.info(":::END = {}", end);
        log.info(":::END = {}", LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOne() {
        final String SEPARATOR_3_SPACES = "   ";
        long totalDistance = 0;
        List<Integer> letfList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        for (String line : inputLines) {
//            log.info("line= [{}]", line);
            String[] split = line.split(SEPARATOR_3_SPACES);
            letfList.add(Integer.parseInt(split[0]));
            rightList.add(Integer.parseInt(split[1]));
        }
//        log.info("""
//                Lists:
//                            left ={}
//                            right={}""", letfList, rightList);

        //sort lists
        letfList.sort(Integer::compareTo);
        rightList.sort(Integer::compareTo);

//        log.info("""
//                Sorted Lists:
//                                left ={}
//                                right={}""", letfList, rightList);
        int pairDistance = 0;

        for (int i = 0; i < inputLines.size(); i++) {
            // compare pairs in each list - 1st with 1st, 2nd with 2nd, etc. Find the difference between the values
            // in each pair:
            pairDistance = Math.abs(letfList.get(i) - rightList.get(i));
            log.info("pair=[{}  {}], dist= {}", letfList.get(i), rightList.get(i), pairDistance);
            totalDistance += pairDistance;
        }

        log.info("totalDistance= {}", totalDistance);

        log.info("""
                Part 1 solution:
                What is the total distance between your lists? = [{}]
                """, totalDistance);
    }


    private static void solvePartTwo() {


        for (String line : inputLines) {
            log.info("-------------------------------------------------------");
            log.info("line= {}", line);
        }

        log.info("""
                 Part 2 solution:
                 TBD
                = [{}]""", "TBD");
    }
}
