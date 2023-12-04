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

    static {
//        inputFileName = "year_2023/day04_input.txt";
        inputFileName = "debug.txt";
    }

    static String year = "2023";
    static String day = "04";
    static String puzzleTitle = "Scratchcards";

    static List<List<Integer>> cardsWinningNumbers = new ArrayList<>();
    static List<List<Integer>> cardsMyNumbers = new ArrayList<>();
    static List<Integer> cardMatches = new ArrayList<>();
    static List<Long> cardPoints = new ArrayList<>();
    private static Long sumTotal = 0L;

    static List<Integer> cardInstances = new ArrayList<>();

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
        // per card for each number of mine matching a winning number, calculate the prize => 2^n-1 (n - the number of matches)
        calcMatches();

        solutionP1 = sumTotal;

        log.info("""
                Part 1 solution:
                 How many points are they worth in total?
                       = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void calcMatches() {
        for (int i = 0; i < inputLines.size(); i++) {
            cardsMyNumbers.get(i).retainAll(cardsWinningNumbers.get(i));
            cardMatches.add(cardsMyNumbers.get(i).size());

            cardPoints.add(round(floor(pow(2, cardMatches.get(i) - 1))));
            sumTotal += cardPoints.get(i);
            log.info("Card {}, has [{}] matches => |{}| points", i + 1, cardMatches.get(i), cardPoints.get(i));
        }
    }

    private static void solvePartTwo() {

        for (int i = 0; i < cardMatches.size(); i++) {
            // find spawned instances based on matches:
            Integer spawnNextXCards = cardMatches.get(i);
        }

        log.info("""
                Part 2 solution:
                 XXXXXX= [{}]""", solutionP2);
    }
//    Card 1, has [4] matches   => 1
//    Card 2, has [2] matches  c1  Card 2, has [2] matches  => 2
//    Card 3, has [2] matches  c1  Card 3, has [2] matches   c2  Card 3, has [2] matches  cc2  Card 3, has [2] matches   => 4
//    Card 4, has [1] matches  c1  Card 4, has [1] matches   c2  Card 4, has [1] matches  cc2  Card 4, has [1] matches   c3 Card 4, has [1] matches  c1  Card 4, has [1] matches   c2  Card 4, has [1] matches  cc2  Card 4, has [1] matches   => 8
//    Card 5, has [0] matches  c1  Card 5, has [0] matches                                                               c3 Card 5, has [0] matches  c1  Card 5, has [0] matches
//    Card 6, has [0] matches



}
