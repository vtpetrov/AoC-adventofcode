package vtpaoc.helper;

import org.apache.commons.lang3.Range;

public class Ranges {

    private Ranges() {
        throw new IllegalStateException("Utility class 'Ranges'");
    }

    /**
     * Determines where a given {@code current} range is positioned compared to another {@code of} range.
     * <br/>{@code current[2, 5]} is to the left of {@code of[6, 10]}
     * <br/>{@code current[5, 7]} is NOT to the left of {@code of[1, 2]}
     *
     * @param current range to compare
     * @param of      the range to compare against
     * @return true if {@code current} is to the left of {@code of}, false otherwise
     */
    public static boolean isLeftOf(Range<Long> current, Range<Long> of) {
        return current.getMaximum() < of.getMinimum();
    }

    public static Range<Long> merge(Range<Long> rangeOne, Range<Long> rangeTwo) {
        long newNextRangeMin = Math.min(rangeOne.getMinimum(), rangeTwo.getMinimum());
        long newNextRangeMax = Math.max(rangeOne.getMaximum(), rangeTwo.getMaximum());
        Range<Long> newRange = Range.of(newNextRangeMin, newNextRangeMax);
        return newRange;
    }

}
