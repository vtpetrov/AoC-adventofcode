package vtpaoc.y2025.day1;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;
import vtpaoc.common.enums.Direction;

import java.util.concurrent.atomic.AtomicInteger;

import static vtpaoc.common.enums.Direction.LEFT;
import static vtpaoc.common.enums.Direction.RIGHT;
import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class SecretEntrance extends BaseDay {

    static final int startPosition = 50;
    static String year = "2025";
    static String day = "01";
    static String puzzleTitle = "Secret Entrance";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//         inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInputLines();
        //or
//        loadDayInputWhole();
//        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {
        solutionP1 = turnSafeDial(true);

        log.info("""
                Part 1 solution:
                 the actual password to open the door
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1));
    }

    private static void solvePartTwo() {
        solutionP2 = turnSafeDial(false);

        log.info("""
                Part 2 solution:
                 Using password method 0x434C49434B, what is the password to open the door?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2));

    }

    private static int turnSafeDial(boolean countOnlyFinalZeros) {
        log.info("Turning dial starting from position {}...", startPosition);
        Direction direction;
        int startPos, endPos = startPosition;
        int endZeroCount = 0;
        int transZeroCount = 0;
        AtomicInteger counter = new AtomicInteger(0);

        for (String line : inputLines) {
            log.debug("============= {} =================", counter.incrementAndGet());
            startPos = endPos;
            direction = getDirection(line);
            int moves = Integer.parseInt(line.substring(1));
            int hundreds = moves / 100;
            int tens = moves % 100;

            // hundreds are directly count as transZeros
            transZeroCount += hundreds;
            if (tens > startPos && direction == LEFT && startPos != 0) { // add 1 extra zero transition for going left
                transZeroCount++;
            }

            endPos = startPos + ((direction == RIGHT) ? (tens) : (100 - tens));

            if (endPos > 100 && direction == RIGHT) { // add 1 more ZERO transition only if going RIGHT
                transZeroCount++;
            }

            // calc final endPos modulo 100
            endPos = endPos % 100;

            if (endPos == 0) endZeroCount++;

            log.debug("Start position: {}, end position: {}, direction: {}, moves: {}, hundreds: {}, tens: {}", startPos, endPos, direction, moves, hundreds, tens);
            //log zero positions on a single line:
            log.info("endZeros= {}, transZeros= {}", endZeroCount, transZeroCount);
        }
        return countOnlyFinalZeros ? endZeroCount : endZeroCount + transZeroCount;
    }

    private static Direction getDirection(String line) {
        if (line.startsWith("L")) {
            return LEFT;
        } else if (line.startsWith("R")) {
            return RIGHT;
        } else {
            throw new RuntimeException("Unknown direction: " + line);
        }
    }
}
