package vtpaoc.y2022.day10;

import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static vtpaoc.helper.InputLoader.*;

public class CathodeRayTube {

    private static final Logger logger = LoggerFactory.getLogger(CathodeRayTube.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2022/day10_input.txt";
    //    private static final String INPUT_FILE_NAME = "debug.txt";
    private static int registerXvalue = 1; // register X initial value
    private static int cycleValue = 0;// cycles initial value

    static List<String> inputLines = new ArrayList<>();
    private static int signalStrengthsSum;
    private static boolean isEndP1;
    private static final AtomicInteger counter = new AtomicInteger(0);

    private static final List<Character> crt = new ArrayList<>();


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

        solve();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("=========================================================================================\n");
        logger.info("    ---=== Part 2 ===---     ");
        
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
     * P1: Find the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles.
     * <br/> What is the sum of these six signal strengths?
     * <p/>
     * P2: Render the image given by your program.
     * <br/>What eight capital letters appear on your CRT?
     */
    private static void solve() {

        Pattern cmdPattern = Pattern.compile("^(noop|addx)(.*?)$");

        for (String line : inputLines) {
            counter.incrementAndGet();
            if (isEndP1) {
                logger.info("    Part 1 solution:\n Find the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles." +
                        "\nWhat is the sum of these six signal strengths?= [{}]\n", signalStrengthsSum);
            }
            Matcher m = cmdPattern.matcher(line);
            final String lineCmd;
            final int addXvalue;
            if (m.find()) {
                lineCmd = m.group(1);
                addXvalue = Integer.parseInt(m.group(2).trim().isEmpty() ? "0" : m.group(2).trim());

                work(lineCmd, addXvalue);
            } else {
                throw new Error("Command not parsed successfully! => " + line);
            }
        }

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
                logger.info("[{}] Executing cmd '{}'. Increasing cycle from {} by 1", counter.get(), cmd, cycleValue);
                cycleValue += 1;
                // make recapitulation of the signal strength:
                calcSignalStrength();
                drawCrt();
                break;
            }
            case ADDX: {
                logger.info("[{}] executing cmd '{}'{}. Increasing cycle from {} by 1 and then 1 again, but check in the middle" +
                                "\n Add {} to registerX= {} at the end."
                        , counter.get(), cmd, xToAdd, cycleValue, xToAdd, registerXvalue);
                cycleValue += 1;
                // make recapitulation of the signal strength:
                calcSignalStrength();
                drawCrt();
                cycleValue += 1;
                // make recapitulation of the signal strength:
//                DURING !!!!! (before the addition of the X value to register)
                calcSignalStrength();
                drawCrt();
                registerXvalue += xToAdd;
                break;
            }
            default:
                throw new Error("Unknown command '" + cmd + "'. Valid values are: " + Arrays.asList(NOOP, ADDX));
        }
    }

    private static void drawCrt() {
        logger.info("p2[{}] Drawing pixel(cycle) '{}' (out of {})", counter.get(), cycleValue, crt.size());
        // if cycle (pixel currently being drawn) matches one of the 3 pixels of a sprite:
        // sprite => [-1, registerX, +1]
        Range<Integer> sprite = Range.of(registerXvalue - 1, registerXvalue + 1);
        if (sprite.contains((cycleValue - 1) % 40)) {
            crt.add('#');
        } else {
            crt.add('.');
        }

        StringBuffer sb = new StringBuffer("\n");
        for (int i = 0; i < crt.size(); i++) {
            sb.append(crt.get(i));
            if ((i + 1) % 40 == 0) {
                sb.append('\n');
            }
        }
        logger.info("Part 2 solution:\n {}", sb);
    }

    private static void calcSignalStrength() {
        // pull stats on 20, 60, 100, 140, 180 and 220th cycle:
        if (cycleValue == 20 || cycleValue == 60 || cycleValue == 100 || cycleValue == 140 || cycleValue == 180 || cycleValue == 220) {
            logger.info("[{}] Pick signal strength at cycle {}. Prev sum= {}, Curr strength: {}*{}={} (cycleValue * registerXvalue)",
                    counter.get(), cycleValue, signalStrengthsSum, cycleValue, registerXvalue, (cycleValue * registerXvalue));
            signalStrengthsSum += (cycleValue * registerXvalue);
            //final round:
            isEndP1 = (cycleValue == 220);
        } else {
            isEndP1 = false;
        }

    }
}
