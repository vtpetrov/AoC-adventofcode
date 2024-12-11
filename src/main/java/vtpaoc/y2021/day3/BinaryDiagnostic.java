package vtpaoc.y2021.day3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static vtpaoc.helper.InputLoader.*;

public class BinaryDiagnostic {

    private static final Logger logger = LoggerFactory.getLogger(BinaryDiagnostic.class.getSimpleName());
            private static final String INPUT_FILE_NAME = "year_2021/day3_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static Map<Integer, Integer> onesCountPerPos = new HashMap<>();
    static Map<Integer, Integer> zerosCountPerPos = new HashMap<>();
    static StringBuilder gammaRateBuilder = new StringBuilder(); // keeps the MOST vtpaoc.common bits (1 or 0) on this position
    static StringBuilder epsilonRateBuilder = new StringBuilder(); // keeps the LEAST vtpaoc.common bits (1 or 0) on this position
    static int gammaDecimal;
    static int epsilonDecimal;
    private static final List<String> lines = new ArrayList<>();

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

    /**
     * 1s: 1=7, 2=5, 3=8, 4=7, 5=5
     * 0s: 1=5, 2=7, 3=4, 4=5, 5=7
     */
    static Map<Integer, Map<Integer, Integer>> generateMapCountPerPosition(List<String> lines) {
        Map<Integer, Map<Integer, Integer>> theMap = new HashMap<>();
        Map<Integer, Integer> innerMapOnes = new HashMap<>();
        Map<Integer, Integer> innerMapZeroes = new HashMap<>();

        for (String line : lines) {
            int pos = 0;
            for (char ch : line.toCharArray()) {
                final Integer posValue = Integer.parseInt(String.valueOf(ch));
                innerMapOnes.put(pos,
                        innerMapOnes.getOrDefault(pos, 0) + posValue);
                innerMapZeroes.put(pos,
                        innerMapZeroes.getOrDefault(pos, 0) + (1 - posValue));
                pos++;
            }
        }

        theMap.put(1, innerMapOnes);
        theMap.put(0, innerMapZeroes);

        return theMap;
    }

