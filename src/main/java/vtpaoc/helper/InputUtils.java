package vtpaoc.helper;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static vtpaoc.helper.Misc.prettyPrintTwoDimensArray;

@Slf4j
public class InputUtils {

    private static Scanner mainIn;

    public static void loadInput(String fileName, String delimiter) {

        final Path inputFile = FileSystems.getDefault().getPath("src", "main", "resources", fileName);

        try {
            mainIn = (new Scanner(inputFile).useDelimiter(delimiter));
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("Couldn't load input file [" + inputFile + "]");
        }

    }

    public static Scanner getMainIn() {
        return mainIn;
    }

    public static void closeInput() {
        mainIn.close();
    }

    public static Character[][] convertInputToTowDimmensArrOfChars(List<String> inputLines, int schemaSize, boolean printSchema) {
        Character[][] inputSchema = new Character[schemaSize][schemaSize];
        for (int i = 0; i < schemaSize; i++) {
            inputSchema[i] = inputLines.get(i).chars().mapToObj(elem -> (char) elem).toArray(Character[]::new);
        }
        if (printSchema) {
            log.info("input schema loaded:\n{}", prettyPrintTwoDimensArray(inputSchema, true, " "));
        }

        return inputSchema;
    }
}
