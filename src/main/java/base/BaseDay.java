package base;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

@Slf4j
public class BaseDay {
    protected static String inputFileName;
    protected static List<String> inputLines = new ArrayList<>();
    protected static Object solutionP1 = 0;
    protected static Object solutionP2 = 0;
    private static long startTime;
    private static long p2Start;

    protected static void loadDayInput() {
        loadInput(inputFileName, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
        }
    }

    protected static void logStartP1(String year, String day, String title) {
        log.info("----   ADVENT Of code   {}    ----", year);
        startTime = new Date().getTime();
        log.info(":::START = " + LocalDateTime.ofEpochSecond(startTime / 1000, 0, ZoneOffset.ofHours(2)));
        log.info("                ---=== Day {} ===---     ", day);
        log.info("                  - {} -     ", title);

        log.info("    ---=== Part 1 ===---     ");
    }

    protected static void logStartP2() {
        p2Start = new Date().getTime();
        log.info("P1 Duration: " + (p2Start - startTime) + "ms (" + (p2Start - startTime) / 1000 + "s)");

        log.info("=========================================================================================");
        log.info("    ---=== Part 2 ===---     ");
    }

    protected static void logEndP2() {
        long end = new Date().getTime();
        log.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        log.info("==========");
        log.info("Total Duration: " + (end - startTime) + "ms (" + (end - startTime) / 1000 + "s)");

        log.info(":::END = " + end);
        log.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

}
