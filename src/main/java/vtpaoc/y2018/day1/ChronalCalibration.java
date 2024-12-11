package vtpaoc.y2018.day1;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static vtpaoc.helper.InputLoader.*;

public class ChronalCalibration {

    private static final String INPUT_FILE_NAME = "year_2018/day1_input.txt";
    private static ArrayList<Integer> moves = new ArrayList<>();

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2018    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 1 ===---     ");
        System.out.println("                   - Chronal Calibration -     ");

        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");


        //read and store the input so it will be used for both part 1 and 2:
        while (getMainIn().hasNextLine()){

            moves.add(Integer.parseInt(getMainIn().nextLine()));

        }
        closeInput();


        partOne();


        long p2Start = new Date().getTime();
        System.out.println("\nP1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        System.out.println("=========================================================================================");

        partTwo();


        long end = new Date().getTime();
        System.out.println("\nP2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        System.out.println("==========");
        System.out.println("\nTotal Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        System.out.println("\n:::END = " + end);
    }

    private static void partOne() {

        long freq = 0;

        for(Integer move : moves){
            freq += move;
        }

        System.out.println("\n    Part 1 solution:   resulting frequency= " + freq);

    }

    private static void partTwo() {

        long freq = 0;
        Set<Long> previousFreqs = new HashSet<>();
        int loop = 1;

        while (true) {
            System.out.println("loop = " + loop);
            for(Integer move : moves){
                freq += move;

                if(!previousFreqs.add(freq)){
                    System.out.println("\n    Part 2 solution:   the first frequency reached twice is = " + freq);
                    return;
                }
            }
            loop++;
        }


    }

}
