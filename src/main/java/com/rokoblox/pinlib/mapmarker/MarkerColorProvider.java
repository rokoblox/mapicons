package com.rokoblox.pinlib.mapmarker;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public interface MarkerColorProvider {
    long run(BlockView world, BlockPos pos);
}
