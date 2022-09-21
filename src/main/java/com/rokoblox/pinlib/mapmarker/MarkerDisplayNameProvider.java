package com.rokoblox.pinlib.mapmarker;

import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public interface MarkerDisplayNameProvider {
    Text run(BlockView world, BlockPos pos);
}
