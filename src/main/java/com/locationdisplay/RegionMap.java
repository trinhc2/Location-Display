package com.locationdisplay;

import java.util.*;

public class RegionMap {

    public Map<String, Set<Region>> areas;

    public RegionMap() {
        areas = new HashMap<>();

        // Hardcode the dictionary of areas and their chunks
        FillMap();
    }

    private void FillMap() {
        Set<Region> lumbridgeRegions = new HashSet<>(Arrays.asList(
                new Region(50,50),
                new Region(50,53)
        ));
        areas.put("Lumbridge", lumbridgeRegions);

        Set<Region> draynorRegions = new HashSet<>(Arrays.asList(
                new Region(48,51),
                new Region(48,50),
                new Region(49,50),
                new Region(49,53)
        ));

        areas.put("Draynor", draynorRegions);
    }

    // Get the area name for a given chunk
    public String getAreaName(Region region) {
        for (Map.Entry<String, Set<Region>> entry : areas.entrySet()) {
            System.out.println(entry);
            if (entry.getValue().contains(region)) {
                return entry.getKey();
            }
        }
        return "Unknown Area"; // Default if chunk is not found
    }
}