package vtpaoc.y2023.day6;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static vtpaoc.helper.InputLoader.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class BoatRaces extends BaseDay {

    static {
        inputFileName = "year_2023/day06_input.txt";
        // inputFileName = "debug.txt";
    }

    static String year = "2023";
    static String day = "06";
    static String puzzleTitle = "Wait For It";

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {
        //    Time:      7  15   30
        //    Distance:  9  40  200
        // List<Race> racesP1 = List.of(new Race(7, 9), new Race(15, 40), new Race(30, 200));

        //    Time:        40     92     97     90
        //    Distance:   215   1064   1505   1100
        List<Race> racesP1 = List.of(new Race(40, 215), new Race(92, 1064), new Race(97, 1505), new Race(90, 1100));

        solutionP1 = calcWaysToWin(racesP1, false);

        log.info("""
                Part 1 solution:
                 Determine the number of ways you could beat the record in each race.
                 What do you get if you multiply these numbers together?
                       = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void solvePartTwo() {
        // single race
        //    Time:      71530
        //    Distance:  940200
        // List<Race> racesP2 = List.of(new Race(71530, 940200));

        //    Time:        40929790
        //    Distance:   215106415051100
        List<Race> racesP2 = List.of(new Race(40_929_790, 215_106_415_051_100L));

        solutionP2 = calcWaysToWin(racesP2, false);

        log.info("""
                Part 2 solution:
                 How many ways can you beat the record in this one much longer race?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));
    }

    private static long calcWaysToWin(List<Race> racesP1, boolean printTravelDistances) {
        long multipleOfWaysToWin = 1;
        AtomicInteger i = new AtomicInteger(0);
        for (Race currRace : racesP1) {
            log.info("-------------------------------------------------------------------------");
            log.info("Race      {} , record= {}", i.incrementAndGet(), currRace.getRecord());
            List<Long> raceTravelDistances = new ArrayList<>();
            boolean doubleEnd = false;
            //    (rTime - holdTime) * holdTime = travelDist
            for (int holdTime = 0; holdTime < currRace.getTime() / 2 + 2; holdTime++) {
                long travelDistance = (long) (currRace.getTime() - holdTime) * holdTime;
                long lastElem = raceTravelDistances.isEmpty() ? -1 : raceTravelDistances.getLast();
                if (travelDistance >= lastElem) {
                    raceTravelDistances.add(travelDistance);
                } else {
                    break;
                }
            }
            int size = raceTravelDistances.size();
            // if the last 2 elements are equal, mark doubleEnd
            if (Objects.equals(raceTravelDistances.get(size - 1), raceTravelDistances.get(size - 2))) {
                doubleEnd = true;
            }

            log.info("raceTravelDistances.size 1/2= {}, doubleEnd= {}", size, doubleEnd);
            if (printTravelDistances) {
                log.info("raceTravelDistances: {}", raceTravelDistances);
            }

            long waysToWin = raceTravelDistances.stream().filter(d -> d > currRace.getRecord()).count();
            log.info("waysToWin= {}, doubleEnd= {}", waysToWin, doubleEnd);
            long adjustedWaysToWin = doubleEnd ? (waysToWin - 1) * 2 : waysToWin * 2 - 1;
            log.info("adjustedWaysToWin= {}", adjustedWaysToWin);

            multipleOfWaysToWin *= adjustedWaysToWin;

        }
        return multipleOfWaysToWin;
    }
}
