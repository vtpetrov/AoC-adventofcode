package vtpaoc.base;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.helper.InputUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;
import static vtpaoc.helper.InputUtils.loadInput;

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
        while (InputUtils.getMainIn().hasNextLine()) {
            String line = InputUtils.getMainIn().nextLine();
            inputLines.add(line);
        }
    }

    protected static void loadDayInputWhole() {
        loadInput(inputFileName, "");
        StringBuilder sb = new StringBuilder();
        while (InputUtils.getMainIn().hasNextLine()) {
            String line = InputUtils.getMainIn().nextLine();
            sb.append(line);
            sb.append("\n");
        }
        inputWhole = sb.toString();
    }

    protected static void logStartP1(String year, String day, String title) {
        log.info("----   ADVENT Of code   {}    ----", year);
        startTime = new Date().getTime();
        log.info(":::START = {}\n", LocalDateTime.ofEpochSecond(startTime / 1000, 0, ZoneOffset.ofHours(2)));
        logDayHeader(parseInt(day), title);

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
        log.info("***====================================================***");
        log.info("Total Duration: {}ms ({}s)\n", end - startTime, (end - startTime) / 1000);

        log.info(":::END = {}", end);
        log.info(":::END = {}", LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    public static void logDayHeader(int day, String title) {
        final String indent = "                ";  // your original left indent

        String headerRaw = String.format("---=== Day %02d ===---", day);
        String header = indent + headerRaw;

        String titleDecorated = String.format("- %s -", title);

        // Compute centered padding (relative to the non-indented header width)
        int padding = Math.max(0, (headerRaw.length() - titleDecorated.length()) / 2);

        String paddedTitle = indent + " ".repeat(padding) + titleDecorated;

        log.info("{}", header);
        log.info("{}\n", paddedTitle);
    }


}
