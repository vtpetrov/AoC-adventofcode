package y2021.day6;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static helper.InputLoader.closeInput;
import static helper.InputLoader.getMainIn;
import static helper.InputLoader.loadInput;

public class LanternFish {

    private static final Logger logger = LoggerFactory.getLogger(LanternFish.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2021/day6_input.txt";
    //    private static final String INPUT_FILE_NAME = "debug.txt";
    private static final List<Integer> fish = new ArrayList<>();


    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 06 ===---     ");
        logger.info("            - --- Day 6: Lanternfish ---      ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, ",");
        do {
            final String readFromInput = getMainIn().next();
            fish.add(Integer.parseInt(readFromInput.trim()));
        } while (getMainIn().hasNext());

        closeInput();
        solve();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");
        long end = new Date().getTime();
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solve() {
        int day = 1;
        final int endDay = 256;
//        Each day, a 0 becomes a 6 and adds a new 8 to the end of the list, while each other
//        number decreases by 1 if it was present at the start of the day.
        logger.info("Initial state: {}", fish);
        Map<Integer, Long> accumulators
                = Maps.newHashMap(ImmutableMap.of(
                0, 0L,
                1, 0L,
                2, 0L,
                3, 0L,
                4, 0L,
                5, 0L,
                6, 0L,
                7, 0L,
                8, 0L));
        //initialize array (directly to save memory):
        for (Integer curFish : fish) {
            accumulators.put(curFish, accumulators.get(curFish) + 1);
        }
        fish.clear();

        long fishCount;
        do {
            // day start
            long toSpawn = accumulators.get(0);

            for (int key = 1; key <= 8; key++) {
                accumulators.put(key - 1, accumulators.get(key));
            }

            // SPAWN: // increase 6s by the count of 0s; // make 8s = of 0s,
            logger.info("Spawn");
            accumulators.put(6, accumulators.get(6) + toSpawn);
            accumulators.put(8, toSpawn);

            fishCount = accumulators.values().stream().reduce(0L, Long::sum);
            logger.info("After {} days: total= {} , added= {}", day, fishCount, toSpawn);
            if (day == 80) {
                logger.info("    Part 1 solution:" +
                        "\n How many lanternfish would there be after 80 days? = [{}]", fishCount);
            }
            day++;
        } while (day <= endDay);

        logger.info("    Part 2 solution:" +
                "\n How many lanternfish would there be after 256 days? = [{}]", fishCount);
    }

}
