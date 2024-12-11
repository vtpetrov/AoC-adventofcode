package vtpaoc.y2017.day15;

import java.util.Date;


public class DuelingGenerators {

    private static final int A_INITIAL_VALUE = 634;
    private static final int B_INITIAL_VALUE = 301;
    private static final long MULTIPLIER_A = 16807;
    private static final long MULTIPLIER_B = 48271;
    private static final long DIVIDER = 2147483647;
    private static final int CYCLES_P1 = 40000000;
    private static final int CYCLES_P2 = 5000000;
    private static long generatorA = A_INITIAL_VALUE;
    private static long generatorB = B_INITIAL_VALUE;
    private static int judges = 0;
    private static int matches = 0;


    /**
     * P1:
     * <p>
     * To create its next value, a generator will take the previous value it produced,
     * multiply it by a factor (generator A uses 16807; generator B uses 48271),
     * and then keep the remainder of dividing that resulting product by 2147483647.
     * That final remainder is the value it produces next.
     * To get a significant sample, the judge would like to consider 40 million pairs. (In the example above, the judge would eventually find a total of 588 pairs that match in their lowest 16 bits.)
     * <p>
     * After 40 million pairs, what is the judge's final count?
     * <p>
     * <p>
     * P2:
     * <p>
     * the judge is getting impatient; it is now only willing to consider 5 million pairs.
     * After 5 million pairs, but using this new generator logic, what is the judge's final count?
     *
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        System.out.println("----   ADVENT Of code   2017    ----");
        long start = new Date().getTime();
        long mid;
        long end;
        System.out.println("\n:::START = " + start);
        System.out.println("\n                ---=== Day 15 ===---     ");
        System.out.println("\n    ---=== Part 1  ===---     \n");

        for (int i = 1; i <= CYCLES_P1; i++) {

            if (((generatorA ^ generatorB) & 65535) == 0) {
                matches++;
            }

            generatorA = (generatorA * MULTIPLIER_A) % DIVIDER;
            generatorB = (generatorB * MULTIPLIER_B) % DIVIDER;

        }

        System.out.println("    Part 1:   matches = " + matches);

        // Start part 2
        generatorA = A_INITIAL_VALUE;
        generatorB = B_INITIAL_VALUE;
        matches = 0;

        while (judges < CYCLES_P2) {
            generateAp2();
            generateBp2();

            if (judge()) {
                matches++;
                mid = new Date().getTime();
                System.out.println("MID: " + (mid - start) / 1000 + " sec..");
            }
            System.out.println("judges =  " + judges);
            System.out.println("matches = " + matches);

        }

        System.out.println();
        System.out.println("judges = " + judges);
        System.out.println("    Part 2:   matches = " + matches);

        end = new Date().getTime();
        System.out.println("\n:::END = " + end);
        System.out.println("\nDuration: " + (end - start) + " milliseconds");

    }

    private static boolean judge() {
        judges++;
//        System.out.println(judges + "  judge -> ((generatorA ^ generatorB) & 65535) = " + ((generatorA ^ generatorB) & 65535));
        return (((generatorA ^ generatorB) & 65535) == 0);
    }

    private static void generateAp2() {
//        System.out.println("    DEBUG: generateAp2...");
        int a = 0;
        do {
            a++;
//            System.out.println("    " + a + ",    generatorA = " + generatorA + ", reminder= " + (generatorA * MULTIPLIER_A) % DIVIDER + " , modulo= " + (generatorA * MULTIPLIER_A) / DIVIDER);
            generatorA = (generatorA * MULTIPLIER_A) % DIVIDER;
        } while ((generatorA % 4) != 0);
//        System.out.println("    (generatorA % 4) = " + (generatorA % 4));
    }

    private static void generateBp2() {
        System.out.println();
//        System.out.println("    DEBUG: generateBp2...");
        do {

//            System.out.println("    " + b + ",    generatorB = " + generatorB + " reminder= " + (generatorB * MULTIPLIER_B) % DIVIDER + " modulo= " + (generatorB * MULTIPLIER_B) / DIVIDER);
            generatorB = (generatorB * MULTIPLIER_B) % DIVIDER;
        } while ((generatorB % 8) != 0);
//        System.out.println("    (generatorB % 4) = " + (generatorB % 4));
    }
}
