package y2024.day09;

import base.BaseDay;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static helper.InputLoader.closeInput;
import static helper.Misc.prettyPrintNumber;

@Slf4j
public class DiskFragmenter extends BaseDay {

    static String year = "2024";
    static String day = "09";
    static String puzzleTitle = "Disk Fragmenter";

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();
        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static final int DOT = -1;
    private static int fileBlocks = 0;
    private static int freeSpaceBlocks = 0;
    private static int filesCount = 0;

    private static void solvePartOne() {
        // file_size, free_space, file_size, free_space, ....
//        List<Integer> rawDiskMap = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> rawDiskMap = Stream.of(inputLines.getFirst().split("")).map(Integer::parseInt).toList();
        //raw       1,  2,  3,  4,  5   6   7   8
        //idx       0   1   2   3   4   5   6   7   (+1)
        //fileId    0       1       2       3
        List<Integer> fileMapWithIDs = new ArrayList<>();

        for (int rawListIdx = 0; rawListIdx < rawDiskMap.size(); rawListIdx++) {
            boolean isFile = rawListIdx % 2 == 0;
            int sizeInBlocks = rawDiskMap.get(rawListIdx);
            fileMapWithIDs.addAll(Collections.nCopies(sizeInBlocks, isFile ? rawListIdx / 2 : DOT));

            if (isFile) {
                fileBlocks += sizeInBlocks;
                filesCount++;
            } else {
                freeSpaceBlocks += sizeInBlocks;
            }
        }

        List<Integer> reArrangedFileMap = defragmentFileMap(fileMapWithIDs);

//        log.info("raw              :{}", rawDiskMap);
//        log.info("fileMapWithIDs   :{}", fileMapWithIDs);
//        log.info("reArrangedFileMap:{}", reArrangedFileMap);

        solutionP1 = calculateChecksumP1(reArrangedFileMap);

        log.info("""
                Part 1 solution:
                 What is the resulting filesystem checksum?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static Long calculateChecksumP1(List<Integer> reArrangedFileMap) {
        // blocks' position multiplied by its file ID number, , the checksum is the sum of these
        long checksum = 0;
        for (int i = 0; i < reArrangedFileMap.indexOf(DOT); i++) {
            checksum += (long) reArrangedFileMap.get(i) * (long) i;
        }

        return checksum;
    }

    private static List<Integer> defragmentFileMap(List<Integer> fileMapWithIDs) {
        List<Integer> listToReturn = new ArrayList<>(fileMapWithIDs);

        // the last non-empty element (!= -1)
        int idxToMove = getLastNonFreeIdx(listToReturn);
        int valueToMove = listToReturn.get(idxToMove); // the element's value
        int idxToFill = listToReturn.indexOf(DOT); // target index (= firstFreeSpaceIdx)

        while ((listToReturn.size() - freeSpaceBlocks) != idxToFill) {
            // while all free spaces are not moved toward the tail of the list, keep moving
            //[0, -1, -1, 1, 1, 1, -1, -1, -1, -1, 2, 2, 2, 2, 2]
            // start moving elements from the tail toward the head of the list by filling the empty (-1) spaces.
            // and placing empty space (-1) in place of the moved element
            listToReturn.set(idxToFill, valueToMove);
            listToReturn.set(idxToMove, DOT);

            idxToFill = listToReturn.indexOf(DOT);
            idxToMove = getLastNonFreeIdx(listToReturn);
            valueToMove = listToReturn.get(idxToMove);
        }

        return listToReturn;
    }

    /**
     * Finds the last index in the disk fragment map that is not marked as free.
     *
     * @param diskFragmentMap a list representing disk fragments where each
     *                        element is an integer indicating the state of a
     *                        disk fragment; a specific value (DOT) represents a free fragment
     * @return the index of the last non-free disk fragment, or -1 if no such fragment exists
     */
    private static int getLastNonFreeIdx(List<Integer> diskFragmentMap) {
        for (int i = diskFragmentMap.size() - 1; i >= 0; i--) {
            if (diskFragmentMap.get(i) != DOT) return i;
        }
        return -1;
    }

    private static void solvePartTwo() {

        log.info("""
                Part 2 solution:
                 XXXXXX
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }
}
