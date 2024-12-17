package vtpaoc.base;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.helper.InputLoader;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static vtpaoc.helper.InputLoader.loadInput;

@Slf4j
public class BaseDay {
    protected static String inputFileName;
    protected static List<String> inputLines = new ArrayList<>();
    protected static String inputWhole;
    protected static Object solutionP1 = 0;
    protected static Object solutionP2 = 0;
    private static long startTime;
    private static long p2Start;

    protected static void loadDayInputLines() {
        loadInput(inputFileName, "");
        while (InputLoader.getMainIn().hasNextLine()) {
            String line = InputLoader.getMainIn().nextLine();
            inputLines.add(line);
        }
    }

    protected static void loadDayInputWhole() {
        loadInput(inputFileName, "");
        StringBuilder sb = new StringBuilder();
        while (InputLoader.getMainIn().hasNextLine()) {
            String line = InputLoader.getMainIn().nextLine();
            sb.append(line);
            sb.append("\n");
        }
        inputWhole = sb.toString();
    }

    protected static void logStartP1(String year, String day, String title) {
        log.info("----   ADVENT Of code   {}    ----", year);
        startTime = new Date().getTime();
        log.info(":::START = {}", LocalDateTime.ofEpochSecond(startTime / 1000, 0, ZoneOffset.ofHours(2)));
        log.info("                ---=== Day {} ===---     ", day);
        log.info("                  - {} -     ", title);

        log.info("    ---=== Part 1 ===---     ");
    }

    protected static void logStartP2() {
        p2Start = new Date().getTime();
        log.info("P1 Duration: {}ms ({}s)", p2Start - startTime, (p2Start - startTime) / 1000);

        log.info("=========================================================================================");
        log.info("    ---=== Part 2 ===---     ");
    }

    protected static void logEndP2() {
        long end = new Date().getTime();
        log.info("P2 Duration: {}ms ({}s)", end - p2Start, (end - p2Start) / 1000);
        log.info("==========");
        log.info("Total Duration: {}ms ({}s)", end - startTime, (end - startTime) / 1000);

        log.info(":::END = {}", end);
        log.info(":::END = {}", LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

}
