package vtpaoc.y2024.day14;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;
import vtpaoc.y2018.day6.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintList;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class ClawContraption extends BaseDay {

    static String year = "2024";
    static String day = "13";
    static String puzzleTitle = "Claw Contraption";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//         inputFileName = "debug.txt";
    }

    static List<ClawMachine> clawMachinesP1;
    static final int BTN_A_COST = 3;
    static final int BTN_B_COST = 1;

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
//        loadDayInputLines();
        loadDayInputWhole();
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {
        clawMachinesP1 = parseClawMachinesInput();
        log.info("All claw machines: {}", prettyPrintList(clawMachinesP1));

        solutionP1 = clawMachinesP1.stream()
                .map(ClawMachine::calcLowestPriceToWinPrizeP1).mapToInt(Integer::intValue).sum();

        log.info("""
                Part 1 solution:
                 Figure out how to win as many prizes as possible.
                 What is the fewest tokens you would have to spend to win all possible prizes?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static void solvePartTwo() {

        log.info("""
                Part 2 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }

    private static List<ClawMachine> parseClawMachinesInput() {
        ArrayList<ClawMachine> parsedClawMachines = new ArrayList<>();
        Pattern machineData = Pattern.compile("""
                Button A: X\\+(\\d+), Y\\+(\\d+)
                Button B: X\\+(\\d+), Y\\+(\\d+)
                Prize: X=(\\d+), Y=(\\d+)""");

        Matcher singleMachineMatcher = machineData.matcher(inputWhole);
        while (singleMachineMatcher.find()) {
            ClawMachine clawMachine = getClawMachine(singleMachineMatcher);
            parsedClawMachines.add(clawMachine);
        }
        return parsedClawMachines;
    }

    private static ClawMachine getClawMachine(Matcher singleMachine) {
        int xA = Integer.parseInt(singleMachine.group(1));
        int yA = Integer.parseInt(singleMachine.group(2));
        int xB = Integer.parseInt(singleMachine.group(3));
        int yB = Integer.parseInt(singleMachine.group(4));
        int xPrize = Integer.parseInt(singleMachine.group(5));
        int yPrize = Integer.parseInt(singleMachine.group(6));

        return new ClawMachine(new Location(xA, yA), new Location(xB, yB), new Location(xPrize, yPrize));
    }
}
