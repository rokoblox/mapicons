package com.rokoblox.pinlib.mapmarker;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DynamicMapMarker extends MapMarker {
    public DynamicMapMarker(Identifier id, BlockPos pos, @Nullable Text name) {
        super(id, pos, name);
    }
}
