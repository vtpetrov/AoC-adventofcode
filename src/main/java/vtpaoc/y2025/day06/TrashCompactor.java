package vtpaoc.y2025.day06;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;
import vtpaoc.common.enums.Operator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static vtpaoc.common.enums.Operator.*;
import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class TrashCompactor extends BaseDay {

    static String year = "2025";
    static String day = "06";
    static String puzzleTitle = "Trash Compactor";
    static List<List<String>> problems;
    static List<List<String>> problemsP2;

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

        problems = parseInput();
        solutionP1 = calculate(problems);
        log.info("""
                Part 1 solution:
                 What is the grand total found by adding together all of the answers to the individual problems?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1));
    }

    private static BigInteger calculate(List<List<String>> problemsInput) {
        BigInteger totalResult = BigInteger.ZERO;
        BigInteger setResult = BigInteger.ZERO;

        //there are X sets of data:
        log.debug("problemsInput.size= {}", problemsInput.getFirst().size());

        for (int setIdx = 0; setIdx < problemsInput.getLast().size(); setIdx++) {
            Operator setOperator = fromString(problemsInput.getLast().get(setIdx));
            BigInteger[] members = new BigInteger[problemsInput.size() - 1];
            for (int memberIdx = 0; memberIdx < problemsInput.size() - 1; memberIdx++) {
                members[memberIdx] = new BigInteger(problemsInput.get(memberIdx).get(setIdx));
            }

            if (setOperator == PLUS) {
                setResult = Arrays.stream(members).reduce(BigInteger.ZERO, BigInteger::add);
            } else if (setOperator == MULTIPLY) {
                setResult = Arrays.stream(members).reduce(BigInteger.ONE, BigInteger::multiply);
            } else {
                throw new RuntimeException("Unknown operator: " + setOperator);
            }
            log.debug("------------------------------------------------------------");
            log.debug("members = {}", Arrays.toString(members));
            log.debug("setResult = {}", setResult);
            totalResult = totalResult.add(setResult);
        }

        log.debug("totalResult = {}", totalResult);
        return totalResult;
    }

    private static List<List<String>> parseInput() {
        List<List<String>> problems = new ArrayList<>();
        for (int idx = 0; idx < inputLines.size(); idx++) {
            List<String> lineItems = Arrays.asList(inputLines.get(idx).trim().split("(\\s+)"));
            problems.add(lineItems);
        }
        log.debug("problems = {}", problems);
        return problems;
    }

    private static void solvePartTwo() {

        problemsP2 = parseInputP2();
        solutionP2 = calculateP2(problemsP2);

        log.info("""
                Part 2 solution:
                 What is the grand total found by adding together all of the answers to the individual problems v2?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2));

    }

    private static BigInteger calculateP2(List<List<String>> sets) {
        BigInteger totalResult = BigInteger.ZERO;
        //there are X sets of data:
        log.debug("sets.size= {}", sets.getFirst().size());

        for (List<String> set : sets) {
            BigInteger setResult;
            Operator setOperator = fromString(set.getLast().trim());
            if (setOperator == PLUS) {
                setResult = BigInteger.ZERO;
            } else if (setOperator == MULTIPLY) {
                setResult = BigInteger.ONE;
            } else {
                throw new RuntimeException("Unknown operator: " + setOperator);
            }
            for (int memIdx = 0; memIdx < set.size() - 1; memIdx++) {
                if (setOperator == PLUS) {
                    setResult = setResult.add(new BigInteger(set.get(memIdx).trim()));
                } else {
                    setResult = setResult.multiply(new BigInteger(set.get(memIdx).trim()));
                }
            }

            log.debug("------------------------------------------------------------");
            log.debug("members = {}", set);
            log.debug("setResult = {}", setResult);

            totalResult = totalResult.add(setResult);
        }

        log.debug("totalResult = {}", totalResult);
        return totalResult;
    }

    private static List<List<String>> parseInputP2() {
        // convert input to problemsP2 /transpose/
        //123 328  51 64
        // 45 64  387 23
        //  6 98  215 314
        //*   +   *   +
        // =>
        // problems: [[356, 8, 175, 4], [24, 248, 581, 431], [1, 369, 32, 623], [*, +, *, +]

        // [1, 2, 3,  , 3, 2, 8,  ,  , 5, 1,  , 6, 4,  ]
        // [ , 4, 5,  , 6, 4,  ,  , 3, 8, 7,  , 2, 3,  ]
        // [ ,  , 6,  , 9, 8,  ,  , 2, 1, 5,  , 3, 1, 4]
        // operator is always in the 1st column [0]


        List<String[]> problemsP2wip = new ArrayList<>();
        for (int idx = 0; idx < inputLines.size(); idx++) {
            String[] lineSymbols = inputLines.get(idx).split("");
            problemsP2wip.add(lineSymbols);
            log.debug("lineSymbols = {}", Arrays.toString(lineSymbols));
        }
        int membersCount = problemsP2wip.size();
        int columnsCount = problemsP2wip.getFirst().length;
        List<List<String>> sets = new ArrayList<>();

        // get a set members column by column, when whole column is "", finish this set and start next. operator is last elem in 1st column [0]
        List<String> setMembers = new ArrayList<>();
        String setOperation = ".";
//        AtomicInteger setCounter = new AtomicInteger(0);
        for (int colIdx = 0; colIdx < columnsCount; colIdx++) {
            log.debug("----------------------------------");
            String member = "";
            for (int rowIdx = 0; rowIdx < membersCount; rowIdx++) {
                String memSymbol = problemsP2wip.get(rowIdx)[colIdx];
//                log.debug("symbol[{}][{}]= {}", colIdx, rowIdx, memSymbol);
                if (memSymbol.equals("+") || memSymbol.equals("*")) {
                    setOperation = memSymbol;
                } else {
                    member = member.concat(memSymbol);
                }
            }

            boolean isLastCol = colIdx == columnsCount - 1;

            if (!member.isBlank()) {
                setMembers.add(member);
            }

            if (member.isBlank() || isLastCol) {
                // cut current set and start new set
                setMembers.add(setOperation);
                sets.add(setMembers);
                setMembers = new ArrayList<>();
                setOperation = ".";
            }

            log.debug("set= {}, member= {} set operation= {}", setMembers, member, setOperation);
        }

        log.debug("sets    = {}", sets);

        return sets;
    }
}
