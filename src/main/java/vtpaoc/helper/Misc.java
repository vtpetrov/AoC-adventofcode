package vtpaoc.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Misc {

    public static void sleepSeconds(final int sleepTimeSeconds) {
        try {
            Thread.sleep(sleepTimeSeconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an element from a Collection at a specified position.
     * It returns null if the Collection is empty.
     *
     * @param actualCollection the Collection that is expected for an element if present
     * @param indexAt          the position at which is the searched element
     */
    public static <T> T getElementSafe(final Collection<T> actualCollection, final int indexAt) {
        ArrayList<T> actualCollectionOfElements = new ArrayList<>(actualCollection);
        try {
            return actualCollectionOfElements.get(indexAt);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Formats the map for printing
     *
     * @param mapToPrint the map to format
     * @return a String containing a line for each key-value
     */
    public static String prettyPrintMap(final Map<?, ?> mapToPrint) {
        if (mapToPrint.isEmpty()) {
            return "none";
        }
        StringBuilder sb = new StringBuilder();
        int maxKeyLength = mapToPrint.keySet().stream()
                .map(String::valueOf)
                .mapToInt(String::length)
                .max()
                .orElse(0);

        String format = "%n%" + maxKeyLength + "s: %s";
        mapToPrint.forEach((key, value) -> sb.append(String.format(format, key, value)));
        return sb.toString();
    }

    /**
     * Formats a given collection into a human-readable string representation. If the collection
     * is null or empty, it returns "[null or empty]". Otherwise, the elements of the collection
     * are converted into a map with each element assigned a unique index, and the map is formatted
     * using the prettyPrintMap method.
     *
     * @param listToPrint the collection to format
     * @return a formatted string representation of the collection
     */
    public static String prettyPrintList(final Collection<?> listToPrint) {
        if (listToPrint == null || listToPrint.isEmpty()) {
            return "[null or empty]";
        }
        // convert the list to Map, then use our prettyPrintMap() method:
        AtomicInteger i = new AtomicInteger(0);
        final Map<Integer, ?> map = listToPrint.stream()
                .collect(Collectors.toMap(n -> i.incrementAndGet(), element -> element));

        return prettyPrintMap(map);
    }

    /**
     * Converts an object into a map and formats it into a human-readable string representation.
     * The object is transformed into a map using Jackson's ObjectMapper and then formatted
     * using the prettyPrintMap method.
     *
     * @param obj the object to be converted and formatted
     * @return a formatted string representation of the object
     */
    public static String prettyPrintObject(final Object obj) {
        ObjectMapper oMapper = new ObjectMapper();

        Map map = oMapper.convertValue(obj, Map.class);
        return prettyPrintMap(map);
    }

    /**
     * Converts a two-dimensional array into a formatted string representation.
     * Each element in the array is appended to the output, optionally separated by a specified delimiter.
     * Rows are separated by a newline character.
     *
     * @param arrayToPrint the two-dimensional array to format
     * @param useDelimiter a boolean flag indicating whether to use the specified delimiter between elements
     * @param delimiter the string to use as a delimiter between elements if useDelimiter is true
     * @return a string representation of the two-dimensional array
     */
    public static <T> String prettyPrintTwoDimensArray(T[][] arrayToPrint, boolean useDelimiter, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (T[] objects : arrayToPrint) {
            for(Object elem : objects) {
                result.append(elem);
                if (useDelimiter){
                    result.append(delimiter);
                }
            }
            result.append('\n');
        }
        return result.toString();
    }

    /**
     * Formats a given number into a human-readable string representation, using a specified
     * character as the thousand separator.
     *
     * @param num the number to format; can be of any type extending Number
     * @param thousandSeparator the character to use as the separator for thousands
     * @return a string representation of the number with the specified thousand separator
     */
    public static String prettyPrintNumber(Number num, char thousandSeparator) {
        DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance();
        DecimalFormatSymbols decimalFormatSymbols = numberFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);
        numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(num);
    }

    /**
     * Formats a number into a human-readable string with a default separator ('\'') for thousands.
     *
     * @param num the number to format
     * @return a string representation of the number with thousands indicated by the default separator
     */
    public static String prettyPrintNumber(Number num) {
        return prettyPrintNumber(num, '\'');
    }
}
