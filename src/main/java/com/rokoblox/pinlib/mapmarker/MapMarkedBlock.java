package com.rokoblox.pinlib.mapmarker;

/**
 * A block that uses PinLib's markers.
 * <p>
 * Implement in your Block class to use it as a MapMarker.
 */
public interface MapMarkedBlock {
    MapMarker getCustomMarker();
}
