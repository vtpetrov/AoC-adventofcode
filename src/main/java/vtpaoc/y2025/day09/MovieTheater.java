package vtpaoc.y2025.day09;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;
import vtpaoc.y2018.day6.Location;

import java.math.BigInteger;

import static vtpaoc.helper.DistanceAndArea.calculateRectangleArea;
import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class MovieTheater extends BaseDay {

    static String year = "2025";
    static String day = "09";
    static String puzzleTitle = "Movie Theater";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//         inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInputLines();
        //or
        //loadDayInputWhole();
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {

//        int ex6 = calculateRectangleArea(7, 3, 2, 3, true, true);
//        int ex50 = calculateRectangleArea(2, 5, 11, 1, true, true);
//        log.info("ex6 = {}", ex6);
//        log.info("ex50 = {}", ex50);
//        
        BigInteger largestRectangle = BigInteger.valueOf(Long.MIN_VALUE);
        for (int i = 0; i < inputLines.size(); i++) {
            for (int j = i + 1; j < inputLines.size(); j++) {
                Location p1 = new Location(inputLines.get(i), ",");
                Location p2 = new Location(inputLines.get(j), ",");
                BigInteger currRectangularArea = calculateRectangleArea(p1.getX(), p1.getY(), p2.getX(), p2.getY(), true, true);
                if (currRectangularArea.compareTo(largestRectangle) > 0) {
                    largestRectangle = currRectangularArea;
                }
                //print dot for each calculation
                System.out.print(".");
            }
        }
        System.out.println();
        
        solutionP1 = largestRectangle;

        log.info("""
                Part 1 solution:
                 Using two red tiles as opposite corners, what is the largest area of any rectangle you can make?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1));
    }

    private static void solvePartTwo() {

        log.info("""
                Part 2 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2));

    }
}
