package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.access.MapIconAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.minecraft.item.map.MapIcon;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MapIcon.class)
public class MapIconMixin implements MapIconAccessor {
    @Unique
    @Nullable
    private MapMarker pinlib$CustomMarker;

    @Override
    public MapIcon setCustomMarker(MapMarker marker) {
        pinlib$CustomMarker = marker;
        return (MapIcon)(Object)this;
    }

    @Override
    public MapMarker getCustomMarker() {
        return this.pinlib$CustomMarker;
    }
}
