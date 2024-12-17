package vtpaoc.y2017.day8;

import lombok.Data;
import lombok.ToString;
import vtpaoc.common.enums.Operator;

import java.util.*;

import static vtpaoc.helper.InputUtils.*;

public class ILikeRegisters {

    private static final String INPUT_FILE_NAME = "year_2017/day8_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    private static Set<String> registerNames = new TreeSet<>();
    private static List<Instruction> instructions = new ArrayList<>();
    private static Map<String, Register> registers = new TreeMap<>();
    private static long MAX_VALUE;

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 8 ===---     ");
        System.out.println("\n    ---=== Part 1 and 2 ===---     ");
        partOneAndTwo();

        long end = new Date().getTime();
        System.out.println("\n:::END = " + end);
        System.out.println("\nDuration: " + (end - start) + " milliseconds");

    }

    /**
     * Each instruction consists of several parts: the register to modify, whether to increase or decrease that register's expectedValue, the amount by which to increase or decrease it, and a condition. If the condition fails, skip the instruction without modifying the register. The registers all start at 0. The instructions look like this:
     * <p>
     * b inc 5 if a > 1
     * a inc 1 if b < 5
     * c dec -10 if a >= 1
     * c inc -20 if c == 10
     * These instructions would be processed as follows:
     * <p>
     * Because a starts at 0, it is not greater than 1, and so b is not modified.
     * a is increased by 1 (to 1) because b is less than 5 (it is 0).
     * c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
     * c is increased by -20 (to -10) because c is equal to 10.
     * After this process, the largest expectedValue in any register is 1.
     * <p>
     * You might also encounter <= (less than or equal to) or != (not equal to). However, the CPU doesn't have the bandwidth to tell you what all the registers are named, and leaves that to you to determine.
     * <p>
     * What is the largest expectedValue in any register after completing the instructions in your puzzle input?
     *
     *
     * --- Part Two ---
     To be safe, the CPU also needs to know the highest value held in any register during this process so that it can decide how much memory to allocate to these operations. For example, in the above instructions, the highest value ever held was 10 (in register c after the third instruction was evaluated).
     *
     */
    private static void partOneAndTwo() {

        loadInput(INPUT_FILE_NAME, ", ");

        while (getMainIn().hasNextLine()) {
            String nextLine = getMainIn().nextLine();

            registerNames.add(nextLine.substring(0, nextLine.indexOf(" ")));
            instructions.add(new Instruction(nextLine));

        }

        // initialize Registers states - add name and initial expectedValue '0':
        for (String name : registerNames) {
            registers.put(name, new Register(name));
        }

        System.out.println("        ----    BEFORE  -   -   -   -   -");
        System.out.println("registers = [" + registers.size() + "], " + registers);
        System.out.println("instructions = [" + instructions.size() + "], " + instructions);

        for (Instruction instruction : instructions) {
            instruction.apply();
        }

        System.out.println("    ----    AFTER   -  -    -       -   -   -   -");
        System.out.println("registers = " + registers);

        findLargestValueAmongRegisters("now");
        System.out.println("\nPart 1 solution:");
        System.out.println("    MAX_VALUE now = " + MAX_VALUE);


        findLargestValueAmongRegisters("ever");
        System.out.println("\nPart 2 solution:");
        System.out.println("    MAX_VALUE ever= " + MAX_VALUE);

        closeInput();
    }

    /**
     * @param when "now" OR "ever" are the valid options.
     */
    private static void findLargestValueAmongRegisters(String when) {

        MAX_VALUE = Long.MIN_VALUE;

        for (String register : registerNames) {
            long currentValue = 0;
            if (when.equals("now")) {
                currentValue = registers.get(register).getValue();
            } else if (when.equals("ever")) {
                currentValue = registers.get(register).getHighestValueEver();
            }

            if (currentValue > MAX_VALUE) {
                MAX_VALUE = currentValue;
            }
        }
    }


    public enum Operation {

        INCREASE,
        DECREASE;

        static Operation fromString(String input) {
            switch (input) {
                case "inc":
                    return INCREASE;
                case "dec":
                    return DECREASE;
                default:
                    throw new Error("VTP - unknown operation:" + input);
            }
        }
    }

    @Data
    private static class Condition {

        String dependsOnRegister;
        Operator operator;
        long expectedValue;

        Condition(String dependsOnRegister, String operator, String expectedValue) {
            this.dependsOnRegister = dependsOnRegister;
            this.operator = Operator.fromString(operator);
            this.expectedValue = Integer.valueOf(expectedValue);
        }

        public boolean check() {
            long value = registers.get(this.dependsOnRegister).getValue();

            switch (this.operator) {

                case GT:
                    return (value > this.expectedValue);
                case LT:
                    return (value < this.expectedValue);
                case GTOET:
                    return (value >= this.expectedValue);
                case LTOET:
                    return (value <= this.expectedValue);
                case EQUAL_TO:
                    return (value == this.expectedValue);
                case NOT_EQUAL_TO:
                    return (value != this.expectedValue);
                default:
                    return false;
            }
        }
    }

    @Data
    @ToString(includeFieldNames = false)
    private static class Instruction {

        String affectsRegister;
        Operation operation;
        Integer amount;
        Condition condition;

        Instruction(String input) {

            String[] tokens = input.split(" ");

            this.affectsRegister = tokens[0];
            this.operation = Operation.fromString(tokens[1]);
            this.amount = Integer.valueOf(tokens[2]);
            this.condition = new Condition(tokens[4], tokens[5], tokens[6]);
        }


        boolean apply() {

            if (this.condition.check()) {
                switch (operation) {

                    case INCREASE:
                        registers.get(this.affectsRegister).value += this.amount;
                        break;
                    case DECREASE:
                        registers.get(this.affectsRegister).value -= this.amount;
                        break;
                }
            } else {
                return false;
            }

            if (registers.get(this.affectsRegister).getValue() > registers.get(this.affectsRegister).getHighestValueEver()) {
                registers.get(this.affectsRegister).highestValueEver = registers.get(this.affectsRegister).getValue();
            }

            return true;
        }
    }

    @Data
    @ToString(includeFieldNames = false)
    private static class Register {

        private String name;
        private long value;
        private long highestValueEver;

        Register(String nameParam) {
            this.name = nameParam;
        }
    }
}
