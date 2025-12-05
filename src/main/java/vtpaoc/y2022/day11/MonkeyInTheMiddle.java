package vtpaoc.y2022.day11;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vtpaoc.common.enums.Operator;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static vtpaoc.helper.InputUtils.*;

public class MonkeyInTheMiddle {

    private static final Logger logger = LoggerFactory.getLogger(MonkeyInTheMiddle.class.getSimpleName());
//            private static final String INPUT_FILE_NAME = "year_2022/day11_input.txt";
    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static int solution;
    private static final List<Monkey> monkeys = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
    private static int currRound;
    private static int currMonkeyIdx;


    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 11 ===---     ");
        logger.info("               - Monkey in the Middle -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line.trim());
        }

//        solvePartOne();

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

    /**
     * Figure out which monkeys to chase by counting how many items they inspect over 20 rounds.
     * <br/>What is the level of monkey business after 20 rounds of stuff-slinging simian shenanigans?
     */
    private static void solvePartOne() {
        readAndParseMonkeys();
        playRoundsAndCalcInspections(20, true);
        findTopPerformersAndSetSolution();

        logger.info("    Part 1 solution:\n " +
                        "What is the level of monkey business after 20 rounds of stuff-slinging simian shenanigans?= [{}]"
                , solution);

    }

    private static void solvePartTwo() {
        monkeys.clear();
        readAndParseMonkeys();
        playRoundsAndCalcInspections(10000, false);
        findTopPerformersAndSetSolution();

        logger.info("    Part 2 solution:\n " +
                        "What is the level of monkey business after 10000 rounds? (no relief)= [{}]"
                , solution);
    }

    private static void findTopPerformersAndSetSolution() {
        // find top performers -> 2 monkeys with the most inspections:
        List<Integer> inspectionsCount = monkeys.stream().map(Monkey::getTotalInspections).collect(Collectors.toList());
        logger.info("Inspections count at the end: {}", inspectionsCount);
        Integer top = inspectionsCount.stream().max(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE);
        inspectionsCount.remove(top);
        Integer _2ndTop = inspectionsCount.stream().max(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE);

        logger.info("the two most active monkeys inspected items: {}, {}", top, _2ndTop);
        solution = top * _2ndTop;
        logger.info("The level of monkey business in this situation can be found by multiplying these together!");
    }

    private static void playRoundsAndCalcInspections(final int rounds, boolean useRelief) {
        //play rounds
        for (int r = 0; r < rounds; r++) {
            // each monkey takes turn once in each round:
            currRound = r;
            for (int m = 0; m < monkeys.size(); m++) {
                currMonkeyIdx = m;
                inspectReliefTestThrow(monkeys.get(m), useRelief);
            }
            logger.info("After round [{}]: Inspections= {}", r + 1, monkeys.stream().map(Monkey::getTotalInspections).collect(Collectors.toList()));
        }
    }

    private static void inspectReliefTestThrow(final Monkey monkeyToCalc, final boolean useRelief) {
        int testDivisibleBy = monkeyToCalc.getCriteria().getTestDivisibleBy();
        int throwToMonkeyIfTestTrue = monkeyToCalc.getCriteria().getThrowToMonkeyIfTestTrue();
        int throwToMonkeyIfTestFalse = monkeyToCalc.getCriteria().getThrowToMonkeyIfTestFalse();

        //for each item possessed by the monkey:
        while (!monkeyToCalc.getItems().isEmpty()) {
            // remove the 1st item from the monkey's inventory as it will be distributed to another monkey:
            Item currItem = monkeyToCalc.getItems().remove(0);

            BigInteger applyToNew;
            if (monkeyToCalc.getCriteria().valueToApplyToNew == -1) { // use self value:
                applyToNew = currItem.worryLevel;
            } else { // use the number value from criteria:
                applyToNew = BigInteger.valueOf(monkeyToCalc.getCriteria().valueToApplyToNew);
            }

            // 1. INSPECT item and calc new worry level value, THEN relief:
            if (monkeyToCalc.getCriteria().newOperator == Operator.MULTIPLY) {
                currItem.worryLevel = currItem.worryLevel.multiply(applyToNew);
            } else if (monkeyToCalc.getCriteria().newOperator == Operator.PLUS) {
                currItem.worryLevel = currItem.worryLevel.add(applyToNew);
            } else {
                throw new Error("Invalid operator" + monkeyToCalc.getCriteria().newOperator);
            }
            // relief or not based on param (p1= true, p2= false):
            if (useRelief) {
                currItem.worryLevel = currItem.worryLevel.divide(BigInteger.valueOf(3));
            } 
//            else if (currItem.worryLevel > 1_000_000) {
//                // artificially divide by 1M to keep value size manageable for int.
//                currItem.worryLevel = (currItem.worryLevel / 1_000_000) + (currItem.worryLevel % 1_000_000);
//            }
            // track the count of each item inspected by this monkey:
            monkeyToCalc.totalInspections++;

            // 2. then TEST and throw to another monkey:
//            if ((currItem.worryLevel % testDivisibleBy) == 0) { // test is true
            if ((currItem.worryLevel.mod(BigInteger.valueOf(testDivisibleBy))).compareTo(BigInteger.valueOf(0)) == 0) { // test is true
                // throw to monkey TURE -> add item to the given monkey's inventory
                monkeys.get(throwToMonkeyIfTestTrue).getItems().add(currItem);
            } else { // test is false
                // throw to monkey FALSE -> add item to the given monkey's inventory
                monkeys.get(throwToMonkeyIfTestFalse).getItems().add(currItem);
            }
        }
    }

