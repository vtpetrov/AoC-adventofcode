package y2024.day03;

import base.BaseDay;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static helper.InputLoader.closeInput;
import static helper.Misc.prettyPrintList;
import static helper.Misc.prettyPrintNumber;
import static java.lang.Integer.parseInt;

@Slf4j
public class MullItOver_Ver2 extends BaseDay {

    static String year = "2024";
    static String day = "03";
    static String puzzleTitle = "Mull It Over";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
//        inputFileName = "debug2.txt";
    }

    static Pattern matchingMuls = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    static final String DO = "do()";
    static final String DONT = "don't()";


    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {
        solutionP1 = calcSumOfMultiplications(inputLines);
        log.info("""
                Part 1 solution:
                 Scan the corrupted memory for uncorrupted `mul` instructions.
                 What do you get if you add up all of the results of the multiplications?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static long calcSumOfMultiplications(List<String> payload) {
        long mySum = 0;

        for (String line : payload) {
            Matcher myMatcher = matchingMuls.matcher(line);
            int multiplication;
            while(myMatcher.find()) {
                int multiplicand = parseInt(myMatcher.group(1));
                int multiplier = parseInt(myMatcher.group(2));
                multiplication = multiplicand * multiplier;
                mySum += multiplication;
            }
        }
        return mySum;
    }

    private static void solvePartTwo() {

        List<String> newLines = new ArrayList<>();
        log.info("How many inputLines: '{}'", inputLines.size());

        for (int i = 0, inputLinesSize = inputLines.size(); i < inputLinesSize; i++) {
            String processingLine = inputLines.get(i);
            String newLine;

            log.debug("processingLine: {}", processingLine);

            // REMOVE anything between DON'Ts and DOs
            String betweenDontsAndDosRegExStr = "don't\\(\\)(.*?)do\\(\\)";
            String intermediateLine = processingLine.replaceAll(betweenDontsAndDosRegExStr, "");

            // THEN remove anything after the last DON'T
            String lastDontRegExStr = "don't\\(\\)(.*?)$";
            newLine = intermediateLine.replaceAll(lastDontRegExStr, "");

            newLines.add(newLine);
        }

        log.info("TOTAL newLines: '{}'", prettyPrintList(newLines));

        solutionP2 = calcSumOfMultiplications(newLines);

        log.info("""
                Part 2 solution:
                 Handle the new instructions;
                 What do you get if you add up all of the results of just the enabled multiplications?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }
}
