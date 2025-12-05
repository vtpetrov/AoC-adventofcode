package vtpaoc.y2017.day9;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static vtpaoc.helper.InputUtils.*;

public class StreamProcessing {
    private static final String INPUT_FILE_NAME = "year_2017/day9_input.txt";
    //            private static final String INPUT_FILE_NAME = "debug.txt";
    private static final String GROUP_START = "{";
    private static final String GROUP_END = "}";
    private static final String GARBAGE_START = "<";
    private static final String GARBAGE_END = ">";
    private static final String SKIP = "!";
    private static long groups = 0;
    private static long totalScore = 0;
    private static boolean inStream;
    private static boolean inGarbage;
    private static boolean skipNext;
    private static boolean inGroup = false;
    private static List<String> skippedList = new ArrayList<>();
    private static int garbageCharCount = 0;

    private static void switchContext() {
        inGarbage = inStream;
        inStream = !inGarbage;
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 9 ===---     ");
        System.out.println("                - StreamProcessing -     ");
        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        inStream = true;
        inGarbage = false;

        String ch = null;
        String skipped = null;
        int cost = 1;

        while (getMainIn().hasNext()) {

            if (!skipNext) { // process:
                ch = getMainIn().next();

                if (inGarbage) { // in garbage
                    switch (ch) {
                        case SKIP:
                            skipNext = true;
                            break;
                        case GARBAGE_END:
                            switchContext();
                            break;
                        default:
                            garbageCharCount++;
                    }
                } else { // in stream
                    switch (ch) {
                        case GARBAGE_START:
                            switchContext();
                            break;
                        case GARBAGE_END:
                            switchContext();
                            break;
                        case GROUP_START:
                            inGroup = true;
                            groups++;
                            totalScore += cost;
                            cost++;
                            break;
                        case GROUP_END:
                            if (inGroup) cost--;
                            break;
                    }
                }

            } else { // skip
                skipped = getMainIn().next();
                skippedList.add(skipped);
                skipNext = false;
            }

        }

        System.out.println("\nGROUPS        = " + groups);
        System.out.println("TOTALSCORE      = " + totalScore);
        System.out.println("garbageCharCount = " + garbageCharCount);

        System.out.println("\nskippedList = " + skippedList);

        long end = new Date().getTime();
        System.out.println("\n:::END = " + end);
        System.out.println("\nDuration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        closeInput();

    }
}
