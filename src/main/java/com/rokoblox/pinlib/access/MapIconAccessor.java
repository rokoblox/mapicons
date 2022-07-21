package com.rokoblox.pinlib.access;

import net.minecraft.item.map.MapIcon;

public interface MapIconAccessor {
    MapIcon setIsCustom(boolean bl);
    boolean getIsCustom();
}
