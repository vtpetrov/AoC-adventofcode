package vtpaoc.y2023.day3;

import lombok.extern.slf4j.Slf4j;
import vtpaoc.base.BaseDay;
import vtpaoc.helper.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class GearRatios extends BaseDay {

        static final int schemaSize = 140;

    private static Character[][] engineSchema = new Character[schemaSize][schemaSize];
    private static List<Integer> enginePartsP1;

    static {
        inputFileName = "year_2023/day03_input.txt";
    }

    static String year = "2023";
    static String day = "03";
    static String puzzleTitle = "Gear Ratios";

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInputLines();

        drawEngineSchematic();

        solvePartOne();

        logStartP2();

        solvePartTwo();

        closeInput();
        logEndP2();
    }

    private static void drawEngineSchematic() {
        for (int i = 0; i < schemaSize; i++) {
            engineSchema[i] = inputLines.get(i).chars().mapToObj(elem -> (char) elem).toArray(Character[]::new);
        }
        log.info("engine schema loaded:\n{}", Misc.prettyPrintTwoDimensArray(engineSchema, true, " "));
    }

    private static void solvePartOne() {
        enginePartsP1 = identifyEngineParts(engineSchema);
        // sum engine parts and get the solution:
        solutionP1 = enginePartsP1.stream().mapToInt(Integer::intValue).sum();

        log.info("""
                Part 1 solution:
                 What is the sum of all of the part numbers in the engine schematic?
                       = [{}]""", prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static List<Integer> identifyEngineParts(Character[][] engineSchema) {
        // find each number (1/2/3 digits in a row, which touch  symbol (anything not a digit or . /dot/)
        List<Integer> engineParts = new ArrayList<>();
        List<Integer> numberIdxs = new ArrayList<>();
        String numberAsString = "";
        int rowIndex;
        boolean hasNumberToCheck = false;

        for (int i = 0; i < schemaSize; i++) {
            rowIndex = i;
            for (int j = 0; j < schemaSize; j++) {
                // if digit, start building a "number"
                Character currChar = engineSchema[i][j];
                if (Character.isDigit(currChar)) {
                    hasNumberToCheck = true;
                    numberAsString += currChar;
                    numberIdxs.add(j);
                } else {// if not a digit, stop building number, check for touching symbols
                    if (hasNumberToCheck) {
                        hasNumberToCheck = false;
                        // check area above, same and below row, and 1 index to the left and right from the number region:
                        for (int k = Math.max(0, rowIndex - 1); k < Math.min(schemaSize, rowIndex + 2); k++) {
                            for (int l = Math.max(0, numberIdxs.getFirst() - 1); l < Math.min(schemaSize, numberIdxs.getLast() + 2); l++) {
                                if (isPartSymbol(engineSchema[k][l])) {
                                    // mark number area as engine part
                                    engineParts.add(Integer.parseInt(numberAsString));
                                    k = l = schemaSize; // break the search
                                }
                            }
                        }
                        // reset number area and indexes:
                        numberIdxs.clear();
                        numberAsString = "";
                    }
                }
            }
        }
        return engineParts;
    }

    private static boolean isPartSymbol(Character character) {// if digit or '.', not a symbol, else is a symbol
        return !Character.isDigit(character) && character != '.';
    }

    private static void solvePartTwo() {

        Map<String, Gear> allGears = identifyGears(engineSchema);
        List<Gear> validGears = allGears.values().stream().filter(Gear::isValidGear).toList();

        solutionP2 = validGears
                .stream()
                .map(Gear::getRatio)
                .mapToLong(Long::longValue)
                .sum();

        log.info("""
                Part 2 solution:
                 What is the sum of all of the gear ratios in your engine schematic?
                       = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));
    }

    private static Map<String, Gear> identifyGears(Character[][] engineSchema) {
        // find each number (1/2/3 digits in a row, which touch  symbol (anything not a digit or . /dot/)
        Map<String, Gear> gears = new HashMap<>();
        List<Integer> engineParts = new ArrayList<>();
        List<Integer> numberIdxs = new ArrayList<>();
        String numberAsString = "";
        int rowIndex;
        boolean hasNumberToCheck = false;

        for (int i = 0; i < schemaSize; i++) {
            rowIndex = i;
            for (int j = 0; j < schemaSize; j++) {
                // if digit, start building a "number"
                Character currChar = engineSchema[i][j];
                if (Character.isDigit(currChar)) {
                    hasNumberToCheck = true;
                    numberAsString += currChar;
                    numberIdxs.add(j);
                } else {// if not a digit, stop building number, check for touching symbols
                    if (hasNumberToCheck) {
                        hasNumberToCheck = false;
                        int partNumber = Integer.parseInt(numberAsString);
                        // check area above, same and below row, and 1 index to the left and right from the number region:
                        for (int k = Math.max(0, rowIndex - 1); k < Math.min(schemaSize, rowIndex + 2); k++) {
                            for (int l = Math.max(0, numberIdxs.getFirst() - 1); l < Math.min(schemaSize, numberIdxs.getLast() + 2); l++) {
                                // check if GEAR
                                String gearKey = String.valueOf(k + 1000) + String.valueOf(l + 1000);
                                if (isGearSymbol(engineSchema[k][l])) {
                                    // add number to Gear parts, by checking ger row,column (map key)
                                    if (gears.containsKey(gearKey)) { // gear already added, add this number to it's parts
                                        gears.get(gearKey).parts.add(partNumber);
                                    } else { // gear not added, add it with first part this number
                                        gears.put(gearKey, new Gear(new ArrayList<>(List.of(partNumber))));
                                    }
                                    engineParts.add(partNumber); // leave this here just in case
                                }
                            }
                        }
                        // reset number area and indexes:
                        numberIdxs.clear();
                        numberAsString = "";
                    }
                }
            }
        }
        return gears;
    }

    private static boolean isGearSymbol(Character character) {// if '*' it is a symbol
        return character == '*';
    }


}