    /**
     * IF theMost= TRUE and 1 and 0 are equal count, return 1
     * <br/> IF theMost= FALSE and 1 and 0 are equal count, return 0
     *
     * @param theMap  the map to be used for calc
     * @param theMost true if want most, false if want fewer
     * @param pos     0 based
     */
    static int returnMostLeastPerPosition(Map<Integer, Map<Integer, Integer>> theMap, boolean theMost, int pos) {
        try {
            if (theMost) {
                return theMap.get(1).get(pos) >= theMap.get(0).get(pos) ? 1 : 0;
            } else {
                return theMap.get(1).get(pos) < theMap.get(0).get(pos) ? 1 : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    private static void partOne() {


        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            lines.add(line);
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

        //determine the most and least vtpaoc.common bits and set gamma and epsilon:
        for (int i : onesCountPerPos.keySet()) {
            // gamma rate is the most vtpaoc.common bit (sum)
            gammaRateBuilder.append(onesCountPerPos.get(i) > zerosCountPerPos.get(i) ? 1 : 0);
            // epsilon rate is the least vtpaoc.common bit (sum)
            epsilonRateBuilder.append(onesCountPerPos.get(i) < zerosCountPerPos.get(i) ? 1 : 0);
        }

        final String gammaRateString = gammaRateBuilder.toString();
        gammaDecimal = Integer.parseInt(gammaRateString, 2);

        final String epsilonRateString = epsilonRateBuilder.toString();
        epsilonDecimal = Integer.parseInt(epsilonRateString, 2);


        logger.info("gammaRate  = {} => {}", gammaRateBuilder, gammaDecimal);
        logger.info("epsilonRate= {} => {}", epsilonRateBuilder, epsilonDecimal);

        final int solution = gammaDecimal * epsilonDecimal;
        logger.info("Part 1 solution:\nWhat is the power consumption of the submarine? = {}", solution);
    }

    private static void partTwo() {

//        "life support rating"  =  "oxygen generator rating"     *    "CO2 scrubber rating".
        // MOST vtpaoc.common, 1s prevail
        ArrayList<String> oxygenLines = new ArrayList<>(lines);
        //initial OxygenMap
        Map<Integer, Map<Integer, Integer>> oxygenMap = new HashMap<>();
        final int bitsCount = lines.get(0).length();
        int bitPosition;
        int currBitValue;
        while (oxygenLines.size() > 1) {
            oxygenMap = generateMapCountPerPosition(oxygenLines);
            for (bitPosition = 0; bitPosition < bitsCount; bitPosition++) {
//                determine the most vtpaoc.common value (0 or 1) in the current bit position,
//                and keep only numbers with that bit in that position.
                int judgingBitValue = returnMostLeastPerPosition(oxygenMap, true, bitPosition);
                // MOST vtpaoc.common, 1s prevail
                List<String> oxysToRemove = new ArrayList<>();
                for (String currentOxy : oxygenLines) {
                    final String currBitValueStr = currentOxy.substring(bitPosition, bitPosition + 1);
                    currBitValue = Integer.parseInt(currBitValueStr);
                    if (currBitValue == judgingBitValue) {
                        // keep entry if current bit value == the MOST
//                        logger.info("bitPos= {}, Oxy= {} : keep!", bitPosition, currentOxy);
                    } else {
                        // remove it:
//                        logger.info("bitPos= {}, Oxy= {} : remove!", bitPosition, currentOxy);
                        oxysToRemove.add(currentOxy);
                    }
                }
                // remove the redundant oxyes from the main list, remain only those that will be judged by the next bit position:
                oxygenLines.removeAll(oxysToRemove);
                //recalculate the oxygen map based on the remaining list:
                oxygenMap = generateMapCountPerPosition(oxygenLines);
            }
        }
        int oxygenRatingFinal = Integer.parseInt(oxygenLines.get(0), 2);
        logger.info("oxygenRatingFinal: {} => {}",oxygenLines.get(0), oxygenRatingFinal);

// LEAST vtpaoc.common 0s prevail
        ArrayList<String> cO2Lines = new ArrayList<>(lines);
        //initial OxygenMap
        Map<Integer, Map<Integer, Integer>> co2Map;
        while (cO2Lines.size() > 1) {
            co2Map = generateMapCountPerPosition(cO2Lines);
            for (bitPosition = 0; bitPosition < bitsCount; bitPosition++) {
//        determine the least vtpaoc.common value (0 or 1) in the current bit position,
//        and keep only numbers with that bit in that position.
                int judgingBitValue = returnMostLeastPerPosition(co2Map, false, bitPosition);
                // LEAST vtpaoc.common 0s prevail
                List<String> co2sToRemove = new ArrayList<>();
                for (String currentCo2 : cO2Lines) {
                    final String currCo2BitValueStr = currentCo2.substring(bitPosition, bitPosition + 1);
                    currBitValue = Integer.parseInt(currCo2BitValueStr);
                    if (currBitValue == judgingBitValue) {
                        // keep entry if current bit value == the MOST
//                        logger.info("bitPos= {}, Co2= {} : keep!", bitPosition, currentCo2);
                    } else {
                        // remove it:
//                        logger.info("bitPos= {}, Co2= {} : remove!", bitPosition, currentCo2);
                        co2sToRemove.add(currentCo2);
                    }
                }
                // remove the redundant co2s from the main list, remain only those that will be judged by the next bit position:
                cO2Lines.removeAll(co2sToRemove);
                // if only 1 row remains, break the FOR:
                if(cO2Lines.size() == 1){
                    break;
                }
                //recalculate the oxygen map based on the remaining list:
                co2Map = generateMapCountPerPosition(cO2Lines);
            }

        }
        int co2RatingFinal = Integer.parseInt(cO2Lines.get(0), 2);
        logger.info("co2RatingFinal: {} => {}",cO2Lines.get(0), co2RatingFinal);

        //        "life support rating"  =  "oxygen generator rating"     *    "CO2 scrubber rating".
        int lifeSupportRating = oxygenRatingFinal * co2RatingFinal;
        logger.info("Part 2 solution:\nWhat is the life support rating of the submarine?= {}", lifeSupportRating);
    }

}
