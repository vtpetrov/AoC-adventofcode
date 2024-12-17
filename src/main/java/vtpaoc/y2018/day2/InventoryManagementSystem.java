package vtpaoc.y2018.day2;

import java.util.*;

import static vtpaoc.helper.InputUtils.*;

public class InventoryManagementSystem {

    private static final String INPUT_FILE_NAME = "year_2018/day2_input.txt";
    private static ArrayList<String> boxes = new ArrayList<>();
    private static int twos = 0;
    private static int threes = 0;


    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2018    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 2 ===---     ");
        System.out.println("                   - Inventory Management System -     ");

        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        //read and store the input so it will be used for both part 1 and 2:
        while (getMainIn().hasNextLine()) {

            boxes.add(getMainIn().nextLine());

        }
        closeInput();

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

        for (String box : boxes) {
            Map<String, Integer> boxStats = new HashMap<>();

            for (int i = 0; i < box.length(); i++) {
                boxStats.merge(String.valueOf(box.charAt(i)), 1, Integer::sum);
            }

            if (boxStats.containsValue(2)) twos++;
            if (boxStats.containsValue(3)) threes++;

        }

        long checksum = twos * threes;


        System.out.println("\n    Part 1 solution:   the checksum for your list of box IDs= " + checksum);

    }

    private static void partTwo() {

        int boxLength = boxes.get(0).length();
        SortedSet<String> sortedBoxes = new TreeSet<>(boxes);
        boxes = new ArrayList<>(sortedBoxes);

        for (int i = 0; i < boxes.size() - 1; i++) {

            String box1, box2;
            box1 = boxes.get(i);
            box2 = boxes.get(i + 1);

            int diffs = 0;
            int diffPos = -1;

            for (int k = 0; k < boxLength; k++) {
                if (box1.charAt(k) != box2.charAt(k)) {
                    diffs++;
                    diffPos = k;
                }
            }

            if (diffs == 1) {// exactly 1 difference, this is our solution

                String commonLetters = box1.substring(0, diffPos) + box1.substring(diffPos + 1, boxLength);
                System.out.println("\n    Part 2 solution:   letters in vtpaoc.common between the two correct box IDs= " + commonLetters);
                return;

            }

        }

    }

}
