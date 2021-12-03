package y2021.day3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class BinaryDiagnostic {

    private static final Logger logger = LoggerFactory.getLogger(BinaryDiagnostic.class.getSimpleName());
        private static final String INPUT_FILE_NAME = "year_2021/day3_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + start);
        logger.info("                ---=== Day 003 ===---     ");
        logger.info("                      - --- Binary Diagnostic --- -     ");
        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        partOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("=========================================================================================");
        logger.info("    ---=== Part 2 ===---     ");

        partTwo();

        closeInput();

        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
    }

    private static void partOne() {
        Map<Integer, Integer> onesCountPerPos = new HashMap<>();
        Map<Integer, Integer> zerosCountPerPos = new HashMap<>();
        StringBuilder gammaRate = new StringBuilder();
        StringBuilder epsilonRate = new StringBuilder();

        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            int pos = 1;
            for (char ch : line.toCharArray()) {
                final Integer posValue = Integer.parseInt(String.valueOf(ch));
                onesCountPerPos.put(pos,
                        onesCountPerPos.getOrDefault(pos, 0) + posValue);
                zerosCountPerPos.put(pos,
                        zerosCountPerPos.getOrDefault(pos, 0) + (1 - posValue));

                pos++;
            }
        }

        //determine the most and least common bits and set gamma and epsilon:
        for(int i : onesCountPerPos.keySet()){
            // gamma rate is the most common bit (sum)
            gammaRate.append(onesCountPerPos.get(i) > zerosCountPerPos.get(i) ? 1 : 0);
            // epsilon rate is the least common bit (sum)
            epsilonRate.append(onesCountPerPos.get(i) < zerosCountPerPos.get(i) ? 1 : 0);
        }

        final int gammaDecimal = Integer.parseInt(gammaRate.toString(), 2);
        final int epsilonDecimal = Integer.parseInt(epsilonRate.toString(), 2);

        logger.info("gammaRate  = {} => {}", gammaRate, gammaDecimal);
        logger.info("epsilonRate= {} => {}", epsilonRate, epsilonDecimal);

        final int solution = gammaDecimal * epsilonDecimal;
        logger.info("Part 1 solution:\nWhat is the power consumption of the submarine? = {}", solution);
    }

    private static void partTwo() {

        logger.info("Part 2 solution:\n = {}", "x2");
    }

}
