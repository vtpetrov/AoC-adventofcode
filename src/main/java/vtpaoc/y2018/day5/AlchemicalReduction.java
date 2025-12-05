package vtpaoc.y2018.day5;

import java.util.Date;

import static vtpaoc.helper.InputUtils.*;

public class AlchemicalReduction {
    private static final String INPUT_FILE_NAME = "year_2018/day5_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";


    private static String originalPolymer;
    private static String resultingPolymer;

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2018    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 5 ===---     ");
        System.out.println("               - Alchemical Reduction -     ");

        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        originalPolymer = getMainIn().nextLine();
        resultingPolymer = originalPolymer;

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

        removeAllReactions(originalPolymer);

        System.out.println("\n    Part 1 solution:   units remain after fully reacting the polymer = " + resultingPolymer.length());

    }

    private static void partTwo() {

        int minLength = originalPolymer.length();

        String[] alphabet =
                new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

        // cycle through each character (lower + upper), remove it and calculate the reaction.
        // Notice length after each
        for(String ch : alphabet){
            String reducedInput = originalPolymer.replaceAll("[" + ch + ch.toUpperCase()+ "]", ""); // remove lowercase + uppercase

            removeAllReactions(reducedInput);

            if(resultingPolymer.length() < minLength){
                minLength = resultingPolymer.length();
            }

        }


        System.out.println("\n    Part 2 solution:   the length of the shortest polymer you can produce= " + minLength);
    }

    private static void removeAllReactions(String inputPolymer) {
        StringBuilder inputSB = new StringBuilder(inputPolymer);

//        System.out.println("DEBUG inputPolymer = " + inputPolymer);

        for (int i = 0; i < inputSB.length() - 2; i++) {
//            System.out.println("LOOP: " + i + ", inputPolymer = " + inputPolymer);
            char curr = inputSB.charAt(i);
            char next = inputSB.charAt(i + 1);

            boolean currIsCapital = Character.isUpperCase(curr);
            boolean nextIsCapital = Character.isUpperCase(next);

            if (Character.toLowerCase(curr) == Character.toLowerCase(next)) {
                if (currIsCapital != nextIsCapital) { // react

                    inputSB.delete(i, i + 2);

                    if (i > 0) {
                        i -= 2;
                    } else {
                        i--;
                    }
                }
            }
        }

        resultingPolymer = inputSB.toString();
    }

}
