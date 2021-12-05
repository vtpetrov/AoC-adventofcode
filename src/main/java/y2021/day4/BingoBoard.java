package y2021.day4;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Data
public class BingoBoard {

    private static final Logger logger = LoggerFactory.getLogger(BingoBoard.class.getSimpleName());
    private List<List<BingoNumber>> lines = new ArrayList<>();
    private int lastCalledNumber;
    private boolean isWinning = false;
    private int score = 0;

    public void addLine(@NotNull String line) {
        line = line.trim();
        List<BingoNumber> lineOfBingoNumbers = new ArrayList<>();
        final String[] lineSplit = line.split("[^0-9]{1,3}");
        Arrays.stream(lineSplit).forEach(n -> {
            lineOfBingoNumbers.add(new BingoNumber(Integer.parseInt(n)));
        });

        this.lines.add(lineOfBingoNumbers);
    }

    /**
     * Mark the given number and set {@code isWinning} flag and {@code score} for this board.
     * @param drawnNumber the number that was called and need to be marked on this board
     */
    public void markNumberOnThisBoard(final int drawnNumber) {
        boolean stopSearch = false;
        for (List<BingoNumber> line : this.lines) {
            for (BingoNumber bingoNumber : line) {
                if (bingoNumber.getValue() == drawnNumber) {
                    bingoNumber.mark();
                    this.lastCalledNumber = drawnNumber;
                    stopSearch = true;
                    break;
                }
            }
            if (stopSearch) {
                break;
            }
        }

        this.setIsWinning();
        this.calculateScore();

    }

    @Override
    public String toString() {
        final StringBuilder linesJoiner = new StringBuilder();
        linesJoiner.append("\n");
        for (List<BingoNumber> line : lines) {
            linesJoiner.append(line.toString()).append("\n");
        }
        return linesJoiner.toString();
    }

    /**
     * Assess if the board is winning or not.
     * <br/> Board should have one completely marked row or column in order to win.
     * <br/> Set this status in field "isWinning"
     */
    public void setIsWinning() {
        if (hasCompleteLine()) {
            isWinning = true;
        } else {
            isWinning = hasCompleteColumn();
        }
    }

    private boolean hasCompleteColumn() {
        for (int col = 0; col < 5; col++) {
            List<BingoNumber> column = new ArrayList<>();
            for (List<BingoNumber> line : lines) {
                column.add(line.get(col));
            }
            int markedInColumn = (int) column.stream().filter(BingoNumber::isMarked).count();
            if (markedInColumn == 5) {
                logger.info("Winning COLUMN: {}", column);
                return true;
            }
        }
        return false;
    }

    private boolean hasCompleteLine() {
        for (List<BingoNumber> line : this.getLines()) {
            int markedInLine = (int) line.stream().filter(BingoNumber::isMarked).count();
            if (markedInLine == 5) {
                logger.info("Winning LINE: {}", line);
                return true;
            }
        }
        return false;
    }

    /**
     * Set the score of this board:
     * <li>the sum of all unmarked numbers on that board</li>
     * <li>* (multiplied by)</li>
     * <li>the last called number</li>
     */
    public void calculateScore() {
        final int sumOfAllUnmarkedNumbersOnBoard = lines.stream().flatMap(Collection::stream).collect(Collectors.toList())
                .stream().filter(bn -> !bn.isMarked()).mapToInt(BingoNumber::getValue).sum();

        this.score = sumOfAllUnmarkedNumbersOnBoard * lastCalledNumber;
    }


    @Data
    private class BingoNumber {
        private int value;
        private boolean marked;

        public BingoNumber(int number) {
            this.value = number;
            this.marked = false;
        }

        public void mark() {
            this.marked = true;
        }

        public void unMark() {
            this.marked = false;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "[", "]")
                    .add(value + (marked ? " x" : ""))
                    .toString();
        }
    }
}
