package vtpaoc.y2024.day04;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;

import static vtpaoc.helper.InputLoader.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;
import static vtpaoc.helper.Misc.prettyPrintTwoDimensArray;

@Slf4j
public class CeresSearch extends BaseDay {

    static String year = "2024";
    static String day = "04";
    static String puzzleTitle = "Ceres Search";

    private static boolean printInput = false;
    //    public static final int schemaSize = 10;
    public static final int schemaSize = 140;

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInputLines();

        drawInputSchema(printInput);

        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    public static final int WORD_LENGTH = 4;
    public static final Character[] XMAS_ARRAY = {'X', 'M', 'A', 'S'};

    private static Character[][] inputSchema = new Character[schemaSize][schemaSize];


    private static void solvePartOne() {
        // traverse each element in the Character[][] inputSchema and look in the 8 possible directions
        // right, left, up, down, right-up, right-down, left-up, left-down
        // for a match for the string X M A S
        int totalCountP1 = 0;

        for (int i = 0; i < schemaSize; i++) {
            for (int j = 0; j < schemaSize; j++) {
                // Check each of the 8 possible directions from the current position
                totalCountP1 += findXMAS(i, j);
            }
        }

        solutionP1 = totalCountP1;

        log.info("""
                Part 1 solution:
                 Take a look at the little Elf's word search.
                 How many times does XMAS appear?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1));
    }

    private static int findXMAS(int row, int col) {
        int matches = 0;
        // Define the 8 directions: right, down, left, up, right-down, right-up, left-down, left-up
        int[][] directions = {
                {0, 1},   // Right
                {1, 0},   // Down
                {0, -1},  // Left
                {-1, 0},  // Up
                {1, 1},   // Right-Down
                {-1, 1},  // Right-Up
                {1, -1},  // Left-Down
                {-1, -1}  // Left-Up
        };

        for (int[] direction : directions) {
            try {
                boolean isMatch = true;
                for (int k = 0; k < WORD_LENGTH; k++) {
                    int newRow = row + k * direction[0];
                    int newCol = col + k * direction[1];
                    if (!inputSchema[newRow][newCol].equals(XMAS_ARRAY[k])) {
                        isMatch = false;
//                        xmasPicture[newRow][newCol] = '.';
                        break;
                    }
                }
                if (isMatch) matches++;
            } catch (IndexOutOfBoundsException e) {
                // Ignore exception and continue with the next direction
            }
        }

        return matches;
    }

    private static void solvePartTwo() {
        int totalXCount = 0;

        for (int i = 0; i < schemaSize; i++) {
            for (int j = 0; j < schemaSize; j++) {
                // Look for an 'A' and check all possible ways two "MAS" cross around it
                if (inputSchema[i][j].equals('A')) {
                    totalXCount += countMASXPattern(i, j);
                }
            }
        }

        solutionP2 = totalXCount;

        log.info("""
                Part 2 solution:
                 Flip the word search from the instructions back over to the word search side and try again.
                 How many times does an X-MAS appear?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2));

    }

    private static int countMASXPattern(int row, int col) {
        int matches = 0;

        // Check all possible diagonals for "MAS" crossing at 'A'
        matches += checkDiagonalForMas(row, col, -1, 1, 1, -1); // Top-Right ↔ Bottom-Left
        matches += checkDiagonalForMas(row, col, -1, -1, 1, 1); // Top-Left ↔ Bottom-Right
        matches += checkDiagonalForMas(row, col, 1, -1, -1, 1); // Bottom-Left ↔ Top-Right
        matches += checkDiagonalForMas(row, col, 1, 1, -1, -1); // Bottom-Right ↔ Top-Left

        // Every valid X pattern must have two valid diags => count 1 match per valid pair of diagonals
        return matches >= 2 ? 1 : 0;
    }

    private static int checkDiagonalForMas(int row, int col, int dir1Row, int dir1Col, int dir2Row, int dir2Col) {
        try {
            // Check the first diagonal direction
            int mRow = row + dir1Row;
            int mCol = col + dir1Col;

            // Check the second diagonal direction
            int sRow = row + dir2Row;
            int sCol = col + dir2Col;

            // Ensure "MAS" pattern is valid in this direction
            if ((inputSchema[mRow][mCol].equals('M') && inputSchema[sRow][sCol].equals('S'))) {
                return 1;
            }
        } catch (IndexOutOfBoundsException e) {
            // If out of bounds, ignore and return 0
        }
        return 0;
    }

    private static void drawInputSchema(boolean printSchema) {
        for (int i = 0; i < schemaSize; i++) {
            inputSchema[i] = inputLines.get(i).chars().mapToObj(elem -> (char) elem).toArray(Character[]::new);
        }
        if (printSchema) {
            log.info("input schema loaded:\n{}", prettyPrintTwoDimensArray(inputSchema, true, " "));
        }
    }
}
