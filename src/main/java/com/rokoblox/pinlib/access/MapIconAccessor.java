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
     * @return Color as a long integer
     */
    long getColor();

    /**
     * Use {@link net.minecraft.util.math.ColorHelper}
     * to get individual ARGB values.
     *
     * @return Text color as long integer
     */
    long getTextColor();

    MapIcon color(long color);

    /**
     * @param r Red
     * @param g Green
     * @param b Blue
     * @param a Alpha (Set to 255 for opaque and 0 for transparent)
     */
    MapIcon color(int r, int g, int b, int a);

    MapIcon textColor(long color);

    /**
     * @param r Red
     * @param g Green
     * @param b Blue
     * @param a Alpha (Set to 255 for opaque and 0 for transparent)
     */
    MapIcon textColor(int r, int g, int b, int a);
}
