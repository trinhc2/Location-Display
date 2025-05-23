package com.locationdisplay;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

@Slf4j
public class RegionMap {

    @Inject
    private Gson gson;

    public Map<Region, String> regionToArea = new HashMap<>();

    public void loadFromJson() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Locations.json")) {
            if (is == null) {
                throw new FileNotFoundException("Could not find Locations.json in resources folder.");
            }

            try (Reader reader = new InputStreamReader(is)) {
                Type mapType = new TypeToken<Map<String, List<List<Integer>>>>() {}.getType();
                Map<String, List<List<Integer>>> areaData = gson.fromJson(reader, mapType);

                for (Map.Entry<String, List<List<Integer>>> entry : areaData.entrySet()) {
                    String areaName = entry.getKey();
                    for (List<Integer> coords : entry.getValue()) {
                        if (coords.size() != 2) {
                            log.warn("Invalid coordinates for area {}: {}", areaName, coords);
                            continue;
                        }
                        Region region = new Region(coords.get(0), coords.get(1));
                        regionToArea.put(region, areaName);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error loading Locations.json", e);
            throw e; // Consider whether to rethrow or handle differently
        }
    }

    public String getAreaName(Region region) {
        return regionToArea.getOrDefault(region, "Unknown Area");
    }

    public void clear() {
        regionToArea.clear();
    }
}