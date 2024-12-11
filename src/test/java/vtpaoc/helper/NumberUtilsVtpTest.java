package vtpaoc.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class NumberUtilsVtpTest {

    @ParameterizedTest(name = "{index} findNumberLength() returns correct for {0}")
    @MethodSource(value = "provideStringsLength1to18")
    void findNumberLength(String input) {
        log.debug("input= {}", input);
        int expectedLen  = input.length();
        int actualLen = NumberUtilsVtp.findNumberLength(Long.parseLong(input));
        log.debug("expected value= {}", expectedLen);
        log.debug("actual   value= {}", actualLen);
        assertEquals(expectedLen, actualLen, "Actual length doesn't match expected");
    }

    @Test
    @DisplayName(value = "findNumberLength returns 19 for Long.MAX_VALUE= 9223372036854775807, (9'223'372'036'854'775'807)")
    void findNumberLengthMaxLong() {
        int expectedLen  = 19;
        long inputMaxLong = Long.MAX_VALUE;
        log.debug("input= {}", inputMaxLong);
        int actualLen = NumberUtilsVtp.findNumberLength(inputMaxLong);
        log.debug("expected value= {}", expectedLen);
        log.debug("actual   value= {}", actualLen);

        assertEquals(expectedLen, actualLen, "Actual length doesn't match expected");
    }

    public static Stream<Arguments> provideStringsLength1to18() {
        List<String> numbersAsStr = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            numbersAsStr.add(RandomStringUtils.secure().nextNumeric(i, i).replace("0", "1"));
        }
        return numbersAsStr.stream().map(Arguments::of);
    }
}