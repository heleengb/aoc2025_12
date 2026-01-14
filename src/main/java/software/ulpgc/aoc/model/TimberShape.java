package software.ulpgc.aoc.model;

import java.util.*;

public class TimberShape {
    private final int id;
    private final boolean[][] mask;
    private final int height;
    private final int width;
    private final int area;
    // Ahora las variaciones son finales y se calculan al inicio
    private final List<TimberShape> variations;

    public TimberShape(int id, boolean[][] rawMask) {
        this.id = id;
        this.mask = trim(rawMask);
        this.height = this.mask.length;
        this.width = this.height > 0 ? this.mask[0].length : 0;
        this.area = calculateArea();
        // Calculamos las variaciones una sola vez al crear el objeto
        this.variations = computeVariations();
    }

    // Constructor privado para las variaciones internas
    private TimberShape(int id, boolean[][] mask, boolean isVariation) {
        this.id = id;
        this.mask = mask; // Ya viene recortada por la rotación
        this.height = mask.length;
        this.width = this.height > 0 ? mask[0].length : 0;
        this.area = calculateArea();
        this.variations = new ArrayList<>(); // Las variaciones no necesitan sus propias variaciones
        this.variations.add(this);
    }

    private boolean[][] trim(boolean[][] input) {
        if (input.length == 0) return input;

        int minRow = input.length, maxRow = -1;
        int minCol = Integer.MAX_VALUE, maxCol = -1;
        boolean empty = true;

        for (int r = 0; r < input.length; r++) {
            if (input[r] == null) continue;
            for (int c = 0; c < input[r].length; c++) {
                if (input[r][c]) {
                    empty = false;
                    if (r < minRow) minRow = r;
                    if (r > maxRow) maxRow = r;
                    if (c < minCol) minCol = c;
                    if (c > maxCol) maxCol = c;
                }
            }
        }

        if (empty) return new boolean[0][0];

        int newH = maxRow - minRow + 1;
        int newW = maxCol - minCol + 1;
        boolean[][] res = new boolean[newH][newW];

        for (int r = 0; r < newH; r++) {
            for (int c = 0; c < newW; c++) {
                // Protección contra arrays irregulares
                int srcRow = minRow + r;
                int srcCol = minCol + c;
                if (srcRow < input.length && srcCol < input[srcRow].length) {
                    res[r][c] = input[srcRow][srcCol];
                } else {
                    res[r][c] = false;
                }
            }
        }
        return res;
    }

    private List<TimberShape> computeVariations() {
        List<TimberShape> list = new ArrayList<>();
        Set<String> fingerprints = new HashSet<>();

        // Usamos una instancia temporal para rotar
        TimberShape current = this;

        for (int i = 0; i < 4; i++) {
            addUnique(current, list, fingerprints);
            addUnique(current.flip(), list, fingerprints);
            current = current.rotate();
        }
        return Collections.unmodifiableList(list);
    }

    private void addUnique(TimberShape s, List<TimberShape> list, Set<String> fps) {
        String fp = s.toFingerprint();
        if (!fps.contains(fp)) {
            fps.add(fp);
            list.add(s);
        }
    }

    public TimberShape rotate() {
        int newH = width;
        int newW = height;
        boolean[][] newMask = new boolean[newH][newW];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                newMask[c][height - 1 - r] = mask[r][c];
            }
        }
        // Usamos el constructor privado para no recalcular variaciones de la variación
        return new TimberShape(id, newMask, true);
    }

    public TimberShape flip() {
        boolean[][] newMask = new boolean[height][width];
        for (int r = 0; r < height; r++) {
            newMask[r] = Arrays.copyOf(mask[r], width);
            for(int i = 0; i < width / 2; i++) {
                boolean temp = newMask[r][i];
                newMask[r][i] = newMask[r][width - i - 1];
                newMask[r][width - i - 1] = temp;
            }
        }
        return new TimberShape(id, newMask, true);
    }

    private int calculateArea() {
        int count = 0;
        for (boolean[] row : mask) {
            for (boolean cell : row) if (cell) count++;
        }
        return count;
    }

    private String toFingerprint() {
        StringBuilder sb = new StringBuilder();
        for (boolean[] row : mask) {
            for (boolean b : row) sb.append(b ? '1' : '0');
            sb.append('|');
        }
        return sb.toString();
    }

    public int getId() { return id; }
    public boolean[][] getMask() { return mask; }
    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public int getArea() { return area; }

    public List<TimberShape> getVariations() {
        return variations;
    }
}