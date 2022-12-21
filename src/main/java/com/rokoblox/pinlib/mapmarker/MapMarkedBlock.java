package com.rokoblox.pinlib.mapmarker;

import net.minecraft.block.Block;

public class MapMarkedBlock {
    public final Block block;
    public final CustomMarkerProvider markerProvider;
    public final MarkerColorProvider markerColorProvider;
    public final MarkerColorProvider textColorProvider;
    public final MarkerDisplayNameProvider markerDisplayNameProvider;

    public MapMarkedBlock(Block block, CustomMarkerProvider markerProvider, MarkerColorProvider markerColorProvider, MarkerColorProvider textColorProvider, MarkerDisplayNameProvider markerDisplayNameProvider) {
        this.block = block;
        this.markerProvider = markerProvider;
        this.markerColorProvider = markerColorProvider;
        this.textColorProvider = textColorProvider;
        this.markerDisplayNameProvider = markerDisplayNameProvider;
    }
}

