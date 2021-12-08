package y2021.day5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class HydrothermalVenture {

    private static final Logger logger = LoggerFactory.getLogger(HydrothermalVenture.class.getSimpleName());
            private static final String INPUT_FILE_NAME = "year_2021/day5_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";
    private static final List<Line> lines = new ArrayList<>();
    public static final int MAP_SIZE = 1000;

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 5 ===---     ");
        logger.info("              - Hydrothermal Venture -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            lines.add(new Line(line));
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
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOne() {

        final List<Line> horizontalAndVerticalLines = lines.stream()
                .filter(Line.horizontalLine.or(Line.verticalLine)).collect(Collectors.toList());

        final int[][] mapOfHorizontalAndVertical = drawMap(horizontalAndVerticalLines);
        int solution = countOverlappingPoints(mapOfHorizontalAndVertical, 2);
        logger.info("    Part 1 solution:\n Consider only horizontal and vertical lines. " +
                "At how many points do at least two lines overlap= [{}]", solution);
    }


    private static void solvePartTwo() {

        final List<Line> horizVerticAndDiagLines = lines.stream()
                .filter(Line.horizontalLine.or(Line.verticalLine).or(Line.diagonalLine)).collect(Collectors.toList());

        final int[][] fullMap = drawMap(horizVerticAndDiagLines);
        int solution2 = countOverlappingPoints(fullMap, 2);

        logger.info("    Part 2 solution:\nConsider all of the lines. " +
                "At how many points do at least two lines overlap?= [{}]", solution2);
    }

    private static String prettyPrintMapOfLines(final int[][] map) {
        return Arrays.deepToString(map).replace("], ", "], \n ").replace(", ", "")
                .replace("0", ".").replace("[[", "[").replace("]]", "]");
    }

    /**
     * Draws a map out of the provided lines.
     * <pre>
     *     In this diagram, the top left corner is 0,0 and the bottom right corner is 9,9.
     *     Each position is shown as the number of lines which cover that point
     *     or 0 if no line covers that point.
     * </pre>
     *
     * @param lines the lines to be used for drawing the map
     * @return the produced map
     */
    private static int[][] drawMap(final List<Line> lines) {
        int[][] generatedMap = new int[MAP_SIZE][MAP_SIZE];
        for (Line line : lines) {
            logger.info("drawing line {}", line);
            int lineLength;
            if (Line.horizontalLine.or(Line.verticalLine).test(line)) {
                //horizontal or vertical line
                logger.info("Drawing {} line...", Line.horizontalLine.test(line) ? "horizontal" : "vertical");
                lineLength = 1 + Math.abs(line.getX1() - line.getX2()) + Math.abs(line.getY1() - line.getY2());
                for (int i = 0; i < lineLength; i++) {
                    if (Line.horizontalLine.test(line)) {
                        //draw horizontally:
                        generatedMap[line.getY1()][Math.min(line.getX1(), line.getX2()) + i]++;
                    } else {
                        //draw vertically:
                        generatedMap[Math.min(line.getY1(), line.getY2()) + i][line.getX1()]++;
                    }
//                    logger.info("map state after line <{}::{}> being drawn: \n {}", line, i, prettyPrintMapOfLines(generatedMap));
                }
            } else {
                logger.info("Drawing diagonal line...");
                //diagonal line:
                lineLength = 1 + Math.abs(line.getX1() - line.getX2());
                boolean increaseV;
                int hStart, vStart, vMultiplier;
                //h ALWAYS increases -> [y][x= 0...length]:
                hStart = Math.min(line.getX1(), line.getX2());
//              v may increase or decrease -> [y= min...max OR max...min][x]
//              the Y of the min(x) is the starting point for V:
//              if the Y of min(x) is > the Y of max(X) => decrease (-1), else increase (1)
                if (line.getX1() < line.getX2()) {
                    vMultiplier = line.getY1() > line.getY2() ? -1 : 1;
                } else {
                    vMultiplier = line.getY2() > line.getY1() ? -1 : 1;
                }

                // define vStat, if direction is decrease, get max(y), else get min(y)
                if (vMultiplier == -1) {
                    vStart = Math.max(line.getY1(), line.getY2());
                } else {
                    vStart = Math.min(line.getY1(), line.getY2());
                }
//                 x1 y1 x2 y2
                //[8, 0][0, 8] diagonal -> V decrease, vStart 8
//                [6, 4][2, 0] diagonal -> V increase, vStart 0
//                [0, 0][8, 8] diagonal -> V increase, vStart 0

                for (int i = 0; i < lineLength; i++) {
                    generatedMap[vStart + (vMultiplier * i)][hStart + i]++;
//                    logger.info("map state after line <{}::{}.{}> being drawn: \n {}", line, i, vStart, prettyPrintMapOfLines(generatedMap));
                }
            }
//            logger.info("map state after WHOLE line <{}> was drawn: \n {}", line, prettyPrintMapOfLines(generatedMap));
        }
        logger.info("Final map state: \n {}", prettyPrintMapOfLines(generatedMap));
        return generatedMap;
    }

    private static int countOverlappingPoints(final int[][] map, final int overlapsMoreThan) {
        return (int) Arrays.stream(map).flatMapToInt(Arrays::stream).filter(x -> x >= overlapsMoreThan).count();
    }

}
