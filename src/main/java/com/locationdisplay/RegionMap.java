package com.locationdisplay;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class RegionMap {

    @Inject
    private Gson gson;

    public Map<Region, String> regionToArea = new HashMap<>();

    public void loadFromJson() {
        System.out.println("load called");
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("Locations.json");
            if (is == null) {
                throw new FileNotFoundException("Could not find Locations.json in resources folder.");
            }

            Reader reader = new InputStreamReader(is);

            Type mapType = new TypeToken<Map<String, List<List<Integer>>>>() {}.getType();
            Map<String, List<List<Integer>>> areaData = gson.fromJson(reader, mapType);

            for (Map.Entry<String, List<List<Integer>>> entry : areaData.entrySet()) {
                String areaName = entry.getKey();
                List<List<Integer>> coordsList = entry.getValue();
                System.out.println(areaName);

                for (List<Integer> coords : coordsList) {
                    int x = coords.get(0);
                    int y = coords.get(1);
                    Region region = new Region(x, y);
                    regionToArea.put(region, areaName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAreaName(Region region) {
        return regionToArea.getOrDefault(region, "Unknown Area");
    }
}