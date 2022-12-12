package y2022.day10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class CathodeRayTube {

    private static final Logger logger = LoggerFactory.getLogger(CathodeRayTube.class.getSimpleName());
    //    private static final String INPUT_FILE_NAME = "year_202x/day10_input.txt";
    private static final String INPUT_FILE_NAME = "debug.txt";// initial value
    private static int registerXvalue = 1;// cycles initial value
    private static int cycleValue = 0;

    static List<String> inputLines = new ArrayList<>();
    private static String solution;
    private static int signalStrengthsSum;
    private static boolean isEnd;


    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 10 ===---     ");
        logger.info("                - Cathode-Ray Tube -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
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

    public static final String NOOP = "noop";
    public static final String ADDX = "addx";


    /**
     * Find the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles.
     * <br/> What is the sum of these six signal strengths?
     */
    private static void solvePartOne() {

        Pattern cmdPattern = Pattern.compile("^(noop|addx)(.*?)$");

        while (!isEnd) {
            for (String line : inputLines) {
                Matcher m = cmdPattern.matcher(line);
                final String lineCmd;
                final int addXvalue;
                if (m.find()) {
                    lineCmd = m.group(1);
                    addXvalue = Integer
                            .parseInt(
                                    m.group(2).trim().isEmpty() ?
                                            "0" :
                                            m.group(2).trim()
                            );
                    work(lineCmd, addXvalue);
                } else {
                    throw new Error("Command not parsed successfully! => " + line);
                }
            }
        }

        logger.info("    Part 1 solution:\n What is the sum of these six signal strengths?= [{}]", signalStrengthsSum);

    }

    /**
     * Increase the cycleValue:
     * <li>by 1 for cmd 'noop'</li>
     * <li>by 2 for cmd 'addx'</li>
     *
     * @param cmd    the command to use
     * @param xToAdd the value to add to registerX at the end
     */
    private static void work(final String cmd, final int xToAdd) {
        switch (cmd) {
            case NOOP: {
                logger.info("Cmd '{}' executing. Increasing cycle from {} by 1", cmd, cycleValue);
                cycleValue += 1;
                break;
            }
            case ADDX: {
                logger.info("Cmd '{}' executing. Increasing cycle from {} by 2", cmd, cycleValue);
                cycleValue += 1;
                // make recapitulation of the signal strength:
                calcSignalStrength();
                cycleValue += 1;
                registerXvalue += xToAdd;
                // make recapitulation of the signal strength:
                calcSignalStrength();
                break;
            }
            default:
                throw new Error("Unknown command '" + cmd + "'. Valid values are: " + Arrays.asList(NOOP, ADDX));
        }
    }

    private static void calcSignalStrength() {
        // pull stats on 20, 60, 100, 140, 180 and 220th cycle:
        if (cycleValue == 20 || cycleValue == 60 || cycleValue == 100 || cycleValue == 140 || cycleValue == 180 || cycleValue == 220) {

            logger.info("Pick signal strength at {}. Prev sum= {}, Curr strength: {}*{}={} (cycleValue * registerXvalue)",
                    cycleValue, signalStrengthsSum, cycleValue, registerXvalue, (cycleValue * registerXvalue));
            signalStrengthsSum += (cycleValue * registerXvalue);
            //final round:
            isEnd = (cycleValue == 220);
        } else {
            isEnd = false;
        }

    }

    private static void solvePartTwo() {


        logger.info("    Part 2 solution:\n YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
    }

}
