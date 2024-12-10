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
    private static List<Integer> fileMapWithIDs;

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInput();
        List<Integer> rawDiskMap = Stream.of(inputLines.getFirst().split("")).map(Integer::parseInt).toList();
        log.debug("raw              :{}", rawDiskMap);
        fileMapWithIDs = convertRawToFileIdMap(rawDiskMap);
        log.debug("fileMapWithIDs   :{}", fileMapWithIDs);
        log.debug("files      : {}", files);
        log.debug("free spaces: {}", eligibleFreeSpaces);
        solvePartOne();

        logStartP2();
        solvePartTwo();

        closeInput();
        logEndP2();
    }

    private static final int DOT = -1;
    private static int freeSpaceBlocks = 0;
    private static final List<MyDiskEntity> files = new ArrayList<>();
    private static final List<MyDiskEntity> eligibleFreeSpaces = new ArrayList<>();

    private static void solvePartOne() {
        List<Integer> reArrangedFileMapP1 = defragmentFileMapP1(fileMapWithIDs);
        log.debug("reArrangedFileMap P1:{}", reArrangedFileMapP1);
        log.debug("files P1            : {}", files);
        log.debug("free spaces P1      : {}", eligibleFreeSpaces);

        solutionP1 = calculateChecksum(reArrangedFileMapP1);

        log.info("""
                Part 1 solution:
                 What is the resulting filesystem checksum?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1, '\''));
    }

    private static List<Integer> convertRawToFileIdMap(List<Integer> rawDiskMap) {
        List<Integer> fileMapWithIDs = new ArrayList<>();
        for (int rawListIdx = 0; rawListIdx < rawDiskMap.size(); rawListIdx++) {
            boolean isFile = rawListIdx % 2 == 0;
            int sizeInBlocks = rawDiskMap.get(rawListIdx);
            int fileId = rawListIdx / 2;

            fileMapWithIDs.addAll(Collections.nCopies(sizeInBlocks, isFile ? fileId : DOT));

            if (isFile) {
                // store in file list:
                MyDiskEntity fileToAdd = new MyDiskEntity(fileId, sizeInBlocks, fileMapWithIDs.size() - 1, true);
                files.add(fileToAdd);
            } else {
                freeSpaceBlocks += sizeInBlocks;
                // store in free space list if size > 0:
                if (sizeInBlocks > 0) {
                    MyDiskEntity freeSpaceToAdd = new MyDiskEntity(eligibleFreeSpaces.size(), sizeInBlocks, fileMapWithIDs.size() - 1, false);
                    eligibleFreeSpaces.add(freeSpaceToAdd);
                }
            }
        }

        return fileMapWithIDs;
    }

    private static Long calculateChecksum(List<Integer> reArrangedFileMap) {
        // blocks' position multiplied by its file ID number, the checksum is the sum of these
        long checksum = 0;
        int toIdx = getLastNonFreeIdx(reArrangedFileMap);
        for (int i = 0; i <= toIdx; i++) {
            Integer blockValue = reArrangedFileMap.get(i);
            if (blockValue != DOT) {
                checksum += (long) blockValue * (long) i;
            }
        }

        return checksum;
    }

    private static List<Integer> defragmentFileMapP1(List<Integer> fileMapWithIDs) {
        List<Integer> listToReturn = new ArrayList<>(fileMapWithIDs);

        // the last non-empty element (!= -1)
        int idxToMove = getLastNonFreeIdx(listToReturn);
        int valueToMove = listToReturn.get(idxToMove); // the element's value
        int idxToFill = listToReturn.indexOf(DOT); // target index (= firstFreeSpaceIdx)

        while ((listToReturn.size() - freeSpaceBlocks) != idxToFill) {
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
        List<Integer> reArrangedDiskMapP2 = defragmentFileMapP2(fileMapWithIDs);
        log.debug("originalMap      : {}", fileMapWithIDs);
        log.debug("reArrangedDiskMap: {}", reArrangedDiskMapP2);

        solutionP2 = calculateChecksum(reArrangedDiskMapP2);
        log.info("""
                Part 2 solution:
                 Start over, now compacting the amphipod's hard drive using this new method instead.
                 What is the resulting filesystem checksum?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2, '\''));

    }

    private static List<Integer> defragmentFileMapP2(List<Integer> originalFileMap) {
        List<Integer> reArrangedDiskMapP2 = new ArrayList<>(originalFileMap);
        ArrayList<MyDiskEntity> notAttemptedFiles = new ArrayList<>(files);

        while (!notAttemptedFiles.isEmpty()) {
            // pick the last File from the non-attempted list:
            MyDiskEntity fileToMoveLeft = notAttemptedFiles.getLast();
            // return the LEFTMOST beginIndex of a free space where the file fits OR -1 if it doesn't fit.
            // FS should be to the LEFT of the file itself!
            int fromIdx = reArrangedDiskMapP2.indexOf(DOT);
            int toIdx = fileToMoveLeft.getBeginIdx() - 1;
            MyDiskEntity freeSpaceToFitTheFileTo = findFreeSpaceForFile(fileToMoveLeft, fromIdx, toIdx);
            // if it doesn't fit, REMOVE it from the notAttemptedFiles, it won't move (stays where is atm);
            if (freeSpaceToFitTheFileTo != null) { // if it FITS:
                boolean fullMatch = freeSpaceToFitTheFileTo.getSizeInBlocks() == fileToMoveLeft.getSizeInBlocks();

                // 1. swap the F with the FS in the reArrangedDiskMapP2:
                for (int targetIdx = freeSpaceToFitTheFileTo.getBeginIdx(); targetIdx < fileToMoveLeft.getSizeInBlocks() + freeSpaceToFitTheFileTo.getBeginIdx(); targetIdx++) {
                    //set the F to target:
                    reArrangedDiskMapP2.set(targetIdx, fileToMoveLeft.getId());
                }
                for (int sourceIdx = fileToMoveLeft.getBeginIdx(); sourceIdx <= fileToMoveLeft.getEndIdx(); sourceIdx++) {
                    //set the FS to source:
                    reArrangedDiskMapP2.set(sourceIdx, DOT);
                }

                // 2. update the info of the FS entity (size, begin index):
                // if full match, remove the FS completely
                if (fullMatch) {
                    eligibleFreeSpaces.remove(freeSpaceToFitTheFileTo);
                } else {// if partial match, adjust the info -> SIZE, beginIdx (endIdx remains unchanged!)
                    int fsNewSize = freeSpaceToFitTheFileTo.getSizeInBlocks() - fileToMoveLeft.getSizeInBlocks();
                    int fsNewBeginIdx = freeSpaceToFitTheFileTo.getEndIdx() - (fsNewSize - 1);
                    freeSpaceToFitTheFileTo.setSizeInBlocks(fsNewSize);
                    freeSpaceToFitTheFileTo.setBeginIdx(fsNewBeginIdx);
                }
            }
            //remove the file once processed (it either moved /above/ OR remains where it was)
            notAttemptedFiles.removeLast();
            // remove all free spaces that are to the right of the processed file:
            eligibleFreeSpaces.removeIf(elem -> elem.getBeginIdx() > fileToMoveLeft.getBeginIdx());
        }

        return reArrangedDiskMapP2;
    }

    /**
     * return the LEFTMOST beginIndex of a free space where the file fits OR -1 if it doesn't fit.
     * <br/>FS should be to the LEFT of the file itself!
     *
     * @param fileToMoveLeft the file to search a place for
     * @param from           the left most valid index of FS (inclusive)
     * @param to             the right most valid index of FS (inclusive)
     * @return the FS found OR null if there is no such space
     */
    private static MyDiskEntity findFreeSpaceForFile(MyDiskEntity fileToMoveLeft, int from, int to) {
        MyDiskEntity matchingFs = null;
        for (MyDiskEntity targetFs : eligibleFreeSpaces) {
            if (targetFs.getBeginIdx() >= from
                    && targetFs.getSizeInBlocks() >= fileToMoveLeft.getSizeInBlocks()
            ) {
                matchingFs = targetFs;
                break;
            } else if (targetFs.getBeginIdx() > to) {
                // stop traversing FSs if we reached one that is to the right of the valid area:
                break;
            }
        }

        return matchingFs;
    }
}
