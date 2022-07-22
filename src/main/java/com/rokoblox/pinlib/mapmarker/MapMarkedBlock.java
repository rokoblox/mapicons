package com.rokoblox.pinlib.mapmarker;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * A block that uses PinLib's markers.
 * <p>
 * Implement in your Block class to use it as a MapMarker.
 */
public interface MapMarkedBlock {
    public Text getMapMarkerName();
    public MapMarker getCustomMarker();
}
