package com.rokoblox.pinlib.mapmarker;

import com.rokoblox.pinlib.PinLib;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/**
 * A block that uses PinLib's markers.
 * <p>
 * Implement in your Block class to use it as a MapMarker.
 */
public interface MapMarkedBlock {
    default MapMarker getCustomMarker() {
        return PinLib.getDefaultMarker();
    }

    default long getMarkerColor() {
        return 0xFFFFFFFFL;
    }

    default Text getDisplayName(BlockView world, BlockPos pos) {
        return null;
    }
}
