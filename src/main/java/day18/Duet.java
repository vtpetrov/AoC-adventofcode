package day18;

import java.util.Date;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.loadInput;

public class Duet {

    private static final String INPUT_FILE_NAME = "day18_input.txt";

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 18 ===---     ");
        System.out.println("                   - Duet -     ");
        System.out.println("\n          ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        partOne();

        System.out.println("\n  Part 1 solution:   XXXXXX= [" + 1 + "]");


        long p2Start = new Date().getTime();
        System.out.println("\n  P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        System.out.println("=========================================================================================");
        System.out.println("\n          ---=== Part 2 ===---     ");

        partTwo();
        System.out.println("\n    Part 2 solution:   YYYYYYYYYYYY= [" + 2 + "]");

        closeInput();


        long end = new Date().getTime();
        System.out.println("\n  P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        System.out.println("\nTotal Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");
        System.out.println("\n:::END = " + end);
    }

    private static void partOne() {

    }

    private static void partTwo() {

    }

}