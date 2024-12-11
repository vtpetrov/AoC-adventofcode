package vtpaoc.helper;

public class NumberUtilsVtp {

    /**
     * Calculates the number of digits in a given number, up to a maximum of 19 digits.
     * <br/> {@code Long.MAX_VALUE= 9223372036854775807, (9'223'372'036'854'775'807)}
     */
    public static int findNumberLength(long number) {
        if (number < 100000) { // 1 to 5
            if (number < 100) { // 1 or 2
                if (number < 10) {
                    return 1;
                }
                return 2;
            } else { // 3, 4, or 5
                if (number < 1000) {
                    return 3;
                }
                if (number < 10000) {
                    return 4;
                }
                return 5;
            }
        } else if (number < 1000000000) { // 6 to 9
            if (number < 1000000) { // 6
                return 6;
            }
            if (number < 10000000) { // 7
                return 7;
            }
            if (number < 100000000) { // 8
                return 8;
            }
            return 9; // 9
        } else if (number < 100000000000000L) { // 10 to 14
            if (number < 10000000000L) { // 10
                return 10;
            }
            if (number < 100000000000L) { // 11
                return 11;
            }
            if (number < 1000000000000L) { // 12
                return 12;
            }
            if (number < 10000000000000L) { // 13
                return 13;
            }
            return 14; // 14
        } else { // 15 to 18
            if (number < 1000000000000000L) { // 15
                return 15;
            }
            if (number < 10000000000000000L) { // 16
                return 16;
            }
            if (number < 100000000000000000L) { // 17
                return 17;
            } else {
                if (number < 1000000000000000000L) { // 18
                    return 18;
                } else {
                    return 19; // Long.MAX_VALUE= 9223372036854775807, (9'223'372'036'854'775'807)
                }
            }
        }
    }
}
