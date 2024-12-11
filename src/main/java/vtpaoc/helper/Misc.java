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
        mapToPrint.forEach((key, value) -> sb.append(String.format("%n%s: %s", key, value)));
        return sb.toString();
    }

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

    public static String prettyPrintObject(final Object obj) {
        ObjectMapper oMapper = new ObjectMapper();

        Map map = oMapper.convertValue(obj, Map.class);
        return prettyPrintMap(map);
    }

    public static <T> String prettyPrintTwoDimensArray(T[][] arrayToPrint, Boolean useDelimiter, String delimiter) {
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

    public static String prettyPrintNumber(Number num, char thousandSeparator) {
        DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance();
        DecimalFormatSymbols decimalFormatSymbols = numberFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);
        numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(num);
    }
}
