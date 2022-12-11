package y2022.day8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class TreetopTreeHouse {

    private static final Logger logger = LoggerFactory.getLogger(TreetopTreeHouse.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2022/day08_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static String solution;
    private static int arrLength;
    private static int[][] trees;


    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 08 ===---     ");
        logger.info("               - Treetop Tree House -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
        }

        arrLength = inputLines.size();
        trees = new int[arrLength][];

        int[] intLineOfTrees;
        for (int i = 0; i < inputLines.size(); i++) {
            intLineOfTrees = Arrays.stream(inputLines.get(i).split("")).mapToInt(Integer::parseInt).toArray();
            trees[i] = intLineOfTrees;
//            logger.info("trees[itr {}]: {}", i, Arrays.deepToString(trees));
        }

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
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOne() {
        int visibleTrees = 0;
        int maxViewingDistance = 0;
        row:
        for (int i = 0; i < arrLength; i++) {
            column:
            for (int j = 0; j < arrLength; j++) {
                int currTree = trees[i][j];
                int currViewingDistance = 0;
                boolean currTreeIsVisible = false;
                // if boundary, mark it as visible
                if (i == 0 || i == arrLength - 1
                        || j == 0 || j == arrLength - 1) {
                    visibleTrees++;
                    currTreeIsVisible = true;
                    logger.info("Boundary tree found; trees[{}][{}] = {}; visible trees so far= {}", i, j, currTree, visibleTrees);
                } else { // check if tree is visible from the edge

                    // look RIGHT
                    for (int r = j + 1; r < arrLength; r++) {
                        if (currTree <= trees[i][r]) { // if we see tree same height or taller, then it's not possible to be visible:
                            // stop looking to the RIGHT:
                            break;
                        }
                        if (r == arrLength - 1) { // last iteration of the look RIGHT
                            // if it didn't brake before that, then tree is visible from the RIGHT
                            visibleTrees++;
                            logger.info("Visible tree from RIGHT; trees[{}][{}] = {}; visible trees so far= {}", i, j, currTree, visibleTrees);
                            currTreeIsVisible = true;
                            //don't look any other direction(s), just move to the next tree:
                            continue column;
                        }
                    }

                    // look LEFT
                    for (int l = j - 1; l >= 0; l--) {
                        if (currTree <= trees[i][l]) { // if we see tree same height or taller, then it's not possible to be visible:
                            // stop looking to the LEFT:
                            break;
                        }
                        if (l == 0) { // last iteration of the look LEFT
                            // if it didn't brake before that, then tree is visible from the LEFT
                            visibleTrees++;
                            logger.info("Visible tree from LEFT; trees[{}][{}] = {}; visible trees so far= {}", i, j, currTree, visibleTrees);
                            currTreeIsVisible = true;
                            //don't look any other direction(s), just move to the next tree:
                            continue column;
                        }
                    }

                    // look UP
                    for (int u = i - 1; u >= 0; u--) {
                        if (currTree <= trees[u][j]) { // if we see tree same height or taller, then it's not possible to be visible:
                            // stop looking UP:
                            break;
                        }
                        if (u == 0) { // last iteration of the look UP
                            // if it didn't brake before that, then tree is visible from TOP
                            visibleTrees++;
                            logger.info("Visible tree from UP; trees[{}][{}] = {}; visible trees so far= {}", i, j, currTree, visibleTrees);
                            currTreeIsVisible = true;
                            //don't look any other direction(s), just move to the next tree:
                            continue column;
                        }
                    }

                    // look DOWN
                    for (int d = i + 1; d < arrLength; d++) {
                        if (currTree <= trees[d][j]) { // if we see tree same height or taller, then it's not possible to be visible:
                            // stop looking DOWN:
                            break;
                        }
                        if (d == arrLength - 1) { // last iteration of the look DOWN
                            // if it didn't brake before that, then tree is visible from the BOTTOM
                            visibleTrees++;
                            logger.info("Visible tree from DOWN; trees[{}][{}] = {}; visible trees so far= {}", i, j, currTree, visibleTrees);
                            currTreeIsVisible = true;
                            //don't look any other direction(s), just move to the next tree:
                            continue column;
                        }
                    }

                }
            }
        }

        logger.info("    Part 1 solution:\n how many trees are visible from outside the grid?= [{}]"
                , visibleTrees);

    }

    private static void solvePartTwo() {
        int maxViewingDistance = 0;

        row:
        for (int i = 0; i < arrLength; i++) {
            column:
            for (int j = 0; j < arrLength; j++) {
                int currTree = trees[i][j];
                int currViewingDistance = 1;

                // Start calculating viewing distance in the different directions:
                // look RIGHT
                int offset;
                for (int r = j + 1; r < arrLength; r++) {
                    offset = r - j;
                    if (currTree <= trees[i][r]) { // if we see tree same height or taller, then this is the END of the viewing distance in this direction:
                        // calc distance by multiplying the one obtained so far by the offset:
                        currViewingDistance *= offset;
                        // stop looking to the RIGHT:
                        break;
                    }
                    if (r == arrLength - 1) { // last iteration of the look RIGHT
                        // calc distance by multiplying the one obtained so far by the offset:
                        currViewingDistance *= offset;
                    }
                }

                // look LEFT
                for (int l = j - 1; l >= 0; l--) {
                    offset = j - l;
                    if (currTree <= trees[i][l]) { // if we see tree same height or taller, then this is the END of the viewing distance in this direction:
                        // calc distance by multiplying the one obtained so far by the offset:
                        currViewingDistance *= offset;
                        // stop looking to the LEFT:
                        break;
                    }
                    if (l == 0) { // last iteration of the look LEFT
                        // calc distance by multiplying the one obtained so far by the offset:
                        currViewingDistance *= offset;
                    }
                }

                // look UP
                for (int u = i - 1; u >= 0; u--) {
                    offset = i - u;
                    if (currTree <= trees[u][j]) { // if we see tree same height or taller, then this is the END of the viewing distance in this direction:
                        // calc distance by multiplying the one obtained so far by the offset:
                        currViewingDistance *= offset;
                        // stop looking UP:
                        break;
                    }
                    if (u == 0) { // last iteration of the look UP
                        // calc distance by multiplying the one obtained so far by the offset:
                        currViewingDistance *= offset;
                    }
                }

                // look DOWN
                for (int d = i + 1; d < arrLength; d++) {
                    offset = d - i;
                    if (currTree <= trees[d][j]) { // if we see tree same height or taller, then this is the END of the viewing distance in this direction:
                        // calc distance by multiplying the one obtained so far by the offset:
                        currViewingDistance *= offset;
                        // stop looking to the RIGHT:
                        break;
                    }
                    if (d == arrLength - 1) { // last iteration of the look DOWN
                        // calc distance by multiplying the one obtained so far by the offset:
                        currViewingDistance *= offset;
                    }
                }
//                logger.info("Current tree [{}][{}] viewing distance is '{}'", i, j, currViewingDistance);
                
                // store top viewing distance:
                if (currViewingDistance > maxViewingDistance) {
                    maxViewingDistance = currViewingDistance;
                    logger.info("Top distance increased = [{}]", maxViewingDistance);
                }
//                logger.info("Top distance so far = [{}]", maxViewingDistance);
            }
        }


        logger.info("    Part 2 solution:\n What is the highest scenic score possible for any tree= [{}]"
                , maxViewingDistance);
    }

}
