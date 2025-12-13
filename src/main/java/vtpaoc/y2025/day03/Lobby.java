package vtpaoc.y2025.day03;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;

import java.math.BigInteger;
import java.util.List;

import static java.util.Arrays.stream;
import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class Lobby extends BaseDay {

    static String year = "2025";
    static String day = "03";
    static String puzzleTitle = "Lobby";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInputLines();
        //or
        //loadDayInputWhole();
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {

        solutionP1 = calcJoltage(inputLines, 2);

        log.info("""
                Part 1 solution:
                 for 2 batteries, what is the total output joltage?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1));
    }

    private static void solvePartTwo() {

        solutionP2 = solutionP1 = calcJoltage(inputLines, 12);

        log.info("""
                Part 2 solution:
                 for 12 batteries, What is the new total output joltage?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2));

    }

    static BigInteger calcJoltage(List<String> partInput, int base) {
        BigInteger totalJoltage = BigInteger.ZERO;
        for (String bank : partInput) {
            log.debug("-------- {} --------", bank);
            BigInteger bankJoltage;
            StringBuilder digitsSb = new StringBuilder();
            List<Integer> batteriesList = stream(bank.split("")).mapToInt(Integer::parseInt).boxed().toList();
            // use collections, find MAX in each. that would be our first and second digit.
            // first collection is bank without the last battery-1 length
            int maxDigitForThatPosition = -1;
            List<Integer> currentNthColl;
            for (int n = 0, startIdx = 0, endIdx = batteriesList.size() - base + 1; n < base; n++) {
                // construct N collections and find MAX in each one.
                currentNthColl = batteriesList.subList(startIdx, endIdx);
                maxDigitForThatPosition = currentNthColl.stream().max(Integer::compareTo).orElse(0);
                digitsSb.append(maxDigitForThatPosition);
                int maxDigitIndex = batteriesList.indexOf(maxDigitForThatPosition) <= startIdx
                        ? currentNthColl.indexOf(maxDigitForThatPosition) + startIdx
                        : batteriesList.indexOf(maxDigitForThatPosition);
                startIdx = maxDigitIndex + 1;
                endIdx = Math.min(endIdx + 1, batteriesList.size());
            }

            // second collection is the remaing of the bank to the right of 1st digit

            bankJoltage = new BigInteger(digitsSb.toString());
            log.debug("digits = {} => {}", digitsSb, bankJoltage);

            totalJoltage = totalJoltage.add(bankJoltage);
        }

        return totalJoltage;
    }
}
