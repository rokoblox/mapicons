package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.access.MapIconAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.minecraft.item.map.MapIcon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MapIcon.class)
public class MapIconMixin implements MapIconAccessor {
    @Unique
    private MapMarker pinlib$MarkerType;
    @Unique
    private long pinlib$Color = 0xFFFFFFFF;

    @Override
    public MapIcon setCustomMarker(MapMarker marker) {
        pinlib$MarkerType = marker;
        return (MapIcon)(Object)this;
    }

    @Override
    public MapMarker getCustomMarkerType() {
        return this.pinlib$MarkerType;
    }

    public long getColor() {
        return pinlib$Color;
    }

    @Override
    public MapIcon color(long color) {
        this.pinlib$Color = color;
        return (MapIcon)(Object) this;
    }

    public MapIcon color(int r, int g, int b, int a) {
        this.pinlib$Color = a << 24 | r << 16 | g << 8 | b;
        return (MapIcon)(Object) this;
    }
}
