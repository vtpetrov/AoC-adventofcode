package vtpaoc.y2025.day05;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import vtpaoc.base.BaseDay;
import vtpaoc.helper.Ranges;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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

    private static void solvePartTwo() {

        // just count elems in ranges. account for overlapping ranges:
        Set<Long> uniqueElems = new HashSet<>();
        List<Range<Long>> uniqueRanges = new ArrayList<>();
        uniqueRanges.add(ranges.getFirst());

//        for (Range<Long> range : ranges) {
//            List<Long> rangeElems = LongStream.range(range.getMinimum(), range.getMaximum() + 1L).boxed().toList();
//            uniqueElems.addAll(rangeElems);

        for (int i = 1; i < ranges.size(); i++) {
            Range<Long> currentProcessing = ranges.get(i);
            //traverse all "unique" ranges, not only the last
            Range<Long> compareTo;
            for (int j = 0; j < uniqueRanges.size(); j++) {
                Range<Long> currentUnique = uniqueRanges.get(j);
                //[1..5][7..9][14..40] => [3..8]
                // check overlapping, if not check LeftOf
                boolean overlap = currentUnique.isOverlappedBy(currentProcessing);
                if (overlap) { // if they overlap, construct new range and substitute the previous one:
                    Range<Long> newRange = Ranges.merge(currentUnique, currentProcessing);
                    uniqueRanges.set(j, newRange);
                    // also check for the next elem (if any). if it overlaps the new J, merge them
                    if (j < uniqueRanges.size() - 1) {
                        boolean nextOverlap = uniqueRanges.get(j + 1).isOverlappedBy(uniqueRanges.get(j));
                        if (nextOverlap) {
                            Range<Long> newNextRange = Ranges.merge(uniqueRanges.get(j), uniqueRanges.get(j + 1));
                            uniqueRanges.set(j, newNextRange);
                            uniqueRanges.remove(j + 1);
                        }
                    }
                }
                //[1..5][7..9][14..40] => [10..12]
                else if (currentUnique.isBeforeRange(currentProcessing)) { // if they don't overlap, add the currentProcessing to the left of the currentUnique
                    //if there is another range to the right, check it's between
                    if (j < uniqueRanges.size() - 1) {
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
        log.debug("Unique ranges: {}", prettyPrintList(uniqueRanges));

        // count elems in uniqueRanges:
        long totalFreshProducts = 0;
        for (Range<Long> range : uniqueRanges) {
            totalFreshProducts += LongStream.rangeClosed(range.getMinimum(), range.getMaximum()).count();
        }

        long totalFreshProduct2 = 0;
        AtomicInteger cnt = new AtomicInteger(0);
        for (Range<Long> uniqueRange : uniqueRanges) {
            long rangeCount = uniqueRange.getMaximum() - uniqueRange.getMinimum() + 1;
            log.debug("[{}] rangeCount= {}", cnt.incrementAndGet(), rangeCount);
            totalFreshProduct2 += rangeCount;
        }
        log.debug("totalFreshProduct2= {}", totalFreshProduct2);

        solutionP2 = totalFreshProducts;

        log.info("""
                Part 2 solution:
                 How many ingredient IDs are considered to be fresh according to the fresh ingredient ID ranges?
                 = [{}] ({})""", solutionP2, prettyPrintNumber((Number) solutionP2));

    }
}
