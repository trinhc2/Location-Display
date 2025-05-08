package com.locationdisplay;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Region {
    @Getter
    @Setter
    private int x;

    @Getter
    @Setter
    private int y;

    public Region(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Region region = (Region) obj;
        return x == region.x && y == region.y;
    }

    //needed this to perform equality check
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
