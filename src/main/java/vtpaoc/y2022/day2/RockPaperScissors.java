package vtpaoc.y2022.day2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static vtpaoc.helper.InputUtils.*;

public class RockPaperScissors {

    private static final Logger logger = LoggerFactory.getLogger(RockPaperScissors.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2022/day02_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static List<String[]> roundMoves = new ArrayList<>();

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 02 ===---     ");
        logger.info("               - Rock Paper Scissors -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
            roundMoves.add(line.split(" "));
        }


        solvePartOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        logger.info("================================");
        logger.info("    ---=== Part 2 ===---     ");

        solvePartTwo();

        closeInput();


        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========----------------==============");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOne() {

        AtomicInteger counter = new AtomicInteger(0);

        logger.info("P1 Start points: elf={}, me={}", Players.ELF, Players.ME);

        for (String[] currentMoves : roundMoves) {
//            logger.info("line {}: {}", counter.incrementAndGet(), currentMoves);
            // [0] is elf, [1] is me
            Players.addPointsPart1(Move.fromElfPlay(currentMoves[0]), Move.fromMyPlay(currentMoves[1]));
//            logger.info("current points: elf={}, me={}", Players.ELF, Players.ME);
//            logger.info("-----      round  {}  end       -------", counter.get());
        }

        logger.info("P1 END points: elf={}, me={}", Players.ELF, Players.ME);
        logger.info("    Part 1 solution:\n my end points= [{}]\n", Players.ME.points);

    }

    private static void solvePartTwo() {

        Players.ELF.points = 0;
        Players.ME.points = 0;

        AtomicInteger counter = new AtomicInteger(0);

        logger.info("P2 Start points: elf={}, me={}", Players.ELF, Players.ME);

        for (String[] currentMoves : roundMoves) {
//            logger.info("line {}: {}", counter.incrementAndGet(), currentMoves);
            // [0] is elf, [1] is me
            Players.addPointsPart2(Move.fromElfPlay(currentMoves[0]), Move.fromMyDesiredResult(currentMoves[1]));
//            logger.info("current points: elf={}, me={}", Players.ELF, Players.ME);
//            logger.info("-----      round  {}  end       -------", counter.get());
        }

        logger.info("P2 END points: elf={}, me={}", Players.ELF, Players.ME);
//      solution: 207576
        logger.info("    Part 2 solution:\n My total score= [{}]\n", Players.ME.points);
    }


    public enum Move {
        ROCK("A", "X", 1),
        PAPER("B", "Y", 2),
        SCISSORS("C", "Z", 3);

        final String elfsCode;
        final String myCode;
        final Integer score;

        Move(String elfCode, String myCode, Integer score) {
            this.elfsCode = elfCode;
            this.myCode = myCode;
            this.score = score;
        }

        private static Move fromElfPlay(String elfsMove) {
            switch (elfsMove) {
                case "A":
                    return ROCK;
                case "B":
                    return PAPER;
                case "C":
                    return SCISSORS;
                default:
                    throw new Error("unknown elf's move, Valid are A, B, C");
            }
        }

        private static Move fromMyPlay(String myMove) {
            switch (myMove) {
                case "X":
                    return ROCK;
                case "Y":
                    return PAPER;
                case "Z":
                    return SCISSORS;
                default:
                    throw new Error("unknown my move, Valid are X, Y, Z");
            }
        }

        private static RoundScores fromMyDesiredResult(String myMove) {
            switch (myMove) {
                case "X":
                    return RoundScores.LOSE;
                case "Y":
                    return RoundScores.DRAW;
                case "Z":
                    return RoundScores.WIN;
                default:
                    throw new Error("unknown my desired result, Valid are X, Y, Z");
            }
        }


    }

    public enum Players {
        ELF(0),
        ME(0);

        Integer points;

        Players(Integer points) {
            this.points = points;
        }

