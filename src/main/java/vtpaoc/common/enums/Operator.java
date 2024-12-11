package vtpaoc.common.enums;

public enum Operator {
    GT,
    LT,
    GTOET,
    LTOET,
    EQUAL_TO,
    NOT_EQUAL_TO,
    PLUS,
    MULTIPLY;

    public static Operator fromString(String input) {
        switch (input) {
            case ">":
                return GT;
            case "<":
                return LT;
            case ">=":
                return GTOET;
            case "<=":
                return LTOET;
            case "==":
                return EQUAL_TO;
            case "!=":
                return NOT_EQUAL_TO;
            case "+":
                return PLUS;
            case "*":
                return MULTIPLY;
            default:
                throw new Error("VTP - unknown operator:" + input);
        }
    }
}
