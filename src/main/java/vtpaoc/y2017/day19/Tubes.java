package vtpaoc.y2017.day19;

import java.util.*;
import java.util.stream.Collectors;

import static vtpaoc.helper.InputLoader.getMainIn;
import static vtpaoc.helper.InputLoader.loadInput;

public class Tubes {
    private static final String INPUT_FILE_NAME = "year_2017/day19_input.txt";
    //        private static final String INPUT_FILE_NAME = "debug.txt";
    private static final int MAP_SIZE = 201;
    private static final String H_PATH = "-";
    private static final String V_PATH = "|";
    private static final String CROSS = "+";
    private static final List<String> PATH_ELEMS = Arrays.asList(H_PATH, V_PATH, CROSS, " ", null);
    private static final List<String> OUT_OF_PATH_ELEMS = Arrays.asList(" ", null);
    private static List<String> PATH_LETTERS = new ArrayList<>();
    private static String[][] map = new String[MAP_SIZE][MAP_SIZE];
    private static int i;
    private static int j;
    private static int moves;
    private static Direction currentDirection;
    private static String currentPathElem;


    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 19 ===---     ");
        System.out.println("                - A Series of Tubes -     ");
        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        String line;
        i = j = 0;

        // initialize map (read from input):
        while (getMainIn().hasNextLine()) {
            line = getMainIn().nextLine();
            map[i] = line.split("");
            i++;
        }

        //initialize starting point, find starting 'j':
        i = 0;
        j = Arrays.binarySearch(map[i], "|");

        currentPathElem = map[i][j];
        currentDirection = Direction.DOWN;

        do {

            move();

            currentPathElem = getMapElem(i, j);

            if (!PATH_ELEMS.contains(currentPathElem)) { // letter:
                PATH_LETTERS.add(currentPathElem);
            }

        } while (!endOfPath());

        System.out.println("\n    Part 1 solution:   letters = " + PATH_LETTERS.stream().collect(Collectors.joining()));

        System.out.println("\n    ---=== Part 2 ===---     ");
        System.out.println("\n    Part 2 solution:   moves = " + moves);


        long end = new Date().getTime();
        System.out.println("\n:::END = " + end);
        System.out.println("\nDuration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");
    }


    private static String getMapElem(int x, int y) {
        String temp;
        try {
            temp = map[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            temp = null;
        }

        return temp;
    }

    private static void move() {
        moves++;

        switch (currentDirection) {
            case DOWN:
                switch (currentPathElem) {
                    case CROSS: // turn LEFT or RIGHT:
                        turn();
                        break;
                    default:
                        moveDown();
                }
                break;
            case UP:
                switch (currentPathElem) {
                    case CROSS: // turn LEFT or RIGHT:
                        turn();
                        break;
                    default:
                        moveUp();
                }
                break;
            case LEFT:
                switch (currentPathElem) {
                    case CROSS: // turn LEFT or RIGHT:
                        turn();
                        break;
                    default:
                        moveLeft();
                }
                break;
            case RIGHT:
                switch (currentPathElem) {
                    case CROSS: // turn LEFT or RIGHT:
                        turn();
                        break;
                    default:
                        moveRight();
                }
                break;
        }
    }

    /**
     * call moveX based on currentDirection and possible turn
     */
    private static void turn() {
        // set new currentDirection and call moveX
        Turn possibleTurn = getPossibleTurn();

        if (currentDirection.equals(Direction.UP)) {
            if (Objects.equals(possibleTurn, Turn.LEFT)) {
                moveLeft();
            }
            if (Objects.equals(possibleTurn, Turn.RIGHT)) {
                moveRight();
            }
        } else if (currentDirection.equals(Direction.LEFT)) {
            if (Objects.equals(possibleTurn, Turn.LEFT)) {
                moveDown();
            }
            if (Objects.equals(possibleTurn, Turn.RIGHT)) {
                moveUp();
            }
        } else if (currentDirection.equals(Direction.DOWN)) {
            if (Objects.equals(possibleTurn, Turn.LEFT)) {
                moveRight();
            }
            if (Objects.equals(possibleTurn, Turn.RIGHT)) {
                moveLeft();
            }
        } else if (currentDirection.equals(Direction.RIGHT)) {
            if (Objects.equals(possibleTurn, Turn.LEFT)) {
                moveUp();
            }
            if (Objects.equals(possibleTurn, Turn.RIGHT)) {
                moveDown();
            }
        }
    }

    /**
     * Determine turning where we can turn.
     *
     * @return
     */
    private static Turn getPossibleTurn() {
        // start clockwise:
        switch (currentDirection) {
            case UP:
                if (!OUT_OF_PATH_ELEMS.contains(getMapElem(i, j - 1))) {
                    return Turn.LEFT;
                } else if (!OUT_OF_PATH_ELEMS.contains(getMapElem(i, j + 1))) {
                    return Turn.RIGHT;
                } else {
                    throw new Error("   [VTP] no way U!!!");
                }
            case DOWN:
                if (!OUT_OF_PATH_ELEMS.contains(getMapElem(i, j - 1))) {
                    return Turn.RIGHT;
                } else if (!OUT_OF_PATH_ELEMS.contains(getMapElem(i, j + 1))) {
                    return Turn.LEFT;
                } else {
                    throw new Error("   [VTP] no way D!!!");
                }
            case LEFT:
                if (!OUT_OF_PATH_ELEMS.contains(getMapElem(i + 1, j))) {
                    return Turn.LEFT;
                } else if (!OUT_OF_PATH_ELEMS.contains(getMapElem(i - 1, j))) {
                    return Turn.RIGHT;
                } else {
                    throw new Error("   [VTP] no way L!!!");
                }
            case RIGHT:
                if (!OUT_OF_PATH_ELEMS.contains(getMapElem(i + 1, j))) {
                    return Turn.RIGHT;
                } else if (!OUT_OF_PATH_ELEMS.contains(getMapElem(i - 1, j))) {
                    return Turn.LEFT;
                } else {
                    throw new Error("   [VTP] no way R!!!");
                }
        }
        throw new Error("   [VTP] no way G !!!");
    }

    private static boolean endOfPath() {
        if (currentPathElem == null || currentPathElem.equals(" ")) {
            return true;
        } else return false;
    }

    private static void moveLeft() {
        j--;
        currentDirection = Direction.LEFT;
    }

    private static void moveRight() {
        j++;
        currentDirection = Direction.RIGHT;
    }

    private static void moveUp() {
        i--;
        currentDirection = Direction.UP;
    }

    private static void moveDown() {
        i++;
        currentDirection = Direction.DOWN;
    }

    enum Direction {

        UP, LEFT, DOWN, RIGHT

    }

    enum Turn {

        RIGHT, LEFT
    }
}


