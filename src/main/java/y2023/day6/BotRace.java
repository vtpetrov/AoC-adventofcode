package y2023.day6;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotRace {

    public static void main(String[] args) {
        log.info("bot race!!!!");

        // Test input
        int[] times = {40929790};
        long[] distances = {215106415051100L};

        int totalWaysToWin = calculateTotalWaysToWin(times, distances);
        System.out.println("Total ways to win: " + totalWaysToWin);
    }

    private static int calculateTotalWaysToWin(int[] times, long[] distances) {
        int totalWaysToWin = 1;

        for (int i = 0; i < times.length; i++) {
            int waysToWin = calculateWaysToWin(times[i], distances[i]);
            totalWaysToWin *= waysToWin;
        }

        return totalWaysToWin;
    }

    private static int calculateWaysToWin(int time, long distance) {
        int waysToWin = 0;

        for (int holdTime = 0; holdTime < time; holdTime++) {
            long speed = holdTime;
            long remainingTime = time - holdTime;
            long totalDistance = speed * remainingTime;

            if (totalDistance > distance) {
                waysToWin++;
            }
        }
        return waysToWin;
    }
}
