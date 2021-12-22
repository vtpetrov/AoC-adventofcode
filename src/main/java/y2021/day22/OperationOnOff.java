package y2021.day22;

public enum OperationOnOff {
    OFF,
    ON;

    public static OperationOnOff fromString(String input) {
        if ("on".equalsIgnoreCase(input)) {
            return ON;
        } else {
            return OFF;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case ON:
                return "on";
            case OFF:
                return "off";
        }
        return null;
    }

    /**
     * convert operation to number
     * <ul>
     *     <li>ON => 1</li>
     *     <li>OFF => 0</li>
     * </ul>
     *
     * @return the respective number
     */
    public int toNumber() {
        switch (this) {
            case ON:
                return 1;
            case OFF:
                return 0;
        }
        return -1;
    }
}
