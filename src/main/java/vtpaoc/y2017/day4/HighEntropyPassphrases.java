package vtpaoc.y2017.day4;

import java.util.*;

import static vtpaoc.helper.InputUtils.*;

public class HighEntropyPassphrases {

    private static final String INPUT_FILE_NAME = "year_2017/day4_input.txt";

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");

        System.out.println("    ---=== Day 4 ===---     ");
        System.out.println("\n    ---=== Part 1 ===---     ");
        partOne();
        System.out.println("\n    ---=== Part 2 ===---     ");
        partTwo();


    }

    /**
     * How many passphrases are valid?
     */
    private static void partOne() {

        loadInput(INPUT_FILE_NAME, ", ");

        String passphrase;

        int i = 1;
        int validCount = 0;
        while (getMainIn().hasNextLine()) {
//            System.out.println("        - " + i + " -");
            passphrase = getMainIn().nextLine();

            List<String> wordsList = Arrays.asList(passphrase.split(" "));

            Set<String> wordsSet = new HashSet<>(wordsList);

            if (wordsList.size() == wordsSet.size()) {
                validCount++;
            }
            i++;
        }

        System.out.println("Part 1 solution:  number of valid passphrases= " + validCount);

        closeInput();

    }

    private static void partTwo() {

        loadInput(INPUT_FILE_NAME, ", ");

        String passphrase;

        int counter = 1;
        int validCount = 0;
        while (getMainIn().hasNextLine()) {
//            System.out.println("    - " + counter + " -");
            passphrase = getMainIn().nextLine();
            boolean invalid = false;

            List<String> wordsList = Arrays.asList(passphrase.split(" "));

            int[] wordLengths = new int[10];

            for (String word : wordsList) {
                wordLengths[word.length()]++;
            }

            break_passphrase_check:
            for (int i = 1; i < wordLengths.length; i++) {

                if (wordLengths[i] > 1) {
                    List<List<String>> listOfWordCharsWithEqualLength = new ArrayList<>();

                    for (String word : wordsList) {
                        if (word.length() == i) {
                            String[] sorted = word.split("");
                            Arrays.sort(sorted);
                            listOfWordCharsWithEqualLength.add(Arrays.asList(sorted));
                        }
                    }

                    for (int k = 0; k < listOfWordCharsWithEqualLength.size() - 1; k++) {
                        for (int l = k + 1; l < listOfWordCharsWithEqualLength.size(); l++) {

                            if (listOfWordCharsWithEqualLength.get(k).equals(listOfWordCharsWithEqualLength.get(l))) {// 2 words
                                // has the same chars:
                                invalid = true;
                                break break_passphrase_check;
                            }
                        }
                    }

                }

            }

            if (!invalid) validCount++;
//            System.out.println("        " + (invalid ? "invalid" : "valid"));


            counter++;
        }

        System.out.println("Part 2 solution:  number of valid passphrases= " + validCount);

        closeInput();

    }
}
