package com.rokoblox.pinlib.mixin;

import com.google.common.collect.Maps;
import com.rokoblox.pinlib.access.MapIconAccessor;
import com.rokoblox.pinlib.access.MapStateAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarkerEntity;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Mixin(MapState.class)
public class MapStateMixin implements MapStateAccessor {

    @Shadow
    private void removeIcon(String id) {}

    @Shadow
    private void addIcon(MapIcon.Type type, @Nullable WorldAccess world, String key, double x, double z, double rotation, @Nullable Text text) {}

    @Unique
    private final Map<String, MapMarkerEntity> pinlib$customMarkerEntities = Maps.newHashMap();

    @Unique
    private MapMarkerEntity pinlib$customIconMarkerToAdd;

    @Inject(method = "fromNbt", at = @At("TAIL"))
    private static void pinlib$loadCustomMarkersNBT(NbtCompound nbt, CallbackInfoReturnable<MapState> cir) {
        MapState mapState = cir.getReturnValue();
        NbtList nbtList = nbt.getList("pinlib_custom_markers", NbtElement.COMPOUND_TYPE);
        for (int k = 0; k < nbtList.size(); ++k) {
            MapMarkerEntity mapMarker = MapMarkerEntity.fromNbt(nbtList.getCompound(k));
            ((MapStateMixin)(Object)mapState).pinlib$customMarkerEntities.put(mapMarker.getKey(), mapMarker);
            ((MapStateMixin)(Object)mapState).pinlib$customIconMarkerToAdd = mapMarker;
            ((MapStateMixin)(Object)mapState).addIcon(MapIcon.Type.TARGET_POINT, null, mapMarker.getKey(), mapMarker.getPos().getX(), mapMarker.getPos().getZ(), 180.0, mapMarker.getDisplayName());
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void pinlib$saveCustomMarkersNBT(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        NbtList nbtList = new NbtList();
        for (MapMarkerEntity mapMarker : this.pinlib$customMarkerEntities.values()) {
            nbtList.add(mapMarker.getNbt());
        }
        nbt.put("pinlib_custom_markers", nbtList);
    }

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Ljava/util/Map;putAll(Ljava/util/Map;)V", ordinal = 1))
    private void copyWaystones(CallbackInfoReturnable<MapState> cir) {
        ((MapStateMixin)(Object)cir.getReturnValue()).pinlib$customMarkerEntities.putAll(this.pinlib$customMarkerEntities);
    }

    @ModifyVariable(method = "addIcon", at = @At("STORE"))
    private MapIcon markIconAsCustomIcon(MapIcon icon) {
        if (pinlib$customIconMarkerToAdd != null) {
            ((MapIconAccessor)icon).setCustomMarker(pinlib$customIconMarkerToAdd.getType());
            ((MapIconAccessor)icon).color(pinlib$customIconMarkerToAdd.getColor());
            pinlib$customIconMarkerToAdd.setIcon(icon);
        }
        pinlib$customIconMarkerToAdd = null;
        return icon;
    }

    public boolean addMapMarker(WorldAccess world, BlockPos pos, MapMarkerEntity mapMarker) {
        double d = (double)pos.getX() + 0.5;
        double e = (double)pos.getZ() + 0.5;
        int i = 1 << ((MapState)(Object)this).scale;
        double f = (d - (double)((MapState)(Object)this).centerX) / (double)i;
        double g = (e - (double)((MapState)(Object)this).centerZ) / (double)i;
        if (f >= -63.0 && g >= -63.0 && f <= 63.0 && g <= 63.0) {
            if (mapMarker == null) {
                return false;
            }
            if (this.pinlib$customMarkerEntities.remove(mapMarker.getKey(), mapMarker)) {
                removeIcon(mapMarker.getKey());
                return true;
            }
            if (!((MapState)(Object)this).method_37343(256)) {
                this.pinlib$customMarkerEntities.put(mapMarker.getKey(), mapMarker);
                pinlib$customIconMarkerToAdd = mapMarker;
                addIcon(MapIcon.Type.TARGET_POINT, world, mapMarker.getKey(), d, e, 180.0, mapMarker.getDisplayName());
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings({"TooBroadScope", "UnclearExpression"})
    public @Nullable BlockPos removeMapMarker(BlockView world, int x, int z) {
        Iterator<MapMarkerEntity> iterator = this.pinlib$customMarkerEntities.values().iterator();
        while (iterator.hasNext()) {
            MapMarkerEntity mapMarker2;
            MapMarkerEntity mapMarker = iterator.next();
            if (!mapMarker.getType().isDynamic()) continue;
            if (mapMarker.getPos().getX() != x || mapMarker.getPos().getZ() != z || mapMarker.equals(mapMarker2 = MapMarkerEntity.fromWorldBlock(world, mapMarker.getPos()))) continue;
            iterator.remove();
            this.removeIcon(mapMarker.getKey());
            return mapMarker.getPos();
        }
        return null;
    }

    public Collection<MapMarkerEntity> getCustomMarkerEntities() {
        return this.pinlib$customMarkerEntities.values();
    }
}
