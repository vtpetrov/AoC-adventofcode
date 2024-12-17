package vtpaoc.y2017.day7;

import vtpaoc.y2017.common.Program;
import vtpaoc.y2017.common.Programs;

import java.util.Date;
import java.util.stream.Collectors;

import static vtpaoc.helper.InputUtils.*;

public class RecursiveCircus {

    private static final String INPUT_FILE_NAME = "year_2017/day7_input.txt";
    //    private static final String INPUT_FILE_NAME = "debug.txt";
    private static Programs programs = new Programs();

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("    ---=== Day 7 ===---     ");
//        System.out.println("\n    ---=== Part 1 ===---     ");
//        partOne();
        System.out.println("\n    ---=== Part 2 ===---     ");
        partTwo();
        long end = new Date().getTime();
        System.out.println("\n:::END = " + end);
        System.out.println("Duration: " + (end - start) + " milliseconds");

    }

    /**
     * What is the name of the bottom program
     */
    private static void partOne() {
        loadInput(INPUT_FILE_NAME, ", ");

        while (getMainIn().hasNextLine()) {
            programs.addProgram(new Program(getMainIn().nextLine()));
        }

        Programs holdingPrograms = new Programs(programs.getTowersHolding(true));
        Programs notHoldedPrograms = new Programs(holdingPrograms.getTowersBeingHold(false));

        if (notHoldedPrograms.getPrograms().size() != 1) {
            throw new Error("Invalid input: more than one bottom program!:\n" + notHoldedPrograms);
        } else {
            System.out.println("\nPart 1 solution:  bottom program name= " + notHoldedPrograms.getPrograms().stream().map
                    (Program::getName).collect(Collectors.toList()));
        }

        closeInput();

    }

    /**
     * Given that exactly one program is the wrong weight, what would its weight need to be to balance the entire tower?
     */
    private static void partTwo() {

        loadInput(INPUT_FILE_NAME, ", ");

        while (getMainIn().hasNextLine()) {
            programs.addProgram(new Program(getMainIn().nextLine()));
        }

        programs.populateHoldedTowers();

        Programs holdingPrograms = new Programs(programs.getTowersHolding(true));

        for (Program t : holdingPrograms.getPrograms()) {
            Programs.calculateStackWeight(t);
        }

        holdingPrograms.findInbalancedDisks();

        System.out.println("Programs.imbalancedHolderDay7 = " + Programs.imbalancedHolderDay7.getName());
        System.out.println("\nPrograms.imbalancedProgramsDay7 = " + Programs.imbalancedProgramsDay7.stream().map(Program::getName).collect
                (Collectors.toList()));

        Programs.calcBalancingValue();

    }
}
