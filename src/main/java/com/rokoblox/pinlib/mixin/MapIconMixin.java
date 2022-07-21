package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.access.MapIconAccessor;
import net.minecraft.item.map.MapIcon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MapIcon.class)
public class MapIconMixin implements MapIconAccessor {
    @Unique
    private boolean pinlib$isCustomMarker = false;

    @Override
    public MapIcon setIsCustom(boolean bl) {
        pinlib$isCustomMarker = bl;
        return (MapIcon)(Object)this;
    }

    @Override
    public boolean getIsCustom() {
        return this.pinlib$isCustomMarker;
    }
}
