package vtpaoc.y2021.day4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static vtpaoc.helper.InputLoader.*;

public class GiantSquid {

    private static final Logger logger = LoggerFactory.getLogger(GiantSquid.class.getSimpleName());
        private static final String INPUT_FILE_NAME = "year_2021/day4_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";
    private static final List<BingoBoard> bingoBoards = new ArrayList<>();
    private static List<Integer> numbers;
    private static final List<BingoBoard> winningBoards = new ArrayList<>();

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 4 ===---     ");
        logger.info("                   - Giant Squid -     ");

        //read input and populate bingo boards:
        readInputAndBingoBoards();

        logger.info("    ---=== Part 1 ===---     ");
        solvePartOne();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");
        logger.info("=========================================================================================");
        logger.info("    ---=== Part 2 ===---     ");
        solvePartTwo();

        closeInput();

        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
    }

    private static void solvePartOne() {
        logger.info("numbers: {}", numbers);
        callNumbersAndMarkThemUntilBoardWins();

        logger.info("    Part 1 solution:\n What will your final score be if you choose that board= [{}]"
                , winningBoards.get(0).getScore());

        logger.info("Boards state: {}", bingoBoards);
    }

    /**
     * Call numbers until a boardWins.
     * <br/> mark them on ALL boards as they are called.
     * <br/> set the winning board
     * <br/ set the winning SCORE
     */
    private static void callNumbersAndMarkThemUntilBoardWins() {
        int n = 1;
        while (numbers.size() > 0) {
            final Integer calledNumber = numbers.get(0);
            logger.info("#{}, {}", n++, calledNumber);
            for (BingoBoard currentBoard : bingoBoards) {
                currentBoard.markNumberOnThisBoard(calledNumber);
                if (currentBoard.isWinning()) {
                    // add this board to the list of winning boards:
                    winningBoards.add(currentBoard);
                }
            }

            //remove the called number from the list of numbers (to use in part Two):
            numbers.remove(calledNumber);
            logger.info("Number {} removed!", calledNumber);
            logger.info("numbers: {}", numbers);

            if (!winningBoards.isEmpty()) {
                logger.info("We have a winning board. Stop drawing numbers and announce it!");
                logger.info("First Winning board: seq#= {}, score= {}, Board={}"
                        , bingoBoards.indexOf(winningBoards.get(0)) + 1
                        , winningBoards.get(0).getScore()
                        , winningBoards.get(0));
                break;
            }
        }
    }

    private static void solvePartTwo() {
        // start removing winning boards from the list until on1y 1 remains, this is the last winning board:
        do {
            bingoBoards.removeAll(winningBoards);
            winningBoards.clear();
            callNumbersAndMarkThemUntilBoardWins();
        } while (bingoBoards.size() > 1 && numbers.size() > 0);

        logger.info("    Part 2 solution:\n Figure out which board will win last. " +
                "Once it wins, what would its final score be?= [{}]", winningBoards.get(0).getScore());
    }


    public static void readInputAndBingoBoards() {
        loadInput(INPUT_FILE_NAME, ",");
        numbers = Arrays.stream(getMainIn().nextLine().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        while (getMainIn().hasNextLine()) {
            //    private static final String INPUT_FILE_NAME = "debug.txt";
            String line = getMainIn().nextLine();
            if (line.isEmpty()) {
                //start reading new board
                BingoBoard newBoard = new BingoBoard();
                for (int l = 0; l < 5; l++) {
                    line = getMainIn().nextLine();
                    newBoard.addLine(line);
                }
                bingoBoards.add(newBoard);
            }
        }
    }
}
