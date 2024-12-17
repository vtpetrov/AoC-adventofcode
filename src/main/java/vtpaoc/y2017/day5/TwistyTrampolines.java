package vtpaoc.y2017.day5;

import java.util.Arrays;

import static vtpaoc.helper.InputUtils.*;

public class TwistyTrampolines {

    private static final String INPUT_FILE_NAME = "year_2017/day5_input.txt";
    private static final int BOUNDARY = 3;

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");

        System.out.println("    ---=== Day 5 ===---     ");
        System.out.println("\n    ---=== Part 1 ===---     ");
        partOne();
        System.out.println("\n    ---=== Part 2 ===---     ");
        partTwo();

    }

    private static void partOne() {
        loadInput(INPUT_FILE_NAME, ", ");
        int l = 0;
        int[] inputArray = new int[1070];
        int hops;


        while (getMainIn().hasNextLine()) {
            inputArray[l] = Integer.valueOf(getMainIn().nextLine());
            l++;
        }
        System.out.println("Initial state= " + Arrays.toString(inputArray));

        hops = calculateHops(inputArray, false);

        System.out.println("Final state= " + Arrays.toString(inputArray));

        System.out.println("Part 1 solution:  hops to exit= " + hops);

        closeInput();

    }

    private static void partTwo() {

        loadInput(INPUT_FILE_NAME, ", ");
        int l = 0;
        int[] input = new int[1070];
        int hops;


        while (getMainIn().hasNextLine()) {
            input[l] = Integer.valueOf(getMainIn().nextLine());
            l++;
        }
        System.out.println("2 Initial state= " + Arrays.toString(input));

        hops = calculateHops(input, true);

        System.out.println("2 Final state= " + Arrays.toString(input));

        System.out.println("Part 2 solution:  hops to exit= " + hops);

        closeInput();
    }

    private static int calculateHops(int[] elems, boolean enableDiffOffsetIncrement) {

        boolean found = false;
        int i = 0;
        int offset;
        int result = 0;
        do {
            try {
                offset = elems[i];
            } catch (ArrayIndexOutOfBoundsException e) {
                found = true;
                continue;
            }

            if (enableDiffOffsetIncrement && offset >= BOUNDARY) {
                elems[i]--;
            } else {
                elems[i]++;
            }

            i += offset;
            result++;
        } while (!found);

        return result;
    }

}
