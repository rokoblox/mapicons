package com.rokoblox.pinlib.mapmarker;

import com.rokoblox.pinlib.PinLib;

/**
 * A block that uses PinLib's markers.
 * <p>
 * Implement in your Block class to use it as a MapMarker.
 */
public interface MapMarkedBlock {
    default MapMarker getCustomMarker() {
        return PinLib.getDefaultMarker();
    }
    default int getMarkerColor() {
        return 0xFFFFFFFF;
    }
}
