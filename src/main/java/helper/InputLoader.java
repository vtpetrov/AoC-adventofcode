package helper;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

public class InputLoader {

    @Getter
    private static Scanner mainIn;

    public static void loadInput(String fileName, String delimiter) {

        final Path inputFile = FileSystems.getDefault().getPath("src", "main", "resources", fileName);

        try {
            mainIn = new Scanner(inputFile).useDelimiter(delimiter);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("Couldn't load input file [" + inputFile + "]");
        }

    }

    public static void closeInput() {
        mainIn.close();
    }

}