        public static void addPointsPart1(final Move elfsMove, final Move myMove) {

            // Draw:
            if (elfsMove.equals(myMove)) {
                // sum score for move and round
                ELF.points += (elfsMove.score + RoundScores.DRAW.score);
                ME.points += (myMove.score + RoundScores.DRAW.score);
            } else {
                if (elfsMove.equals(Move.ROCK) && myMove.equals(Move.PAPER)) {
                    //I win:
                    ELF.points += (elfsMove.score + RoundScores.LOSE.score);
                    ME.points += (myMove.score + RoundScores.WIN.score);
                } else {
                    if (elfsMove.equals(Move.ROCK) && myMove.equals(Move.SCISSORS)) {
                        //Elf win:
                        ELF.points += (elfsMove.score + RoundScores.WIN.score);
                        ME.points += (myMove.score + RoundScores.LOSE.score);
                    } else {
                        if (elfsMove.equals(Move.PAPER) && myMove.equals(Move.ROCK)) {
                            //Elf win
                            ELF.points += (elfsMove.score + RoundScores.WIN.score);
                            ME.points += (myMove.score + RoundScores.LOSE.score);
                        } else {
                            if (elfsMove.equals(Move.PAPER) && myMove.equals(Move.SCISSORS)) {
                                // I win
                                ELF.points += (elfsMove.score + RoundScores.LOSE.score);
                                ME.points += (myMove.score + RoundScores.WIN.score);
                            } else {
                                if (elfsMove.equals(Move.SCISSORS) && myMove.equals(Move.ROCK)) {
                                    // I win
                                    ELF.points += (elfsMove.score + RoundScores.LOSE.score);
                                    ME.points += (myMove.score + RoundScores.WIN.score);
                                } else {
                                    if (elfsMove.equals(Move.SCISSORS) && myMove.equals(Move.PAPER)) {
                                        // Elf win
                                        ELF.points += (elfsMove.score + RoundScores.WIN.score);
                                        ME.points += (myMove.score + RoundScores.LOSE.score);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public static void addPointsPart2(Move elfsMove, RoundScores myDesiredResult) {

            Move myMove = null;

            if (myDesiredResult.equals(RoundScores.DRAW)) {
                myMove = elfsMove;
                addPointsPart1(elfsMove, myMove);
            }

            //Elf win:
            if (myDesiredResult.equals(RoundScores.LOSE)) {
//                elfsMove.equals(Move.ROCK) && myMove.equals(Move.SCISSORS)) {
                if (elfsMove.equals(Move.ROCK)) {
                    myMove = Move.SCISSORS;
                    addPointsPart1(elfsMove, myMove);
                } else if (elfsMove.equals(Move.PAPER)) {
//                    (elfsMove.equals(Move.PAPER) && myMove.equals(Move.ROCK)) {
                    myMove = Move.ROCK;
                    addPointsPart1(elfsMove, myMove);
                } else if (elfsMove.equals(Move.SCISSORS)) {
//                (elfsMove.equals(Move.SCISSORS) && myMove.equals(Move.PAPER)) {
                    myMove = Move.PAPER;
                    addPointsPart1(elfsMove, myMove);
                }
            }

            // I win:
            if (myDesiredResult.equals(RoundScores.WIN)) {
                if (elfsMove.equals(Move.ROCK)) {
                    myMove = Move.PAPER;
                    addPointsPart1(elfsMove, myMove);
                } else if (elfsMove.equals(Move.PAPER)) {
                    myMove = Move.SCISSORS;
                    addPointsPart1(elfsMove, myMove);
                } else if (elfsMove.equals(Move.SCISSORS)) {
                    myMove = Move.ROCK;
                    addPointsPart1(elfsMove, myMove);
                }
            }
        }

        @Override
        public String toString() {
            return "points=" + points;
        }
    }

    public enum RoundScores {
        LOSE(0),
        DRAW(3),
        WIN(6);

        final Integer score;

        RoundScores(Integer score) {
            this.score = score;
        }
    }


    private static class MyDesiredResult {

    }
}
