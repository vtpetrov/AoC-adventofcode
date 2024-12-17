package vtpaoc.y2023.day4;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static java.util.Arrays.stream;
import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

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
    static int[] cardCopies = new int[209];
    static List<Long> cardPoints = new ArrayList<>();
    private static Long sumTotal = 0L;

    static List<Integer> cardInstances = new ArrayList<>();
    static List<List<Integer>> spawningCards = new ArrayList<>();


    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInputLines();

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
//            log.info("Card {}, has [{}] matches => |{}| points", i + 1, cardMatches.get(i), cardPoints.get(i));
        }
    }

    private static void solvePartTwo() {

        if (!isCalcMatchesInvoked) {
            calcMatches();
        }

        int currentCardIdx = 0;

        int originalsCount = cardMatches.size();
        while (currentCardIdx < originalsCount - 1) {

            int nCardsfollowingCurrenToBeCopied = cardMatches.get(currentCardIdx);

            // 1 for the original and X from the copies of the original:
            int howManyCopiesOfEach = 1 + cardCopies[currentCardIdx];
            // THEN increment the value of the COPIES array with howManyCopiesOfEach
            for (int c = currentCardIdx + 1; c < currentCardIdx + 1 + nCardsfollowingCurrenToBeCopied; c++) {
                cardCopies[c] += howManyCopiesOfEach;
            }
            currentCardIdx++;
        }

        int copiesCount = stream(cardCopies).sum();
        solutionP2 = originalsCount + copiesCount;

        log.info("""
                Part 2 solution:
                 Process all of the original and copied scratchcards until no more scratchcards are won.
                 Including the original set of scratchcards, how many total scratchcards do you end up with?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));
    }
}
