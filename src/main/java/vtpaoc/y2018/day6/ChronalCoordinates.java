package vtpaoc.y2018.day6;

import java.util.Date;
import java.util.Map;

import static vtpaoc.helper.InputUtils.*;

public class ChronalCoordinates {

        private static final String INPUT_FILE_NAME = "year_2018/day6_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    private static Map<Integer, Location> allLocations;
    private static Grid myGrid = new Grid();
    private static Map<Integer, Location> finiteLocations;

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2018    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 6 ===---     ");
        System.out.println("                  - Chronal Coordinates -     ");

        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        int i = 1;
        while (getMainIn().hasNextLine()) {
            myGrid.addLocationToGrid(i, getMainIn().nextLine().split(","));
            i++;
        }


        myGrid.markClosestLocation();


        partOne();


        long p2Start = new Date().getTime();
        System.out.println("\nP1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        System.out.println("=========================================================================================");
        System.out.println("\n    ---=== Part 2 ===---     ");

        partTwo();

        closeInput();


        long end = new Date().getTime();
        System.out.println("\nP2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        System.out.println("==========");
        System.out.println("\nTotal Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        System.out.println("\n:::END = " + end);
    }

    private static void partOne() {

//        System.out.println("myGrid = " + myGrid);

        System.out.println("\n    Part 1 solution:   the size of the largest area that isn't infinite= " + myGrid.getLargestFiniteLocation().getArea());

    }

    private static void partTwo() {

        myGrid.calculateRegionArea();

        System.out.println("\n    Part 2 solution:   " +
                "the size of the region containing all locations which have a total distance to all given coordinates of less than 10000= "
        + myGrid.getRegionArea());
    }

}
