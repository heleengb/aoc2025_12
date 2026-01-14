package software.ulpgc.aoc;

import software.ulpgc.aoc.controller.FarmController;
import software.ulpgc.aoc.io.FileSourceReader;
import software.ulpgc.aoc.view.SolutionDisplay;

import java.nio.file.Path;

public class Main {
    private static final Path INPUT_PATH = Path.of("src", "main", "resources", "presents.txt");

    public static void main(String[] args) {
        try {
            // IO
            var rawData = new FileSourceReader(INPUT_PATH).readContent();

            // CONTROLLER
            long result = new FarmController().processSolvableRegions(rawData);

            // VIEW
            new SolutionDisplay().display("Regiones Válidas", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}