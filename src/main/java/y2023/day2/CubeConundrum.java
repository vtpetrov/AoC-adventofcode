package y2023.day2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static helper.InputLoader.*;

public class CubeConundrum {

    private static final Logger logger = LoggerFactory.getLogger(CubeConundrum.class.getSimpleName());
        private static final String INPUT_FILE_NAME = "year_2023/day02_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    static List<String> inputLines = new ArrayList<>();
    private static String solution;


    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2023    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 02 ===---     ");
        logger.info("                  - Cube Conundrum -     ");

        logger.info("    ---=== Part 1 ===---     ");

        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            inputLines.add(line);
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

    static Map<String, Integer> thresholdsPart1 = Map.of(Colors.RED.value, 12, Colors.GREEN.value, 13, Colors.BLUE.value, 14);
    static List<Game> games = new ArrayList<>();
    static int sumValidGameIdsPart1 = 0;

    private static void solvePartOne() {

        for (int i = 0; i < inputLines.size(); i++) {

            String lineInput = inputLines.get(i);

            logger.info("lineInput= {}", lineInput);

            List<String> gamePulls = Arrays.stream(lineInput.split(";")).toList();

            List<Game.Pull> currGamePulls = new ArrayList<>();

            for (String gamePull : gamePulls) {
                Pattern numberColorPairs = Pattern.compile("(\\d+) (red|green|blue)");
                Matcher pairsMatcher = numberColorPairs.matcher(gamePull);

                Map<String, Integer> currentGamePullColorNumberPairs = new HashMap<>();

                logger.info("================================ gamePull= {}", gamePull);
                while (pairsMatcher.find()) {
//                logger.info("-------------------------------- {}", pairsMatcher.group());
//                logger.info("pairsMatcher.group(0)= {}", pairsMatcher.group(0));// whole pair
//                logger.info("pairsMatcher.group(1)= {}", pairsMatcher.group(1));// number
//                logger.info("pairsMatcher.group(2)= {}", pairsMatcher.group(2));// color

                    currentGamePullColorNumberPairs.put(pairsMatcher.group(2), Integer.valueOf(pairsMatcher.group(1)));
                }

                Game.Pull currentPull = new Game.Pull();
                currentPull.colorNumberPairs.putAll(currentGamePullColorNumberPairs);

                currGamePulls.add(currentPull);
            }

            Game currGame = new Game();
            currGame.setPulls(currGamePulls);

            boolean isCurrGameValid = determineGameValidity(currGame);
            currGame.setIsValid(isCurrGameValid);

            logger.info("currGame isValid= {}", currGame.isValid);
            logger.info("currGame= {}", currGame.pulls);

            games.add(currGame);
        }

        for (int i = 0; i < games.size(); i++) {
            sumValidGameIdsPart1 += games.get(i).isValid ? i + 1 : 0;
        }

        logger.info("    Part 1 solution:\n What is the sum of the IDs of valid games?= [{}]", sumValidGameIdsPart1);

    }

    private static boolean determineGameValidity(Game game) {
        boolean isGameValid = true;
        for (Game.Pull pull : game.getPulls()) {
            if (pull.getColorNumberPairs().get(Colors.RED.value) > thresholdsPart1.get(Colors.RED.value) ||
                    pull.getColorNumberPairs().get(Colors.GREEN.value) > thresholdsPart1.get(Colors.GREEN.value) ||
                    pull.getColorNumberPairs().get(Colors.BLUE.value) > thresholdsPart1.get(Colors.BLUE.value)) {
                pull.setIsValid(false);
                isGameValid = false;
            } else {
                pull.setIsValid(true);
            }
        }
        return isGameValid;
    }

    private static void solvePartTwo() {


        logger.info("    Part 2 solution:\n YYYYYYYYYYYY= [{}]", "<solution_goes_here>");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Game {
        Boolean isValid;
        List<Pull> pulls = new ArrayList<>();

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        private static class Pull {
            //            Map<String, Integer> colorNumberPairs = new HashMap<>();
            Map<String, Integer> colorNumberPairs = new HashMap<>(Map.of(Colors.RED.value, 0, Colors.GREEN.value, 0, Colors.BLUE.value, 0));
            Boolean isValid;
        }
    }

    private static enum Colors {
        RED("red"),
        GREEN("green"),
        BLUE("blue");


        private final String value;

        Colors(String value) {
            this.value = value;
        }
    }
}
