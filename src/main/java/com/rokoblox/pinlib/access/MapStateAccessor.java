package com.rokoblox.pinlib.access;

import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface MapStateAccessor {
    boolean addMapMarker(WorldAccess world, BlockPos pos);
    @Nullable BlockPos removeMapMarker(BlockView world, int x, int z);

    Collection<MapMarker> getCustomMarkers();
}
