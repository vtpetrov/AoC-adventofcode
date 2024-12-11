package vtpaoc.y2017.day6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static vtpaoc.helper.InputLoader.getMainIn;
import static vtpaoc.helper.InputLoader.loadInput;

public class MemoryReallocation {


    private static final String INPUT_FILE_NAME = "year_2017/day6_input.txt";
    private static List<Integer[]> patternHistory = new ArrayList<>();
    private static int firstIndex;
    //    private static final String INPUT_FILE_NAME = "debug.txt";

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);

        System.out.println("    ---=== Day 6 ===---     ");
        System.out.println("\n    ---=== Part 1 ===---     ");
        partOne();
        System.out.println("\n    ---=== Part 2 ===---     ");
        partTwo();

        long end = new Date().getTime();
        System.out.println("\n:::END = " + end);
        System.out.println("Duration: " + (end - start) + " milliseconds");

    }

    /**
     * Given the initial block counts in your puzzle input, how many redistribution cycles must be completed
     * before a configuration is produced that has been seen before?
     * 0   2    7 0
     * the answer in this example is 5.
     */
    private static void partOne() {
        firstIndex = job();

        System.out.println("\nPart 1 solution:  redistribution cycles= " + patternHistory.size());

    }


    private static void partTwo() {
        System.out.println("Part 2 solution:  redistribution cycles= " + (patternHistory.size() - firstIndex));
    }

    private static int job() {
        loadInput(INPUT_FILE_NAME, ", ");

        String s = getMainIn().nextLine();

        List<Integer> banksList = new ArrayList<>();

        Arrays.stream(s.split("\\s *")).forEach(entry -> banksList.add(Integer.valueOf(entry)));

        Integer[] banksArray = banksList.toArray(new Integer[banksList.size()]);
        System.out.println("initial banksArray = " + Arrays.toString(banksArray));

        // put initial state in the history container:
        patternHistory.add(Arrays.copyOf(banksArray, banksArray.length));

        boolean match = false;
        do {
            banksArray = redistribute(banksArray, returnIndexOfMaxElem(banksArray));
//            System.out.println("banksArray = " + Arrays.toString(banksArray));
            for (int i = 0; i < patternHistory.size(); i++) {
                if (Arrays.equals(patternHistory.get(i), banksArray)) {
                    match = true;
                    System.out.println("FINAL banksArray = " + Arrays.toString(banksArray));
                    return i;
                }
            }

            if (!match) patternHistory.add(Arrays.copyOf(banksArray, banksArray.length));

        } while (!match);

        return -1;
    }

    private static Integer[] redistribute(Integer[] array, int indexOfMaxElem) {

        int moves = array[indexOfMaxElem];
        array[indexOfMaxElem] = 0;

        int addTo = indexOfMaxElem;

        for (int i = 0; i < moves; i++) {

            addTo = (addTo + 1) % array.length;
            array[addTo]++;

        }

        return array;
    }

    private static int returnIndexOfMaxElem(Integer[] array) {

        int max = array[0];
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        }

        return index;
    }

}
