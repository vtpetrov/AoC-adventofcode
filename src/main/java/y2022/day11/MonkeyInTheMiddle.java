package y2022.day11;

import common.enums.Operator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class MonkeyInTheMiddle {

    private static final Logger logger = LoggerFactory.getLogger(MonkeyInTheMiddle.class.getSimpleName());
//    private static final String INPUT_FILE_NAME = "year_2022/day11_input.txt";
    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static String solution;
    private static final List<Monkey> monkeys = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
    

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
        // read monkeys and their data and store in objects:
        readAndParseMonkeys();
        playRoundsAndCalcInspections(20);

        logger.info("All monkeys: {}", monkeys);
        logger.info("    Part 1 solution:\n XXXXXX= [{}]", "<solution_goes_here>");

    }

    private static void playRoundsAndCalcInspections(final int rounds) {
        //play rounds
        for(int r = 0; r < rounds; r++) {
            // each monkey takes turn once in each round:
            for(int m = 0; m < monkeys.size(); m++) {
                inspectReliefTestThrow(monkeys.get(m));
//                testAndThrow(monkeys.get(m));
            }
        }
    }

    private static void inspectReliefTestThrow(Monkey monkeyToCalc) {
        //for each item calc new value and then relief:
        monkeyToCalc.getItems().forEach(item -> {
            int applyToNew = 0;
            if(monkeyToCalc.getCriteria().valueToApplyToNew == -1) {
                applyToNew = item.worryLevel;
            } else {
                applyToNew = monkeyToCalc.getCriteria().valueToApplyToNew;
            }
            
            if(monkeyToCalc.getCriteria().newOperator == Operator.MULTIPLY) {
                item.worryLevel *= applyToNew;
                item.worryLevel /= 3;
            } else if(monkeyToCalc.getCriteria().newOperator == Operator.PLUS) {
                item.worryLevel += applyToNew;
                item.worryLevel /= 3;
            } else {
                throw new Error("Invalid operator" + monkeyToCalc.getCriteria().newOperator);
            }
        });
        
        // then TEST and throw to another monkey:
        
        
    }

    private static void readAndParseMonkeys() {
        // each monkey span 6 rows
        for(int i = 0; i < inputLines.size(); i += 7){
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
        if(operationMatcher.find()) {
            operator = Operator.fromString(operationMatcher.group(1));
            String applyToNewStr = operationMatcher.group(2);// may be string of digits or 'old'
            applyToNewVal = applyToNewStr.equals("old") ? -1 : Integer.parseInt(applyToNewStr);// for 'old' set to -1
        }

        // line 4 => 'Test: divisible by 23'
        int divisibleBy = Integer.parseInt(testLine.substring("Test: divisible by ".length()));

        // line 5, throw if test TRUE => 'If true: throw to monkey 7'
        int throwToMonkeyTrue = Integer.parseInt(testTrueLine.substring("If true: throw to monkey ".length()));
        
        // line 6, throw if test FALSE => 'If false: throw to monkey 0'
        int throwToMonkeyFalse = Integer.parseInt(testFalseLine.substring("If false: throw to monkey ".length()));

        return Criteria.builder()
                .newOperator(operator)
                .valueToApplyToNew(applyToNewVal)
                .testDivisibleBy(divisibleBy)
                .throwToMonkeyIfTestTrue(throwToMonkeyTrue)
                .throwToMonkeyIfTestFalse(throwToMonkeyFalse)
                .build();
    }

    @NotNull
    private static List<Item> getMonkeyItemsFromLine(@NotNull String itemsLine) {
        return Arrays.stream(itemsLine
                .substring("Starting items: ".length())
                .split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList())
                .stream()
                .map(Item::new).collect(Collectors.toList());
    }

    
    private static void solvePartTwo() {


        logger.info("    Part 2 solution:\n YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
    }


    
    
    @Builder
    @Data
    public static class Monkey {
        List<Item> items = new ArrayList<>();
        Criteria criteria;
        int totalInspections;
    }
    
    @Builder
    @Data
    private static class Criteria {
        Operator newOperator;
        int valueToApplyToNew;;
        int testDivisibleBy;
        
        int throwToMonkeyIfTestTrue;
        int throwToMonkeyIfTestFalse;
    }
    
    @Data
    public static class Item {
        @NonNull int worryLevel;
    }

    
}
