package y2023.day4;

import base.BaseDay;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static helper.InputLoader.closeInput;
import static helper.Misc.prettyPrintNumber;
import static java.lang.Math.*;
import static java.util.Arrays.stream;

@Slf4j
public class Scratchcards extends BaseDay {

    private static boolean isCalcMatchesInvoked = false;

    static {
        inputFileName = "year_2023/day04_input.txt";
//        inputFileName = "debug.txt";
    }

    static String year = "2023";
    static String day = "04";
    static String puzzleTitle = "Scratchcards";

    static List<List<Integer>> cardsWinningNumbers = new ArrayList<>();
    static List<List<Integer>> cardsMyNumbers = new ArrayList<>();
    static List<Integer> cardMatches = new ArrayList<>();
    static List<Integer> cardCoppies = new ArrayList<>();
    static List<Long> cardPoints = new ArrayList<>();
    private static Long sumTotal = 0L;

    static List<Integer> cardInstances = new ArrayList<>();
    static List<List<Integer>> spawningCards = new ArrayList<>();


    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadInputDay();

        readNumbers();

        solvePartOne();
        logStartP2();

        solvePartTwo();

        closeInput();
        logEndP2();
    }

    private static void readNumbers() {
        for (String line : inputLines) {
            Pattern p = Pattern.compile("\\d+: (.*) \\| (.*)");
            Matcher m = p.matcher(line);
            if (m.find()) {
                cardsWinningNumbers.add(stream(m.group(1).split(" "))
                        .filter(e -> !e.isEmpty())
                        .map(Integer::valueOf)
                        .collect(Collectors.toList()));

                cardsMyNumbers.add(stream(m.group(2).split(" "))
                        .filter(e -> !e.isEmpty())
                        .map(Integer::valueOf)
                        .collect(Collectors.toList()));
            }
        }
    }

    private static void solvePartOne() {

        calcMatches();

        solutionP1 = sumTotal;

        log.info("""
                Part 1 solution:
                 How many points are they worth in total?
                       = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void calcMatches() {
        isCalcMatchesInvoked = true;
        for (int i = 0; i < inputLines.size(); i++) {
            cardsMyNumbers.get(i).retainAll(cardsWinningNumbers.get(i));
            cardMatches.add(cardsMyNumbers.get(i).size());

            cardPoints.add(round(floor(pow(2, cardMatches.get(i) - 1))));
            sumTotal += cardPoints.get(i);
            log.info("Card {}, has [{}] matches => |{}| points", i + 1, cardMatches.get(i), cardPoints.get(i));
        }
    }

    private static void solvePartTwo() {

        if (!isCalcMatchesInvoked) {
            calcMatches();
        }


        int currentCardIdx = 0;
        cardCoppies.add(0); // set the number of copies for Card 1 to 0 (because it starts the sequence and doesn't have copies)
        while (currentCardIdx < cardMatches.size() - 1) {
            int currCardMatches = cardMatches.get(currentCardIdx);

            // spawn copies:
            int from = currentCardIdx + 1;
            int to = from + currCardMatches;
            // process ORIGINALS:
            for (int j = from; j < to; j++) {
                try {
                    Integer old = cardCoppies.get(j);
                    cardCoppies.set(j, ++old);
                } catch (IndexOutOfBoundsException e) {
                    cardCoppies.add(1);
                }
            }

            // get the number of copies of the ORIGINAL card currently being processed
            Integer currCardCopies = cardCoppies.get(currentCardIdx);
            log.info("currCardCopies= {}", currCardCopies);
            for (int c = from; c < to; c++) {
                // increment FROM-TO in COPIES by adding 'currCardCopies' to them
                Integer newVal;
                try {
                    newVal = cardCoppies.get(c) + currCardCopies;
                    cardCoppies.set(c, newVal);
                } catch (IndexOutOfBoundsException e) {
                    cardCoppies.add(currCardCopies);
                }
            }


//                // process COPIES:
//                Integer currCardCopies = cardCoppies.get(currentCardIdx);// get the number of copies of the ORIGINAL card currently being processed
//                Integer newVal = cardCoppies.get(j) + currCardCopies;
//                cardCoppies.set(j, newVal);

//            // account for a lot of 0 matches, sync copy list with 0 copies; copy list should follow the 'currentCardIdx'
//            if(currentCardIdx >= cardCoppies.size()){
//                for (int i = 0; i < currentCardIdx - cardCoppies.size() + 1; i++) {
//                    cardCoppies.add(0);
//                }
//            }

            currentCardIdx++;
        }

        solutionP2 = cardMatches.size() + cardCoppies.stream().mapToInt(Integer::intValue).sum();

        log.info("""
                Part 2 solution:
                 Process all of the original and copied scratchcards until no more scratchcards are won.
                 Including the original set of scratchcards, how many total scratchcards do you end up with?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));
    }
}
