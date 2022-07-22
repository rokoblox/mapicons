package com.rokoblox.pinlib.access;

import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.minecraft.item.map.MapIcon;
import org.jetbrains.annotations.Nullable;

public interface MapIconAccessor {
    MapIcon setCustomMarker(MapMarker marker);
    @Nullable MapMarker getCustomMarker();
}
