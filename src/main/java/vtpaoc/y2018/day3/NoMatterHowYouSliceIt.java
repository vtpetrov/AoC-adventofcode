package vtpaoc.y2018.day3;

import java.util.ArrayList;
import java.util.Date;

import static vtpaoc.helper.InputLoader.*;

public class NoMatterHowYouSliceIt {

    private static final String INPUT_FILE_NAME = "year_2018/day3_input.txt";
    private static Fabric fabric = new Fabric();
    private static ArrayList<Claim> claims = new ArrayList<>();

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2018    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 3 ===---     ");
        System.out.println("                   - No Matter How You Slice It -     ");

        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            claims.add(new Claim(getMainIn().nextLine()));
        }

        partOne();


        long p2Start = new Date().getTime();
        System.out.println("\nP1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        System.out.println("=========================================================================================");

        partTwo();

        closeInput();


        long end = new Date().getTime();
        System.out.println("\nP2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        System.out.println("==========");
        System.out.println("\nTotal Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        System.out.println("\n:::END = " + end);
    }

    private static void partOne() {


        for (Claim c : claims) {
            fabric.drawClaim(c);
        }

        System.out.println("\n    Part 1 solution:   square inches of fabric within two or more claims= " + fabric.getOverlaps());

    }

    private static void partTwo() {

        System.out.println("\n    Part 2 solution:   the ID of the only claim that doesn't overlap= " + fabric.getNonOverlapingClaims());
    }


}
