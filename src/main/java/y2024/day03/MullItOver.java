package y2024.day03;

import base.BaseDay;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static helper.InputLoader.closeInput;
import static helper.Misc.prettyPrintList;
import static helper.Misc.prettyPrintNumber;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

@Slf4j
public class MullItOver extends BaseDay {

    static String year = "2024";
    static String day = "03";
    static String puzzleTitle = "Mull It Over";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
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
        AtomicLong sumOfMultiplications = new AtomicLong();
        AtomicInteger counter = new AtomicInteger();

        for (String inputLine : payload) {
            matchingMuls.matcher(inputLine).results().forEach(matchResult -> {
                log.info("{}: matchResult.group(0) = {}", counter.addAndGet(1), matchResult.group(0));
                int firstNumber = parseInt(matchResult.group(1));
                int secondNumber = parseInt(matchResult.group(2));
                sumOfMultiplications.addAndGet((long) firstNumber * secondNumber);
            });
        }
        return sumOfMultiplications.get();
    }

    private static void solvePartTwo() {

        List<String> enabledParts = new ArrayList<>();
        log.info("How many inputLines: '{}'", inputLines.size());

        for (int i = 0, inputLinesSize = inputLines.size(); i < inputLinesSize; i++) {
            String processingLine = inputLines.get(i);
            boolean inEnabledMode = true;
            int p = 1;
            while (!processingLine.isEmpty()) {
                log.debug("----------   l{} , p{}", i, p);
                log.debug("processingLine: {}", processingLine);
                log.debug("inEnabledMode: {}", inEnabledMode);

                if (inEnabledMode) { // if enabled, search from DON'T
                    // start from beginning until the first DONT or until the end of the string if there is no DONT
                    int until = processingLine.contains(DONT) ? processingLine.indexOf(DONT) : processingLine.length();
                    enabledParts.add(format("l%d p%d: %s", i, p, processingLine.substring(0, until)));

                    // REMOVE the processed part:
                    processingLine = processingLine.substring(until);
                    // flip the mode boolean
                    inEnabledMode = false;
                } else { // if disabled, search from DO
                    // start from beginning until the first DO or until the end of the string if there is no DO
                    // mark this part as DISABLED
                    int until = processingLine.contains(DO) ? processingLine.indexOf(DO) : processingLine.length();
                    // REMOVE it:
                    processingLine = processingLine.substring(until);
                    // flip the mode boolean
                    inEnabledMode = true;
                }
                p++;
                log.debug("enabledParts so far: '{}'", prettyPrintList(enabledParts));
            }
            log.debug("enabledParts after line {}: '{}'", i, prettyPrintList(enabledParts));
        }

        log.info("TOTAL enabledParts: '{}'", prettyPrintList(enabledParts));

        solutionP2 = calcSumOfMultiplications(enabledParts);

        log.info("""
                Part 2 solution:
                 Handle the new instructions;
                 What do you get if you add up all of the results of just the enabled multiplications?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }
}
