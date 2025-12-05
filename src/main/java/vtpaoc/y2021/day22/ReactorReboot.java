package vtpaoc.y2021.day22;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static vtpaoc.helper.InputUtils.*;

public class ReactorReboot {

    private static final Logger logger = LoggerFactory.getLogger(ReactorReboot.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2021/day22_input.txt";
//        private static final String INPUT_FILE_NAME = "debug.txt";
    public static final int BOUNDARY = 50;

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 22 ===---     ");
        logger.info("                  - Reactor Reboot -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

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
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOne() {
        //        on= 1, off= 0,
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> space = new HashMap<>();
        int processedLines = 0, lineIndex = 0;

        long cubesOn = 0;
        while (getMainIn().hasNextLine()) {
            lineIndex++;
            String line = getMainIn().nextLine();

            //start setting on/off state directly from input:
            Pattern opAndRange = Pattern.compile("(on|off).(.*)");
            Matcher lineMatcher = opAndRange.matcher(line);
            lineMatcher.find();
            OperationOnOff operation = OperationOnOff.fromString(lineMatcher.group(1));
            String[] ranges = lineMatcher.group(2).split(",");
            Pattern xyzRanges = Pattern.compile("([xyz])=(.*)\\.\\.(.*)");
            Matcher xMatcher = xyzRanges.matcher(ranges[0]);
            Matcher yMatcher = xyzRanges.matcher(ranges[1]);
            Matcher zMatcher = xyzRanges.matcher(ranges[2]);
            for (Matcher matcher : Arrays.asList(xMatcher, yMatcher, zMatcher)) {
                matcher.find();
            }

            int xFrom = Integer.parseInt(xMatcher.group(2));
            int xTo = Integer.parseInt(xMatcher.group(3));

            int yFrom = Integer.parseInt(yMatcher.group(2));
            int yTo = Integer.parseInt(yMatcher.group(3));

            int zFrom = Integer.parseInt(zMatcher.group(2));
            int zTo = Integer.parseInt(zMatcher.group(3));

            // if any cube in this steps fall off the boundary (50), ignore this step/line and move to the next one:
            if (Math.abs(xFrom) > BOUNDARY || Math.abs(xTo) > BOUNDARY
                    || Math.abs(yFrom) > BOUNDARY || Math.abs(yTo) > BOUNDARY
                    || Math.abs(zFrom) > BOUNDARY || Math.abs(zTo) > BOUNDARY) {
                logger.info("Skipping line {}) as OUT of boundary: {}", lineIndex, line);
                continue;
            }

            // populate cube values for this step:
            int cubesInThisStep = 0, flipped = 0, overlap = 0;
            for (int x = xFrom; x <= xTo; x++) {
                space.computeIfAbsent(x, k -> new HashMap<>());
                for (int y = yFrom; y <= yTo; y++) {
                    space.get(x).computeIfAbsent(y, k -> new HashMap<>());
                    for (int z = zFrom; z <= zTo; z++) {
                        Integer current = space.get(x).get(y).get(z);
                        if (current != null) {
                            if (current != operation.toNumber()) {
                                flipped++;
                            } else {
                                overlap++;
                            }
                        }
                        space.get(x).get(y).put(z, operation.toNumber());

                        cubesInThisStep++;
                    }
                }
            }

            List<Integer> flattenSpace = space
                    .values()
                    .stream()
                    .flatMap(
                            ys -> ys.values()
                                    .stream()
                                    .flatMap(zs -> zs.values().stream())).collect(Collectors.toList());
            cubesOn = flattenSpace.stream().filter(z -> z == 1).count();
            long cubesOff = flattenSpace.stream().filter(z -> z == 0).count();
            logger.info("Processed line {}): [{}], c= {}, f= {}, overlap= {}", lineIndex, line, cubesInThisStep, flipped, overlap);
            logger.info("       Cubes ON= {},   Cubes OFF= {}", cubesOn, cubesOff);
//            logger.info("space:\n{}", space);
            processedLines++;
        }

        logger.info("    Part 1 solution:\n      how many cubes are on?= [{}]", cubesOn);

    }

    private static void solvePartTwo() {


        logger.info("    Part 2 solution:\n YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
    }

}
