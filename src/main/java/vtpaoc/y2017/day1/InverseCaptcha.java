package vtpaoc.y2017.day1;

import static vtpaoc.helper.InputLoader.*;

public class InverseCaptcha {

    private static final String INPUT_FILE_NAME = "year_2017/day1_input.txt";

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");

        System.out.println("    ---=== Day 1 ===---     ");
        partOne();
        partTwo();

    }

    /**
     * The captcha requires you to review a sequence of digits (your puzzle input) and find the sum of all digits that match the next digit in the list. The list is circular, so the digit after the last digit is the first digit in the list.
     * <p>
     * For example:
     * <p>
     * 1122 produces a sum of 3 (1 + 2) because the first digit (1) matches the second digit and the third digit (2) matches the fourth digit.
     * 1111 produces 4 because each digit (all 1) matches the next.
     * 1234 produces 0 because no digit matches the next.
     * 91212129 produces 9 because the only digit that matches the next one is the last digit, 9.
     *
     * @throws Throwable
     */
    private static void partOne() throws Throwable {

        loadInput(INPUT_FILE_NAME, ", ");

        String input = getMainIn().nextLine();
        int MAX_INDEX = input.length() - 1;
        long sum = 0;


        for (int i = 0; i < MAX_INDEX - 1; i++) {
            if ((int) input.charAt(i) == (int) input.charAt(i + 1)) {
                sum += Character.getNumericValue(input.charAt(i));
            }
        }

        if ((int) input.charAt(MAX_INDEX) == (int) input.charAt(0)) { // last digit, first digit:
            sum += Character.getNumericValue(input.charAt(MAX_INDEX));
        }

        System.out.println("Part 1 solution:  sum = " + sum);

        closeInput();

    }

    /**
     * Now, instead of considering the next digit, it wants you to consider the digit halfway around the circular list. That is, if your list contains 10 items, only include a digit in your sum if the digit 10/2 = 5 steps forward matches it. Fortunately, your list has an even number of elements.
     * <p>
     * For example:
     * <p>
     * 1212 produces 6: the list contains 4 items, and all four digits match the digit 2 items ahead.
     * 1221 produces 0, because every comparison is between a 1 and a 2.
     * 123425 produces 4, because both 2s match each other, but no other digit has a match.
     * 123123 produces 12.
     *
     * @throws Throwable
     */
    private static void partTwo() throws Throwable {

        loadInput(INPUT_FILE_NAME, ", ");

        String input = getMainIn().nextLine();
        int MAX_INDEX = input.length() - 1;
        long sum = 0;
        int step = input.length() / 2;


        for (int i = 0; i <= MAX_INDEX; i++) {

            if (i + step <= MAX_INDEX) {
                if (Character.getNumericValue(input.charAt(i)) == Character.getNumericValue(input.charAt(i + step))) {
                    sum += Character.getNumericValue(input.charAt(i));
                }
            } else {
                int reminder = step - (MAX_INDEX - i);

                if (Character.getNumericValue(input.charAt(i)) == Character.getNumericValue(input.charAt(reminder - 1))) {
                    sum += Character.getNumericValue(input.charAt(i));
                }
            }

        }


        System.out.println("Part 2 solution:  sum = " + sum);

        closeInput();

    }

}
