package y2023.day1;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static helper.InputLoader.*;

public class Trebuchet {

    private static final Logger logger = LoggerFactory.getLogger(Trebuchet.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2023/day01_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2023    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 01 ===---     ");
        logger.info("                  - Trebuchet -     ");

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

    private static void solvePartOne() {

        long sumOfCalibrationValues1 = 0;

        for (String line : inputLines) {
            logger.debug("line= {}", line);

            int lineCalibrationValue = findAndConcatFirstAndLastDigit(line);
            sumOfCalibrationValues1 += lineCalibrationValue;
            logger.debug("Line num= {}, Intermediate sum= {}", lineCalibrationValue, sumOfCalibrationValues1);
        }

        logger.info("    Part 1 solution:\n Consider your entire calibration document. What is the sum of all of the calibration values?= [{}]",
                sumOfCalibrationValues1);
    }

    /**
     * Digits only
     */
    private static int findAndConcatFirstAndLastDigit(String line) {
        String[] digits = StringUtils.getDigits(line).split("");
        return Integer.parseInt(digits[0] + digits[digits.length - 1]);
    }

    final static List<String> numberWords = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

    static int lineMostLeftDigit, lineMostLeftDigitIdx, lineMostLeftWord, lineMostLeftWordIdx, lineMostRightDigit, lineMostRightDigitIdx, lineMostRightWord, lineMostRightWordIdx;


    private static void solvePartTwo() {

        long sumOfCalibrationValues2 = 0;

        for (String line : inputLines) {
            logger.info("-------------------------------------------------------");
            logger.info("line= {}", line);
            int lineCalibrationValue;

            if (line.length() < 3) { // if whole line is less than 3 chars, it cannot contain word number, so use digits
                lineCalibrationValue = findAndConcatFirstAndLastDigit(line);
                logger.info("short line, use digits only");
            } else {
                String firstNumber = getFirstNumber(line);
                String lastNumber = getLastNumber(line);
                String concatenation = firstNumber.concat(lastNumber);
                lineCalibrationValue = Integer.parseInt(concatenation);
            }

            sumOfCalibrationValues2 += lineCalibrationValue;

            logger.info("Line value= {}, Intermediate sum= {}", lineCalibrationValue, sumOfCalibrationValues2);
        }

        logger.info("""
                 Part 2 solution:
                 What is the sum of all of the calibration values?

                = [{}]""", sumOfCalibrationValues2);
    }

    private static String getFirstNumber(String line) {
        calcFirstDigitData(line);
        // decide if pick digit or word (which is more to the left. i.e. has smaller index):
        int firstNumber = lineMostLeftDigitIdx < lineMostLeftWordIdx ? lineMostLeftDigit : lineMostLeftWord;
        return String.valueOf(firstNumber);
    }

    private static String getLastNumber(String line) {
        calcLastDigitData(line);
        // decide if pick digit or word (which is more to the right. i.e. has bigger index):
        int lastNumber = lineMostRightDigitIdx > lineMostRightWordIdx ? lineMostRightDigit : lineMostRightWord;
        return String.valueOf(lastNumber);
    }

    private static void calcFirstDigitData(String line) {
        // digit:
        lineMostLeftDigit = Integer.parseInt(StringUtils.getDigits(line).substring(0, 1));
        // need to pass the ascii code of the digit (0:48, 9:57, so add 48)
        lineMostLeftDigitIdx = line.indexOf(lineMostLeftDigit + 48);

        // word:
        int minIndex = 10000; // remain unchanged if word not found
        int minIndexNumber = -1;

        for (int i = 0; i < numberWords.size(); i++) {
            String word = numberWords.get(i);

            if (line.contains(word)) {
                int currWordIndex = line.indexOf(word);
                if (currWordIndex < minIndex) {
                    minIndex = currWordIndex;
                    minIndexNumber = i + 1;
                }
            }
        }

        lineMostLeftWord = minIndexNumber;
        lineMostLeftWordIdx = minIndex;
    }

    private static void calcLastDigitData(String line) {
        // digit:
        String lineDigits = StringUtils.getDigits(line);
        lineMostRightDigit = Integer.parseInt(lineDigits.substring(lineDigits.length() - 1));
        // need to pass the ascii code of the digit (0:48, 9:57, so add 48)
        lineMostRightDigitIdx = line.lastIndexOf(lineMostRightDigit + 48);

        // word:
        int maxIndex = -2; // remain unchanged if word not found
        int maxIndexNumber = -1;

        for (int i = 0; i < numberWords.size(); i++) {
            String word = numberWords.get(i);

            if (line.contains(word)) {
                int currWordIndex = line.lastIndexOf(word);
                if (currWordIndex > maxIndex) {
                    maxIndex = currWordIndex;
                    maxIndexNumber = i + 1;
                }
            }
        }

        lineMostRightWord = maxIndexNumber;
        lineMostRightWordIdx = maxIndex;
    }

}
