package com.rokoblox.pinlib.access;

import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.minecraft.item.map.MapIcon;
import org.jetbrains.annotations.Nullable;

public interface MapIconAccessor {
    MapIcon setCustomMarker(MapMarker marker);
    @Nullable MapMarker getCustomMarkerType();

    /**
     * Use {@link net.minecraft.util.math.ColorHelper}
     * to get individual ARGB values.
     *
     * @return Color as integer
     */
    long getColor();

    MapIcon color(long color);
    /**
     * @param r Red
     * @param g Green
     * @param b Blue
     * @param a Alpha (Set to 255 for opaque and 0 for transparent)
     */
    MapIcon color(int r, int g, int b, int a);
}
