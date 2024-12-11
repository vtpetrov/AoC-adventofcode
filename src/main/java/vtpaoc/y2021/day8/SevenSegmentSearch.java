package vtpaoc.y2021.day8;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Predicate;

import static vtpaoc.helper.InputLoader.*;

public class SevenSegmentSearch {

    private static final Logger logger = LoggerFactory.getLogger(SevenSegmentSearch.class.getSimpleName());
    private static final String INPUT_FILE_NAME = "year_2021/day8_input.txt";
//    private static final String INPUT_FILE_NAME = "debug.txt";

    public static void main(String[] args) {
        logger.info("----   ADVENT Of code   2021    ----");
        long start = new Date().getTime();
        logger.info(":::START = " + LocalDateTime.ofEpochSecond(start / 1000, 0, ZoneOffset.ofHours(2)));
        logger.info("                ---=== Day 8 ===---     ");
        logger.info("              - Seven Segment Search -     ");

        logger.info("    ---=== Part 1 ===---     ");

        solvePartOneAndTwo();

        long p2Start = new Date().getTime();
        logger.info("P1 Duration: " + (p2Start - start) + "ms (" + (p2Start - start) / 1000 + "s)");

        closeInput();

        long end = new Date().getTime();
        logger.info("P2 Duration: " + (end - p2Start) + "ms (" + (end - p2Start) / 1000 + "s)");
        logger.info("==========");
        logger.info("Total Duration: " + (end - start) + "ms (" + (end - start) / 1000 + "s)");

        logger.info(":::END = " + end);
        logger.info(":::END = " + LocalDateTime.ofEpochSecond(end / 1000, 0, ZoneOffset.ofHours(2)));
    }

    private static void solvePartOneAndTwo() {

        int easyCounter = 0;
        long totalOutputSum = 0L;
        loadInput(INPUT_FILE_NAME, "");
        while (getMainIn().hasNextLine()) {
            String line = getMainIn().nextLine();
            String inputSegmentsPart = line.substring(0, line.indexOf(" |"));
            String[] inputNumbers = inputSegmentsPart.split(" ", 10);
            String outputPart = line.substring(line.indexOf("| ") + 2);
            String[] outputNumbers = outputPart.split(" ", 4);

//            length 2, 3, 4 and 7:
            Predicate<String> length2 = x -> x.length() == 2;
            Predicate<String> length3 = x -> x.length() == 3;
            Predicate<String> length4 = x -> x.length() == 4;
            Predicate<String> length7 = x -> x.length() == 7;
            Predicate<String> easyNumber = s -> length2.or(length3).or(length4).or(length7).test(s);

            easyCounter += Arrays.stream(outputNumbers).filter(easyNumber).count();

            // P2 starts here, work with inputSegments:
            final String ONE = "cf"; //2
            final String FOUR = "bcdf"; //4
            final String SEVEN = "acf"; // 3
            final String EIGHT = "abcdefg"; //7
            SegmentalDigit currentSegmentalDigit = new SegmentalDigit();
        }

        logger.info("    Part 1 solution:" +
                "\n In the output values, how many times do digits 1, 4, 7, or 8 appear?= [{}]", easyCounter);


        logger.info("    Part 2 solution:\n " +
                "For each entry, determine all of the wire/segment connections and decode the four-digit output values." +
                " What do you get if you add up all of the output values?= [{}]", "<solution_goes_here>");
    }

    @Data
    private static class SegmentalDigit {

        private String topSegment = "a";
        private String upperLeftSegment = "b";
        private String upperRightSegment = "c";
        private String middleSegment = "d";
        private String bottomLeftSegment = "e";
        private String bottomRightSegment = "f";
        private String bottomSegment = "g";

        //        ab: 1, cd, ac, gd
        public void transformSegmentsForDigitOne(String newOneString) {
            this.upperRightSegment = newOneString.split("")[0];
            this.bottomRightSegment = newOneString.split("")[1];
        }

        //        dab: 7
        public void transformSegmentsForDigitSeven(String newOneString) {
            this.upperRightSegment = newOneString.split("")[0];
            this.bottomRightSegment = newOneString.split("")[1];
        }
    }
}
// ecfdbg decfba aegd fdcag fagecd gd gcafb efdac cbgeafd dfg

// 1: gd
//      xxxx        xxxx
//     x    g      x    d
//     x    g      x    d
//      xxxx        xxxx
//     x    d      x    g
//     x    d      x    g
//      xxxx        xxxx
// 7: dfg
//      ffff        ffff
//     x    g      x    d
//     x    g      x    d
//      xxxx        xxxx
//     x    d      x    g
//     x    d      x    g
//      xxxx        xxxx
// 4: aegd
//      ffff      ffff        ffff      ffff
//     a    g    e    g      e    d    a    d
//     a    g    e    g      e    d    a    d
//      eeee      aaaa        aaaa      eeee
//     x    d    x    d      x    g    x    g
//     x    d    x    d      x    g    x    g
//      xxxx      xxxx        xxxx      xxxx
// 8: cbgeafd
//      ffff        ffff
//     a    g      e    d
//     a    g      e    d
//      eeee        aaaa
//     c    d      b    g
//     c    d      b    g
//      bbbb        cccc
// fdcag (5 segment ?) =>
//      ffff        ffff
//     a    g      e    d
//     a    g      e    d
//      eeee        aaaa
//     c    d      b    g
//     c    d      b    g
//      bbbb        cccc


//   aaaa         dddd
//  b    c       e    a
//  b    c       e    a
//   dddd         ffff
//  e    f       g    b
//  e    f       g    b
//   gggg         cccc

