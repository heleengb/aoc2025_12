package software.ulpgc.aoc.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ZoneSolver {

    public boolean isSolvable(ForestZone zone, Map<Integer, TimberShape> dictionary) {
        List<TimberShape> toPlace = new ArrayList<>();
        int areaSum = 0;

        for (Map.Entry<Integer, Integer> entry : zone.getRequirements().entrySet()) {
            TimberShape shape = dictionary.get(entry.getKey());
            for (int i = 0; i < entry.getValue(); i++) {
                toPlace.add(shape);
                areaSum += shape.getArea();
            }
        }

        if (areaSum > zone.getTotalArea()) return false;

        // Ordenar por área descendente para optimizar backtracking
        toPlace.sort(Comparator.comparingInt(TimberShape::getArea).reversed());

        boolean[][] grid = new boolean[zone.getHeight()][zone.getWidth()];
        return backtrack(grid, toPlace, 0);
    }

    private boolean backtrack(boolean[][] grid, List<TimberShape> shapes, int idx) {
        if (idx >= shapes.size()) return true;

        TimberShape current = shapes.get(idx);
        int H = grid.length;
        int W = grid[0].length;

        for (TimberShape variant : current.getVariations()) {
            // Intentar colocar en cada posición libre
            for (int r = 0; r < H; r++) {
                for (int c = 0; c < W; c++) {
                    if (fits(grid, variant, r, c)) {
                        place(grid, variant, r, c, true);
                        if (backtrack(grid, shapes, idx + 1)) return true;
                        place(grid, variant, r, c, false);
                    }
                }
            }
        }
        return false;
    }

    private boolean fits(boolean[][] grid, TimberShape s, int r, int c) {
        if (r + s.getHeight() > grid.length || c + s.getWidth() > grid[0].length) return false;
        boolean[][] mask = s.getMask();
        for (int i = 0; i < s.getHeight(); i++) {
            for (int j = 0; j < s.getWidth(); j++) {
                if (mask[i][j] && grid[r + i][c + j]) return false;
            }
        }
        return true;
    }

    private void place(boolean[][] grid, TimberShape s, int r, int c, boolean val) {
        boolean[][] mask = s.getMask();
        for (int i = 0; i < s.getHeight(); i++) {
            for (int j = 0; j < s.getWidth(); j++) {
                if (mask[i][j]) grid[r + i][c + j] = val;
            }
        }
    }
}