package vtpaoc.y2017.day12;

import vtpaoc.y2017.common.Program;
import vtpaoc.y2017.common.Programs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static vtpaoc.helper.InputLoader.getMainIn;
import static vtpaoc.helper.InputLoader.loadInput;

public class DigitalPlumber {

    private static final String INPUT_FILE_NAME = "year_2017/day12_input.txt";
    //    private static final String INPUT_FILE_NAME = "debug.txt";
    private static Programs programs = new Programs();

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("                ---=== Day 12 ===---     ");
        System.out.println("\n    ---=== Part 1 ===---     ");
        partOne();
//
        System.out.println("\n    ---=== Part 2 ===---     ");
        partTwo();

        long end = new Date().getTime();
        System.out.println("\n:::END = " + end);
        System.out.println("Duration: " + (end - start) + " milliseconds");

    }

    /**
     * You walk through the village and record the ID of each program and the IDs with which it can communicate directly (your puzzle input). Each program has one or more programs with which it can communicate, and these pipes are bidirectional; if 8 says it can communicate with 11, then 11 will say it can communicate with 8.
     * <p>
     * You need to figure out how many programs are in the group that contains program ID 0.
     * <p>
     * For example, suppose you go door-to-door like a travelling salesman and record the following list:
     * <p>
     * 0 <-> 2
     * 1 <-> 1
     * 2 <-> 0, 3, 4
     * 3 <-> 2, 4
     * 4 <-> 2, 3, 6
     * 5 <-> 6
     * 6 <-> 4, 5
     */
    private static void partOne() {

        loadInput(INPUT_FILE_NAME, ", ");

        while (getMainIn().hasNextLine()) {
            programs.addProgram(new Program(getMainIn().nextLine()));
        }

        programs.findConnectedToZero();
        List<Integer> result = new ArrayList<>(programs.getProgramGroups().get(0));

        System.out.println("Part 1 solution:");
        System.out.println("    INFO:    number of programs in group '0' = " + result.size());
    }

    private static void partTwo() {

        loadInput(INPUT_FILE_NAME, ", ");

        while (getMainIn().hasNextLine()) {
            programs.addProgram(new Program(getMainIn().nextLine()));
        }

        programs.determineProgramGroups();

        System.out.println("Part 2 solution:    ");
        System.out.println("    INFO:   number of groups= " + programs.getProgramGroups().size());


    }
}
