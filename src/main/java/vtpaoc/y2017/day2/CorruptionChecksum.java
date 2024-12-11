package vtpaoc.y2017.day2;

import static vtpaoc.helper.InputLoader.*;

public class CorruptionChecksum {

    private static final String INPUT_FILE_NAME = "year_2017/day2_input.txt";

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");

        System.out.println("    ---=== Day 2 ===---     ");
        partOne();
        partTwo();

    }

    /**
     * As you walk through the door, a glowing humanoid shape yells in your direction. "You there! Your state appears to be idle. Come help us repair the corruption in this spreadsheet - if we take another millisecond, we'll have to display an hourglass cursor!"
     * <p>
     * The spreadsheet consists of rows of apparently-random numbers. To make sure the recovery process is on the right track, they need you to calculate the spreadsheet's checksum. For each row, determine the difference between the largest expectedValue and the smallest expectedValue; the checksum is the sum of all of these differences.
     * <p>
     * For example, given the following spreadsheet:
     * <p>
     * 5 1 9 5
     * 7 5 3
     * 2 4 6 8
     * The first row's largest and smallest values are 9 and 1, and their difference is 8.
     * The second row's largest and smallest values are 7 and 3, and their difference is 4.
     * The third row's difference is 6.
     * In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.
     */
    private static void partOne() {

        long sum = 0;

        loadInput(INPUT_FILE_NAME, ", ");

        while (getMainIn().hasNextLine()) {
            String row = getMainIn().nextLine();

            String[] red = (row.split("\\s"));

            int min = Integer.valueOf(red[0]), max = Integer.valueOf(red[0]);

            for (int i = 1; i < red.length; i++) {

                if (Integer.valueOf(red[i]) < min) {
                    min = Integer.valueOf(red[i]);
                }

                if (Integer.valueOf(red[i]) > max) {
                    max = Integer.valueOf(red[i]);
                }

            }

            sum += max - min;

        }

        System.out.println("\nPart 1 solution:  checksum= " + sum);

        closeInput();

    }


    /**
     * -- Part Two ---
     * <p>
     * "Great work; looks like we're on the right track after all. Here's a star for your effort." However, the program seems a little worried. Can programs be worried?
     * <p>
     * "Based on what we're seeing, it looks like all the User wanted is some information about the evenly divisible values in the spreadsheet. Unfortunately, none of us are equipped for that kind of calculation - most of us specialize in bitwise operations."
     * <p>
     * It sounds like the goal is to find the only two numbers in each row where one evenly divides the other - that is, where the result of the division operation is a whole number. They would like you to find those numbers on each line, divide them, and add up each line's result.
     * <p>
     * For example, given the following spreadsheet:
     * <p>
     * 5 9 2 8
     * 9 4 7 3
     * 3 8 6 5
     * In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4.
     * In the second row, the two numbers are 9 and 3; the result is 3.
     * In the third row, the result is 2.
     * In this example, the sum of the results would be 4 + 3 + 2 = 9.
     */
    private static void partTwo() {

        long sum = 0;

        loadInput(INPUT_FILE_NAME, ", ");
        int redNomer = 0;
        while (getMainIn().hasNextLine()) {
            redNomer++;

            String row = getMainIn().nextLine();
//            System.out.println("\n-- red " + redNomer + ": " + row);

            String[] red = (row.split("\\s"));

            int min = Integer.valueOf(red[0]), max = Integer.valueOf(red[0]);
            boolean found = false;
            search:
            for (int i = 0; i < red.length - 1; i++) {
//                System.out.print("i = " + i + ":");
                for (int j = i + 1; j < red.length; j++) {
//                    System.out.print(" " + j);
                    int big, small;
                    big = Math.max(Integer.valueOf(red[i]), Integer.valueOf(red[j]));
                    small = Math.min(Integer.valueOf(red[i]), Integer.valueOf(red[j]));
                    if (big % small == 0) {
                        //evenly divisible
                        sum += big / small;
//                        System.out.println("\nsum = " + sum);
                        break search;
                    }
                }
//                System.out.println();

            }

        }

        System.out.println("\nPart 2 solution:  checksum= " + sum);

        closeInput();
    }
}
