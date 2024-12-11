package vtpaoc.y2022.day5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static vtpaoc.helper.InputLoader.*;

public class SupplyStacks {

    public static final Pattern MOVE_ROW_PATTERN = Pattern.compile("move (\\d{1,2}) from (\\d) to (\\d)");
    private static final Logger logger = LoggerFactory.getLogger(SupplyStacks.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2022/day05_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static String solution;

    private static final List<Stack<String>> stacks = new ArrayList<>(); // 3 stacks of crates
    private static int dividerLineIdx;
    private static Integer stacksCount;
//    After the rearrangement procedure completes, what crate ends up on top of each stack?

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2022    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 05 ===---     ");
        logger.info("                  - Supply Stacks -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
        }

        parseInput();
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

    /**
     * Move crates 1 by 1, following instructions like:
     * move 1 from 2 to 1
     * move 3 from 1 to 3
     * move 2 from 2 to 1
     * move 1 from 1 to 2
     */
    private static void moveCratesP1() {
        // Start moving crates 

        logger.info("Start moving crates P1 ..................");
        // loop moving lines
        for (int l = dividerLineIdx + 1; l < inputLines.size(); l++) {
            int moveCount, fromStack, toStack;

            String movingLine = inputLines.get(l);
            Matcher m = MOVE_ROW_PATTERN.matcher(movingLine);
            if (m.find()) {
                moveCount = Integer.parseInt(m.group(1));
                fromStack = Integer.parseInt(m.group(2));
                toStack = Integer.parseInt(m.group(3));
            } else {
                throw new Error("No matches found in [" + movingLine + "] by matcher: " + MOVE_ROW_PATTERN);
            }

//            logger.info("Moving crates: {} crates from stack {} to stack {}", moveCount, fromStack, toStack);

            // for each line, do as many moves as the text "move X" suggests => 'moveCount'
            for (int cratesToMove = 0; cratesToMove < moveCount; cratesToMove++) {
                // move crate from one stack to another
                String popped = stacks.get(fromStack - 1).pop();

//                logger.info("Moving crate {} from stack {} to stack {}", popped, fromStack, toStack);
                stacks.get(toStack - 1).push(popped);
//                logger.info("Stacks after: {}", stacks);

            }
        }
        logger.info("Final Stacks state (after moving per Part 1): {}", stacks);
        logger.info("--------------------------------------------------------------------");
    }

    /**
     * Move multiple crates at once, following instructions like:
     * move 1 from 2 to 1
     * move 3 from 1 to 3 (move 3 crates at once)
     * move 2 from 2 to 1 (move 2 crates at once)
     * move 1 from 1 to 2
     */
    private static void moveCratesP2() {
        // Start moving crates 

        logger.info("Start moving crates P2 ..................");
        // loop moving lines
        for (int l = dividerLineIdx + 1; l < inputLines.size(); l++) {
            int moveCount, fromStack, toStack;

            String movingLine = inputLines.get(l);
            Matcher m = MOVE_ROW_PATTERN.matcher(movingLine);
            if (m.find()) {
                moveCount = Integer.parseInt(m.group(1));
                fromStack = Integer.parseInt(m.group(2));
                toStack = Integer.parseInt(m.group(3));
            } else {
                throw new Error("No matches found in [" + movingLine + "] by matcher: " + MOVE_ROW_PATTERN);
            }

//            logger.info("Moving crates: {} crates from stack {} to stack {}", moveCount, fromStack, toStack);

            List<String> tempList = new ArrayList<>();
            // for each line, do ONLY 1 many by adding all crates to be moved into a collection and then addAll to target stack
            for (int cratesToMove = 0; cratesToMove < moveCount; cratesToMove++) {
                // collect crates to be moved into a temp List
                String popped = stacks.get(fromStack - 1).pop();
                tempList.add(0, popped);
            }
            // push the collected list to the target stack:
//            logger.info("Moving crate(s) {} from stack {} to stack {}", tempList, fromStack, toStack);
            stacks.get(toStack - 1).addAll(tempList);
//            logger.info("Stacks after: {}", stacks);
        }

        logger.info("Final Stacks state (after moving per Part 2): {}", stacks);
        logger.info("--------------------------------------------------------------------");
    }


    /**
     * Initialize globals 'dividerLineIdx' and 'stacksCount' based on the input in the following format:
     * <pre>
     * [G]                 [D] [R]
     * [W]         [V]     [C] [T] [M]
     * [L]         [P] [Z] [Q] [F] [V]
     * [J]         [S] [D] [J] [M] [T] [V]
     * [B]     [M] [H] [L] [Z] [J] [B] [S]
     * [R] [C] [T] [C] [T] [R] [D] [R] [D]
     * [T] [W] [Z] [T] [P] [B] [B] [H] [P]
     * [D] [S] [R] [D] [G] [F] [S] [L] [Q]
     *  1   2   3   4   5   6   7   8   9
     *
     * move 1 from 3 to 5
     * move 5 from 5 to 4
     * move 6 from 7 to 3
     * </pre>
     * ..... and so on til EOF
     */
    private static void parseInput() {
        //1 find number of stacks, row after the empty row:
        for (int l = 0; l < inputLines.size(); l++) {
            if (inputLines.get(l).isEmpty()) {
                dividerLineIdx = l;
                break;
            }
        }

        //2 find the count stacks count:
        String[] idxs = "  ".concat(inputLines.get(dividerLineIdx - 1)).concat("  ").split("   ");
        idxs[0] = "0";
        stacksCount = Arrays.stream(idxs).map(Integer::parseInt).max(Integer::compareTo).get();
    }

    /**
     * Put stacks into initial state (before moving starts)
     * <p>
     * [G]                 [D] [R]
     * [W]         [V]     [C] [T] [M]
     * [L]         [P] [Z] [Q] [F] [V]
     * [J]         [S] [D] [J] [M] [T] [V]
     * [B]     [M] [H] [L] [Z] [J] [B] [S]
     * [R] [C] [T] [C] [T] [R] [D] [R] [D]
     * [T] [W] [Z] [T] [P] [B] [B] [H] [P]
     * [D] [S] [R] [D] [G] [F] [S] [L] [Q]
     * 1   2   3   4   5   6   7   8   9
     */
    private static void initializeStacks() {
        logger.info("Initializing stacks....");
        stacks.clear();
        // add N empty stacks to the stack list
        for (int s = 0; s < stacksCount; s++) {
            stacks.add(new Stack<>());
        }
        // fill in initial state of the stacks
        // Start from the bottom of the pile to the top
        for (int cratesLineIdx = dividerLineIdx - 2; cratesLineIdx >= 0; cratesLineIdx--) {
            String cratesLine = inputLines.get(cratesLineIdx);

            //start traversing the row and get each 2, 6, 10, etc chars and if they match a letter, assign it as a crate in the given stack
            for (int stackIdx = 0, cursor = 1; stackIdx < stacksCount; stackIdx++, cursor += 4) {
                String charAtCursor = cratesLine.substring(cursor, cursor + 1);
                if (charAtCursor.matches("[A-Z]")) { // if it's a crate, add it to the current stack
//                    logger.info("{}, Add crate {} to stack {}", cratesLineIdx, charAtCursor, stackIdx + 1);
                    stacks.get(stackIdx).push(charAtCursor);
//                    logger.info("Stacks after: {}", stacks);
                }
            }
        }
        logger.info("Initial Stacks state (before moving)        : {}", stacks);
    }

    private static void solvePartOne() {
        initializeStacks();
        moveCratesP1();

        //collect crates from top of each stack
        solution = stacks.stream().map(Stack::pop).collect(Collectors.joining());
        logger.info("    Part 1 solution:\n What crate ends up on top of each stack? => [{}]\n", solution);
    }


    private static void solvePartTwo() {
        initializeStacks();
        moveCratesP2();

        //collect crates from top of each stack
        solution = stacks.stream().map(Stack::pop).collect(Collectors.joining());
        logger.info("    Part 2 solution:\n What crate ends up on top of each stack? ==> [{}]\n", solution);
    }

}
