package com.rokoblox.pinlib.mapmarker;

import net.minecraft.util.Identifier;

/**
 * A BlockEntity that uses PinLib's markers.
 * <p>
 * Implement in your BlockEntity class to use it as a MapMarker.
 */
public interface MapMarkedBlockEntity {
    public String getMapMarkerName();
    public Identifier getCustomMapMarkerId();
}
