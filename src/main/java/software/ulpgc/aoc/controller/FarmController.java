package software.ulpgc.aoc.controller;

import software.ulpgc.aoc.command.FarmOperation;
import software.ulpgc.aoc.command.ValidateZoneCommand;
import software.ulpgc.aoc.model.ForestZone;
import software.ulpgc.aoc.model.TimberShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmController {

    public long processSolvableRegions(List<String> rawData) {
        // 1. Fase de Parseo (Separar Formas de Tareas)
        ParseResult parsed = parseInput(rawData);

        // 2. Fase de Ejecución (Crear comandos y ejecutar en paralelo)
        return parsed.tasks.parallelStream()
                .map(zone -> (FarmOperation) new ValidateZoneCommand(zone, parsed.dictionary))
                .filter(FarmOperation::execute)
                .count();
    }

    // --- Lógica de Parseo interna ---
    private static class ParseResult {
        Map<Integer, TimberShape> dictionary = new HashMap<>();
        List<ForestZone> tasks = new ArrayList<>();
    }

    private ParseResult parseInput(List<String> lines) {
        ParseResult result = new ParseResult();
        int i = 0;

        // Leer Formas
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) { i++; continue; }
            if (line.matches("\\d+x\\d+:.*")) break; // Fin de formas

            if (line.matches("\\d+:")) {
                int id = Integer.parseInt(line.replace(":", ""));
                i++;
                List<boolean[]> rows = new ArrayList<>();
                while (i < lines.size() && !lines.get(i).trim().isEmpty() && !lines.get(i).matches("\\d+:")) {
                    String rowStr = lines.get(i);
                    boolean[] row = new boolean[rowStr.length()];
                    for (int c = 0; c < rowStr.length(); c++) row[c] = rowStr.charAt(c) == '#';
                    rows.add(row);
                    i++;
                }
                result.dictionary.put(id, new TimberShape(id, rows.toArray(new boolean[0][])));
            } else {
                i++;
            }
        }

        // Leer Tareas (Regiones)
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                String[] parts = line.split(":");
                String[] dims = parts[0].split("x");
                String[] reqs = parts[1].trim().split("\\s+");

                Map<Integer, Integer> reqMap = new HashMap<>();
                for (int id = 0; id < reqs.length; id++) {
                    int count = Integer.parseInt(reqs[id]);
                    if (count > 0) reqMap.put(id, count);
                }
                result.tasks.add(new ForestZone(Integer.parseInt(dims[0]), Integer.parseInt(dims[1]), reqMap));
            }
            i++;
        }
        return result;
    }
}