    private static void readAndParseMonkeys() {
        // each monkey span 6 rows
        for (int i = 0; i < inputLines.size(); i += 7) {
            //1st line is monkey index (but they go in a row anyway, so no actually need to use it, just create new)
            Monkey currMonkey = Monkey.builder().build();
            logger.info("Initializing monkey => {}", inputLines.get(i));

            //2nd line, initialize items in monkey inventory:
            String itemsLine = inputLines.get(i + 1);
            currMonkey.setItems(getMonkeyItemsFromLine(itemsLine));

            //lines 3, 4, 5 and 6 initialize new Criteria (with Operator and value to apply, test divisible by and throw to monkey(s)):
            // line 3 => 'Operation: new = old * 19/old'
            String operationLine = inputLines.get(i + 2);

            // line 4 => 'Test: divisible by 23'
            String testLine = inputLines.get(i + 3);

            // line 5, throw if test TRUE => 'If true: throw to monkey 7'
            String testTrueLine = inputLines.get(i + 4);
            // line 6, throw if test FALSE => 'If false: throw to monkey 0'
            String testFalseLine = inputLines.get(i + 5);

            currMonkey.setCriteria(getMonkeyCriteriaFromLines(operationLine, testLine, testTrueLine, testFalseLine));
            logger.info("Add Curr monkey to list: {}", currMonkey);
            monkeys.add(currMonkey);
        }
    }

    private static Criteria getMonkeyCriteriaFromLines(String operationLine, String testLine, String testTrueLine, String testFalseLine) {
        // line 3 => 'Operation: new = old * 19/old'
        Pattern operationPtrn = Pattern.compile("Operation: new = old (\\*|\\+) (\\d+|old)$");
        Matcher operationMatcher = operationPtrn.matcher(operationLine);
        Operator operator = null;
        int applyToNewVal = 0;
        if (operationMatcher.find()) {
            operator = Operator.fromString(operationMatcher.group(1));
            String applyToNewStr = operationMatcher.group(2);// may be string of digits or 'old'
            applyToNewVal = applyToNewStr.equals("old") ? -1 : Integer.parseInt(applyToNewStr);// for 'old' set to -1
        }

        // line 4 => 'Test: divisible by 23'
        int divisibleBy = Integer.parseInt(testLine.substring("Test: divisible by " .length()));

        // line 5, throw if test TRUE => 'If true: throw to monkey 7'
        int throwToMonkeyTrue = Integer.parseInt(testTrueLine.substring("If true: throw to monkey " .length()));

        // line 6, throw if test FALSE => 'If false: throw to monkey 0'
        int throwToMonkeyFalse = Integer.parseInt(testFalseLine.substring("If false: throw to monkey " .length()));

        return Criteria.builder()
                .newOperator(operator)
                .valueToApplyToNew(applyToNewVal)
                .testDivisibleBy(divisibleBy)
                .throwToMonkeyIfTestTrue(throwToMonkeyTrue)
                .throwToMonkeyIfTestFalse(throwToMonkeyFalse)
                .build();
    }

    private static List<Item> getMonkeyItemsFromLine(@NonNull String itemsLine) {
        return Arrays.stream(itemsLine
                        .substring("Starting items: " .length())
                        .split(","))
                .map(String::trim)
                .map(BigInteger::new)
                .map(Item::new).collect(Collectors.toList());
    }

    @Builder
    @Data
    public static class Monkey {
        List<Item> items;
        Criteria criteria;
        int totalInspections;

        @Override
        public String toString() {
            return "Mnky{" +
                    "items=" + items +
                    ", crit=" + criteria +
                    ", inspctns=" + totalInspections +
                    '}';
        }
    }

    @Builder
    @Data
    private static class Criteria {
        Operator newOperator;
        Integer valueToApplyToNew;
        ;
        int testDivisibleBy;

        int throwToMonkeyIfTestTrue;
        int throwToMonkeyIfTestFalse;

        @Override
        public String toString() {
            return "{" +
                    "Op=" + newOperator +
                    ", Nominator=" + valueToApplyToNew +
                    ", tstDivBy=" + testDivisibleBy +
                    ", toMnkyTstTrue=" + throwToMonkeyIfTestTrue +
                    ", toMnkyTstFlse=" + throwToMonkeyIfTestFalse +
                    '}';
        }
    }

    @Data
    @Builder
    public static class Item {
//        int worryLevel;
        BigInteger worryLevel;

        @Override
        public String toString() {
            return "Itm{" +
                    "wrryLvl=" + worryLevel +
                    '}';
        }
    }


}
