package com.rokoblox.pinlib.access;

import com.rokoblox.pinlib.mapmarker.MapMarker;
import com.rokoblox.pinlib.mapmarker.MapMarkerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface MapStateAccessor {
    boolean addMapMarker(WorldAccess world, BlockPos pos, MapMarkerEntity mapMarker);

    @Nullable MapMarkerEntity removeMapMarker(@Nullable BlockView world, int x, int z, boolean keepStatic, @Nullable MapMarker markerType);

    @Nullable MapMarkerEntity getMapMarker(int x, int z);

    Collection<MapMarkerEntity> getCustomMarkerEntities();
}
