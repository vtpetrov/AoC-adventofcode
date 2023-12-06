package y2023.day6;

import base.BaseDay;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static helper.InputLoader.closeInput;
import static helper.Misc.prettyPrintNumber;

@Slf4j
public class BoatRaces extends BaseDay {

    static {
        inputFileName = "year_2023/day06_input.txt";
        // inputFileName = "debug.txt";
    }

    static String year = "2023";
    static String day = "06";
    static String puzzleTitle = "Wait For It";

    //    Time:      7  15   30
    //    Distance:  9  40  200
//    static List<Race> races = List.of(new Race(7, 9), new Race(15, 40), new Race(30, 200));

    //    Time:        40     92     97     90
    //    Distance:   215   1064   1505   1100
    static List<Race> races = List.of(new Race(40, 215), new Race(92, 1064), new Race(97, 1505), new Race(90, 1100));

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    //    (rTime - holdTime) * holdTime = travelDist
//
//        0. (7 - 0) * 0      =           0
//        1. (7 - 1) * 1      = 6 * 1 =   6
//        2. (7 - 2) * 2      = 5 * 2 =   10
//        3. (7 - 3) * 3      = 4 * 3 =   12
//        4. (7 - 4) * 4      = 3 * 4 =   12
//        5. (7 - 5) * 5      =           10
//        6. (7 - 6) * 6      =           6
//        7. (7 - 7) * 7      =           0
    private static void solvePartOne() {


        long multipleOfWaysToWin = 1;
        for (Race currRace : races) {
            List<Integer> raceTravelDistances = new ArrayList<>();
            //    (rTime - holdTime) * holdTime = travelDist
            for (int holdTime = 0; holdTime < currRace.getTime() + 1; holdTime++) {
                int travelDistance = (currRace.getTime() - holdTime) * holdTime;
                raceTravelDistances.add(travelDistance);
            }
            long waysToWin = raceTravelDistances.stream().filter(d -> d > currRace.getRecord()).count();
            multipleOfWaysToWin *= waysToWin;
        }

        solutionP1 = multipleOfWaysToWin;

        log.info("""
                Part 1 solution:
                 Determine the number of ways you could beat the record in each race.
                 What do you get if you multiply these numbers together?      
                       = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void solvePartTwo() {

        log.info("""
                Part 2 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));
    }

}
