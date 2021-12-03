package y2021.day2;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class Dive {

    private static final Logger logger = LoggerFactory.getLogger(Dive.class.getSimpleName());
        private static final String INPUT_FILE_NAME = "year_2021/day2_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";
    static int depth = 0;
    static int hPosition = 0;
    static int aim = 0;
    static List<Move> moves = new ArrayList<>();

    public static void main(String[] args) {


        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + start);
        logger.info("                ---=== Day 002 ===---     ");
        logger.info("                      - Dive! -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        partOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("=========================================================================================");
        logger.info("    ---=== Part 2 ===---     ");

        partTwo();

        closeInput();

        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
    }

    private static void partOne() {
        while (getMainIn().hasNextLine()) {
//            parse line:
            String line = getMainIn().nextLine();
            String direction;
            int moveBy;

            direction = line.substring(0, line.indexOf(" "));
            moveBy = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
            moves.add(new Move(direction, moveBy));

            //calc positions
            if (direction.equalsIgnoreCase("forward")) {
                hPosition += moveBy;
            } else if (direction.equalsIgnoreCase("down")) {
                depth += moveBy;
            } else {
                depth -= moveBy;
            }

        }

        int result = depth * hPosition;
        logger.info("final positions: horizontal= {}", hPosition);
        logger.info("final positions: depth= {}", depth);
//        logger.info("Moves: \n {}", moves.toString());

        logger.info("Part 1 solution:\nWhat do you get if you multiply your final horizontal position by your final depth?\n = {}", result);
    }

    private static void partTwo() {
        //reinitialize depth and hpos:
        depth = 0;
        hPosition = 0;

        for(Move currMove : moves){
            String currDirection = currMove.getDirection();
            int currMoveBy = currMove.getMoveBy();

            //calc positions Part 2:
            if (currDirection.equalsIgnoreCase("down")) {
                aim += currMoveBy;
            } else if (currDirection.equalsIgnoreCase("up")) {
                aim -= currMoveBy;
            } else {
                // forward
                hPosition += currMoveBy;
                depth += (aim * currMoveBy);
            }

        }
//        down X increases your aim by X units.
//        up X decreases your aim by X units.
//        forward X does two things:
    //        It increases your horizontal position by X units.
    //        It increases your depth by your aim multiplied by X.

        int result2 = depth * hPosition;
        logger.info("final positions: horizontal= {}", hPosition);
        logger.info("final positions: depth= {}", depth);
        logger.info("    Part 2 solution:   \nWhat do you get if you multiply your final horizontal position by your final depth? \n= {}", result2);
    }

    @Data
    @AllArgsConstructor
    private static class Move {
        private String direction;
        private int moveBy;

        @Override
        public String toString() {
            return new StringJoiner(", ", Move.class.getSimpleName() + "[", "]")
                    .add("'" + direction + "'")
                    .add("'" + moveBy + "'")
                    .toString();
        }
    }
}
