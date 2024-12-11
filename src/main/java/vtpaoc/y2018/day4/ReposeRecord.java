package vtpaoc.y2018.day4;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static vtpaoc.helper.InputLoader.*;

public class ReposeRecord {

    private static final String INPUT_FILE_NAME = "year_2018/day4_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    private static Map<String, String> inputRows = new TreeMap<>();
    private static Map<Integer, Guard> guards = new HashMap<>();

    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2018    ----");
        long start = new Date().getTime();
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 4 ===---     ");
        System.out.println("                 - Repose Record -     ");

        System.out.println("\n    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");

        int currGuardId = -1;
        int minuteFallAsleep = -1;
        int minuteWakeUp = -1;
        boolean awake = false;

        while (getMainIn().hasNextLine()) {

            String inputLine = getMainIn().nextLine();
            inputRows.put(inputLine.substring(0,inputLine.indexOf("]")),
                    inputLine.substring(inputLine.indexOf("]")));
        }

        for (Map.Entry<String, String> curr : inputRows.entrySet()) {
            String currLine = curr.getKey() + curr.getValue();
            //switch currentGuard
            if (currLine.contains("begins shift")) {
                currGuardId = Integer.parseInt(currLine.substring(currLine.indexOf("#") + 1, currLine.indexOf("begins") - 1));
                guards.putIfAbsent(currGuardId, new Guard(currGuardId));
                awake = false;
            }

            // time fall asleep
            if (currLine.contains("falls asleep")) {
                minuteFallAsleep = Integer.parseInt(currLine.substring(currLine.indexOf("]") - 2, currLine.indexOf("]")));
                awake = false;
            }

            // time wake up
            if (currLine.contains("wakes up")) {
                minuteWakeUp = Integer.parseInt(currLine.substring(currLine.indexOf("]") - 2, currLine.indexOf("]")));
                awake = true;
            }

            if (awake) {
                int sleepDuration = minuteWakeUp - minuteFallAsleep;
                guards.get(currGuardId).addSleep(sleepDuration);
                guards.get(currGuardId).updateSleepMap(minuteFallAsleep, minuteWakeUp);
            }
        }


    partOne();


    long p2Start = new Date().getTime();
        System.out.println("\n\nP1 Duration: "+(p2Start -start)+"ms ("+(p2Start -start)/1000+"s)");

        System.out.println("=========================================================================================");

    partTwo();

    closeInput();


    long end = new Date().getTime();
        System.out.println("\nP2 Duration: "+(end -p2Start)+"ms ("+(end -p2Start)/1000+"s)");
        System.out.println("==========");
        System.out.println("\nTotal Duration: "+(end -start)+"ms ("+(end -start)/1000+"s)");

        System.out.println("\n:::END = "+end);
}

    private static void partOne() {

        int maxSleepTime = Integer.MIN_VALUE;
        int pospalankoId = -1;

        for (Guard curr : guards.values()) {
            if (curr.getTotalMinutesAsleep() > maxSleepTime) {
                maxSleepTime = curr.getTotalMinutesAsleep();
                pospalankoId = curr.getId();
            }
        }

        System.out.println("maxSleepTime = " + maxSleepTime);
        System.out.println("pospalankoId = " + pospalankoId);
        System.out.println("guards.get(pospalankoId).getMostSleepingMinute() = " + guards.get(pospalankoId).getMostSleepingMinute());

        System.out.println("\n    Part 1 solution:   the ID of the guard you chose multiplied by the minute you chose= " +
                pospalankoId * guards.get(pospalankoId).getMostSleepingMinute());

    }

    private static void partTwo() {

        int maxMinuteValue = -1;
        int maxMinuteIdx = -1;
        int guardIdS2 = -1;

        for(Guard currGuard : guards.values()){

            int currMinuteIndex = currGuard.getMostSleepingMinute();
            int currMinuteValue = currGuard.getSleepMap()[currMinuteIndex];
            if( currMinuteValue > maxMinuteValue){
                maxMinuteValue = currMinuteValue;
                maxMinuteIdx = currMinuteIndex;
                guardIdS2 = currGuard.getId();
            }

        }

        System.out.println("maxMinuteIdx = " + maxMinuteIdx);
        System.out.println("maxMinuteValue = " + maxMinuteValue);
        System.out.println("guardIdS2 = " + guardIdS2);

        System.out.println("\n    Part 2 solution:   the ID of the guard you chose multiplied by the minute you chose= " +
        guardIdS2 * maxMinuteIdx);
    }

}

@Getter
@Setter
@ToString
class Guard {

    int id;
    int totalMinutesAsleep;
    int[] sleepMap;

    Guard(int id) {
        this.id = id;
        this.totalMinutesAsleep = 0;
        this.sleepMap = new int[60];
    }

    public void addSleep(int sleepDuration) {
        this.totalMinutesAsleep += sleepDuration;
    }

    public void updateSleepMap(int minuteFallAsleep, int minuteWakeUp) {

        for (int i = minuteFallAsleep; i < minuteWakeUp; i++) {
            this.sleepMap[i]++;
        }
    }

    /**
     *
     * @return minute index
     */
    public int getMostSleepingMinute() {
        int max = -1;
        int maxI = -1;

        for (int i = 0; i <= 59; i++) {
            if (this.sleepMap[i] > max) {
                max = this.sleepMap[i];
                maxI = i;
            }
        }

        return maxI;
    }
}