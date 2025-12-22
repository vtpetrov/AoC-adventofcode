package vtpaoc.y2025.day05;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import vtpaoc.base.BaseDay;
import vtpaoc.helper.Ranges;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static vtpaoc.helper.InputUtils.closeInput;
import static vtpaoc.helper.Misc.prettyPrintList;
import static vtpaoc.helper.Misc.prettyPrintNumber;

@Slf4j
public class Cafeteria extends BaseDay {

    static String year = "2025";
    static String day = "05";
    static String puzzleTitle = "Cafeteria";
    static List<Range<Long>> ranges = new ArrayList<>();
    static List<Long> products = new ArrayList<>();

    static {
        inputFileName = "year_" + year + "/day" + day + "_input.txt";
//        inputFileName = "debug.txt";
    }

    public static void main(String[] args) {
        logStartP1(year, day, puzzleTitle);
        loadDayInputLines();
        //or
//        loadDayInputWhole();

        solvePartOne();
        logStartP2();
        solvePartTwo();
        closeInput();
        logEndP2();
    }

    private static void solvePartOne() {
        boolean switchProcessing = false;

        for (String line : inputLines) {
            if (line.isBlank()) {
                switchProcessing = true;
                continue;
            }

            if (switchProcessing) { // read products
                products.add(Long.parseLong(line));
            } else { // read ranges
                String[] parts = line.split("-");
                Range<Long> range = Range.of(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                ranges.add(range);
            }
        }

        //find fresh products:
        int freshProducts = 0;
        for (Long product : products) {
            for (Range<Long> range : ranges) {
                boolean isFresh = range.contains(product);
                if (isFresh) {
                    freshProducts++;
                    break;
                }
            }
        }

        solutionP1 = freshProducts;

        log.info("""
                Part 1 solution:
                 How many of the available ingredient IDs are fresh?
                 = [{}] ({})""", solutionP1, prettyPrintNumber((Number) solutionP1));
    }

    static List<Range<Long>> uniqueRanges = new ArrayList<>();

    private static void solvePartTwo() {
        // just count elems in ranges. account for overlapping ranges:
        uniqueRanges.add(ranges.getFirst());

        for (int i = 1; i < ranges.size(); i++) {
            Range<Long> currentProcessing = ranges.get(i);
            //traverse all "unique" ranges, not only the last
            Range<Long> compareTo;
            for (int j = 0; j < uniqueRanges.size(); j++) {
                Range<Long> currentUnique = uniqueRanges.get(j);
                //[1..5][7..9][14..40] => [3..8]
                // check for overlapping and merge till the end of th unique list
                boolean overlap = currentUnique.isOverlappedBy(currentProcessing);
                if (overlap) { // if they overlap, construct new range and substitute the previous one:
                    mergeOverlappedTillTheEndOfIfAny(uniqueRanges, j, currentProcessing);
                }
                //[1..5][7..9][14..40] => [10..12]
                else if (currentUnique.isBeforeRange(currentProcessing)) { // if they don't overlap
                    //if there is another range to the right, check it's between
                    if (uniqueHasNext(j)) {
                        if (uniqueRanges.get(j + 1).isAfterRange(currentProcessing)) {
                            //if it is, fit it between j and j + 1
                            uniqueRanges.set(j + 1, currentProcessing);
                        }
                    } else { // if there is no next range, add it to the end
                        uniqueRanges.add(currentProcessing);
                        break;
                    }
                } else {
                    uniqueRanges.add(j, currentProcessing);
                    break;
                }
            }
        }
        log.debug("Unique ranges 1: {}", prettyPrintList(uniqueRanges));

        // count elems in uniqueRanges:
        solutionP2 = countElementsInRanges(uniqueRanges);

        log.info("""
                Part 2 solution:
                 How many ingredient IDs are considered to be fresh according to the fresh ingredient ID ranges?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2));


        // solution 2A
        mergeOverlappedTillTheEndOfIfAny(uniqueRanges, 1, uniqueRanges.getFirst());
        log.debug("\n\nUnique ranges 2a: {}", prettyPrintList(uniqueRanges));

        log.debug("Solution 2A: {}", countElementsInRanges(uniqueRanges));

        // solution 2b
        List<Range<Long>> ranges2b = removeDuplicateRanges(uniqueRanges);
        log.debug("\n\nUnique ranges 2b: {}", prettyPrintList(ranges2b));
        log.debug("Solution 2b: {}", countElementsInRanges(ranges2b));

    }

    private static long countElementsInRanges(List<Range<Long>> rangesToCount) {
        long totalElemCount = 0;
        for (Range<Long> range : rangesToCount) {
            totalElemCount += LongStream.rangeClosed(range.getMinimum(), range.getMaximum()).count();
        }
        return totalElemCount;
    }

    /**
     * Merges a given range with overlapping ranges in the list from the specified starting index until
     * no more overlapping ranges are found.
     *
     * @param parentListOfRanges the list of ranges to check for overlaps and perform merging
     * @param startAtIdx         the index in the list where overlapping check and merging should begin
     * @param currentToMerge     the range to merge with overlapping ranges in the list
     */
    private static void mergeOverlappedTillTheEndOfIfAny(List<Range<Long>> parentListOfRanges, int startAtIdx, Range<Long> currentToMerge) {
        boolean initial = true;
        for (int m = startAtIdx; m < parentListOfRanges.size(); m++) {
            Range<Long> parentToMerge = parentListOfRanges.get(m);
            if (parentToMerge.isOverlappedBy(currentToMerge)) {
                Range<Long> newRange = Ranges.merge(parentToMerge, currentToMerge);
                int idxToPut = Math.min(parentListOfRanges.indexOf(parentToMerge),
                        parentListOfRanges.indexOf(currentToMerge) > 0 ? parentListOfRanges.indexOf(currentToMerge) : Integer.MAX_VALUE);
                parentListOfRanges.set(idxToPut, newRange);
                log.debug("merged {} with {} at idx {} => {}", parentToMerge, currentToMerge, m, newRange);
                currentToMerge = newRange;
                if (initial) {
                    initial = false;
                } else {
                    // remove the range to the right.
                    parentListOfRanges.remove(m);
                }
                log.debug("new range to check with next one => {}", currentToMerge);
            } else {
                log.debug("no more overlapping, finish merging. Started at: {} , ended at: {}", startAtIdx, m);
                break;
            }
        }
    }

    private static List<Range<Long>> removeDuplicateRanges(List<Range<Long>> rangesWithDuplicates) {
        List<Range<Long>> noDupes = new ArrayList<>();

        //check any two ranges, merge them if they overlap, add new range back to list, remove right.
        for (int d = 0; d < rangesWithDuplicates.size() - 1; ) {// make sure there is always d + 1
            if (rangesWithDuplicates.get(d).isOverlappedBy(rangesWithDuplicates.get(d + 1))) {
                Range<Long> merged = Ranges.merge(rangesWithDuplicates.get(d), rangesWithDuplicates.get(d + 1));
                rangesWithDuplicates.set(d, merged);
                rangesWithDuplicates.remove(d + 1);
            } else {
                noDupes.add(rangesWithDuplicates.get(d));
                d++;
            }
        }

        return noDupes;
    }

    private static boolean uniqueHasNext(int currIdx) {
        return currIdx < uniqueRanges.size() - 1;
    }
}
