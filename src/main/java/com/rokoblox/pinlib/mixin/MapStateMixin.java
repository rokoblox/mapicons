package com.rokoblox.pinlib.mixin;

import com.google.common.collect.Maps;
import com.rokoblox.pinlib.access.MapIconAccessor;
import com.rokoblox.pinlib.access.MapStateAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarker;
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
    private final Map<String, MapMarker> pinlib$customMarkers = Maps.newHashMap();

    @Unique
    private boolean pinlib$isAddingCustomMapIcon;

    @Inject(method = "fromNbt", at = @At("TAIL"))
    private static void pinlib$loadCustomMarkersNBT(NbtCompound nbt, CallbackInfoReturnable<MapState> cir) {
        MapState mapState = cir.getReturnValue();
        NbtList nbtList = nbt.getList("pinlib_custom_markers", NbtElement.COMPOUND_TYPE);
        for (int k = 0; k < nbtList.size(); ++k) {
            MapMarker mapMarker = MapMarker.fromNbt(nbtList.getCompound(k));
            ((MapStateMixin)(Object)mapState).pinlib$customMarkers.put(mapMarker.getKey(), mapMarker);
            ((MapStateMixin)(Object)mapState).pinlib$isAddingCustomMapIcon = true;
            ((MapStateMixin)(Object)mapState).addIcon(mapMarker.getIconType(), null, mapMarker.getKey(), mapMarker.getPos().getX(), mapMarker.getPos().getZ(), 180.0, mapMarker.getName());
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void pinlib$saveCustomMarkersNBT(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        NbtList nbtList = new NbtList();
        for (MapMarker mapMarker : this.pinlib$customMarkers.values()) {
            nbtList.add(mapMarker.getNbt());
        }
        nbt.put("pinlib_custom_markers", nbtList);
    }

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Ljava/util/Map;putAll(Ljava/util/Map;)V", ordinal = 1))
    private void copyWaystones(CallbackInfoReturnable<MapState> cir) {
        ((MapStateMixin)(Object)cir.getReturnValue()).pinlib$customMarkers.putAll(this.pinlib$customMarkers);
    }

    @ModifyVariable(method = "addIcon", at = @At("STORE"))
    private MapIcon markIconAsCustomIcon(MapIcon icon) {
        if (pinlib$isAddingCustomMapIcon)
            ((MapIconAccessor)icon).setIsCustom(true);
        pinlib$isAddingCustomMapIcon = false;
        return icon;
    }

    public boolean addMapMarker(WorldAccess world, BlockPos pos) {
        double d = (double)pos.getX() + 0.5;
        double e = (double)pos.getZ() + 0.5;
        int i = 1 << ((MapState)(Object)this).scale;
        double f = (d - (double)((MapState)(Object)this).centerX) / (double)i;
        double g = (e - (double)((MapState)(Object)this).centerZ) / (double)i;
        if (f >= -63.0 && g >= -63.0 && f <= 63.0 && g <= 63.0) {
            MapMarker mapMarker = MapMarker.fromWorldBlock(world, pos);
            if (mapMarker == null) {
                return false;
            }
            if (this.pinlib$customMarkers.remove(mapMarker.getKey(), mapMarker)) {
                removeIcon(mapMarker.getKey());
                return true;
            }
            if (!((MapState)(Object)this).method_37343(256)) {
                this.pinlib$customMarkers.put(mapMarker.getKey(), mapMarker);
                pinlib$isAddingCustomMapIcon = true;
                addIcon(mapMarker.getIconType(), world, mapMarker.getKey(), d, e, 180.0, mapMarker.getName());
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings({"TooBroadScope", "UnclearExpression"})
    public @Nullable BlockPos removeMapMarker(BlockView world, int x, int z) {
        Iterator<MapMarker> iterator = this.pinlib$customMarkers.values().iterator();
        while (iterator.hasNext()) {
            MapMarker mapMarker2;
            MapMarker mapMarker = iterator.next();
            if (mapMarker.getPos().getX() != x || mapMarker.getPos().getZ() != z || mapMarker.equals(mapMarker2 = MapMarker.fromWorldBlock(world, mapMarker.getPos()))) continue;
            iterator.remove();
            this.removeIcon(mapMarker.getKey());
            return mapMarker.getPos();
        }
        return null;
    }

    public Collection<MapMarker> getCustomMarkers() {
        return this.pinlib$customMarkers.values();
    }
}
