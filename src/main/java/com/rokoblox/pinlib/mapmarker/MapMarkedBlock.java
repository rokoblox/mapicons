package com.rokoblox.pinlib.mapmarker;

import net.minecraft.text.Text;

/**
 * A block that uses PinLib's markers.
 * <p>
 * Implement in your Block class to use it as a MapMarker.
 */
public interface MapMarkedBlock {
    Text getMapMarkerName();
    MapMarker getCustomMarker();
}
