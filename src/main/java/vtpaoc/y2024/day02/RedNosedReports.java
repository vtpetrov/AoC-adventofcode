package vtpaoc.y2024.day02;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static vtpaoc.helper.InputLoader.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class RedNosedReports extends BaseDay {

    static String year = "2024";
    static String day = "02";
    static String puzzleTitle = "Red-Nosed Reports";
    private static List<List<Integer>> reports;

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
        // inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();
        reports = parseReportsData(inputLines);
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {
        solutionP1 = countSafeReports(reports, false);

        log.info("""
                Part 1 solution:
                 How many reports are safe?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void solvePartTwo() {
        solutionP2 = countSafeReports(reports, true);

        log.info("""
                Part 2 solution:
                 With the Problem Dampener, How many reports are safe?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }

    private static List<List<Integer>> parseReportsData(List<String> inputLines) {
        List<List<Integer>> parsedReports = new ArrayList<>();
        for (String line : inputLines) {
            parsedReports.add(new ArrayList<>(Stream.of(line.split(" ")).map(Integer::parseInt).toList()));
        }
        return parsedReports;
    }

    /**
     * <b>Rules</b>:
     * A report only counts as <b>safe</b> if both of the following are true:
     * <ol>
     *   <li>The levels are either all increasing or all decreasing.</li>
     *   <li>Any two adjacent levels differ by at least one and at most three.</li>
     * </ol>
     *
     * @param reports        our input data
     * @param applyTolerance Apply fault tolerance or not - Part 1: false, Part 2: true
     * @return the count of the safe reports according to the rules
     */
    private static Object countSafeReports(List<List<Integer>> reports, boolean applyTolerance) {
        int safeReports = 0;
        for (List<Integer> originalReport : reports) { // REPORT

            //ORIGINAL report
            if (isReportSafe(originalReport)) {
                safeReports++;
            } else if (applyTolerance) {

                // start removing elements and keep checking either until report becomes safe or until there are no more elements to remove
                for (int i = 0; i < originalReport.size(); i++) {
                    List<Integer> newReport = new ArrayList<>(originalReport);
                    newReport.remove(i);
                    if (isReportSafe(newReport)) {
                        safeReports++;
                        break;
                    }
                }
            }
        }
        return safeReports;
    }

    static boolean isReportSafe(List<Integer> reportToCheck) {
        boolean increasing = reportToCheck.get(0) < reportToCheck.get(1);
        boolean unsafe = false;

        if (increasing) {
            // increasing example: 1 3 6 7 9
            // check if all the rest of the numbers in the report are increasing
            for (int i = 1; i < reportToCheck.size() - 1; i++) {
                if (reportToCheck.get(i) >= reportToCheck.get(i + 1)) {
                    logUnsafe(reportToCheck);
                    return false;
                }
            }
        } else {
            // decreasing example: 7 6 4 2 1
            // check if all the rest of the numbers in the report are decreasing
            for (int i = 1; i < reportToCheck.size() - 1; i++) {
                if (reportToCheck.get(i) <= reportToCheck.get(i + 1)) {
                    logUnsafe(reportToCheck);
                    return false;
                }
            }
        }

        //any two adjacent levels differ by at least one and at most three.
        for (int levelIdx = 0; levelIdx < reportToCheck.size() - 1; levelIdx++) {
            int diff = Math.abs(reportToCheck.get(levelIdx) - reportToCheck.get(levelIdx + 1));
            if (diff < 1 || diff > 3) {
                logUnsafe(reportToCheck);
                return false;
            }
        }
        log.info("Report {} is safe", reportToCheck);
        return true;
    }

    private static void logUnsafe(List<Integer> reportToCheck) {
        log.debug("Report {} is UNsafe", reportToCheck);
    }

}
