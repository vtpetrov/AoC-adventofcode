package vtpaoc.y2024.day06;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;
import vtpaoc.common.enums.Direction;

import java.util.Arrays;

import static vtpaoc.common.enums.Direction.*;
import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.InputUtils.convertInputToTowDimmensArrOfChars;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class GuardGallivant extends BaseDay {

    static String year = "2024";
    static String day = "06";
    static String puzzleTitle = "Guard Gallivant";
    private static Character[][] areaMap;
    private static final char START_SYMBOL = '^';
    private static final char OBSTACLE_SYMBOL = '#';
    private static final char VISITED = 'X';
    private static int currPosRow, currPosCol;

    private static boolean printInput = true;
//    public static final int schemaSize = 10;
        public static final int schemaSize = 130;
    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInputLines();
        areaMap = convertInputToTowDimmensArrOfChars(inputLines, schemaSize, printInput);

        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static int moves = 0;
    private static Direction currentDirection;

    private static void solvePartOne() {

        int startRow = 0, startCol = 0;
        //set start position (character ^)
        for (int i = 0; i < schemaSize; i++) {
            for (int j = 0; j < schemaSize; j++) {
                if (areaMap[i][j] == START_SYMBOL) {
                    startRow = i;
                    startCol = j;
                    break;
                }
            }
        }

        log.info("start: {}, {}", startRow, startCol);
        startMovingGuardP1(startRow, startCol);

        log.debug("Guard moved {} times", moves);
        solutionP1 = Arrays.stream(areaMap)
                .flatMap(Arrays::stream)
                .filter(c -> c == VISITED)
                .count();

        log.info("""
                Part 1 solution:
                 Predict the path of the guard.
                 How many distinct positions will the guard visit before leaving the mapped area?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1));
    }

    private static void startMovingGuardP1(int startRow, int startCol) {
        currPosRow = startRow;
        currPosCol = startCol;
        currentDirection = UP;

        //move toward the current direction (initially UP)
        //if guard reaches obstacle (#) turn RIGHT
        // continue moving forward or turning RIGHT until going out of map area

        boolean move = true;
        do {
            areaMap[currPosRow][currPosCol] = VISITED; // Including the guard's starting position
            try {
                move();
            } catch (ArrayIndexOutOfBoundsException e) {
                move = false;
            }
        } while (move);

    }

    private static void move() {
        moves++;
        Character nextPathElem;

        switch (currentDirection) {
            case DOWN:
                nextPathElem = areaMap[currPosRow + 1][currPosCol];
                if (nextPathElem == OBSTACLE_SYMBOL) {
                    turnRight();
                } else {
                    moveDown();
                }
                break;
            case UP:
                nextPathElem = areaMap[currPosRow - 1][currPosCol];
                if (nextPathElem == OBSTACLE_SYMBOL) {
                    turnRight();
                } else {
                    moveUp();
                }
                break;
            case LEFT:
                nextPathElem = areaMap[currPosRow][currPosCol - 1];
                if (nextPathElem == OBSTACLE_SYMBOL) {
                    turnRight();
                } else {
                    moveLeft();
                }
                break;
            case RIGHT:
                nextPathElem = areaMap[currPosRow][currPosCol + 1];
                if (nextPathElem == OBSTACLE_SYMBOL) {
                    turnRight();
                } else {
                    moveRight();
                }
                break;
        }
    }

    /**
     * Change the direction from currentDirection to the next one to the right.
     * For example UP -> RIGHT -> DOWN -> LEFT
     */
    private static void turnRight() {
        switch (currentDirection) {
            case UP:
                currentDirection = RIGHT;
                break;
            case RIGHT:
                currentDirection = DOWN;
                break;
            case DOWN:
                currentDirection = LEFT;
                break;
            case LEFT:
                currentDirection = UP;
                break;
        }
    }

    private static void moveLeft() {
        currPosCol--;
    }

    private static void moveRight() {
        currPosCol++;
    }

    private static void moveUp() {
        currPosRow--;
    }

    private static void moveDown() {
        currPosRow++;
    }

    private static void solvePartTwo() {

        log.info("""
                Part 2 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }
}
