package software.ulpgc.aoc.model;

import java.util.Map;

public class ForestZone {
    private final int width;
    private final int height;
    private final Map<Integer, Integer> requirements;

    public ForestZone(int width, int height, Map<Integer, Integer> requirements) {
        this.width = width;
        this.height = height;
        this.requirements = requirements;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Map<Integer, Integer> getRequirements() { return requirements; }
    public int getTotalArea() { return width * height; }
}