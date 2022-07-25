package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.PinLib;
import com.rokoblox.pinlib.access.MapStateAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarkedBlock;
import com.rokoblox.pinlib.mapmarker.MapMarkerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.map.MapState;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FilledMapItem.class)
public abstract class FilledMapItemMixin {

    @ModifyArgs(method = "updateColors", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/map/MapState;removeBanner(Lnet/minecraft/world/BlockView;II)V"))
    private void pinlib$UpdateCustomMarkers(Args args, World world, Entity entity, MapState state) {
        MapMarkerEntity removedMarker;
        if ((removedMarker = ((MapStateAccessor) state).removeMapMarker(world, args.get(1), args.get(2), true, null)) != null)
            ((MapStateAccessor) state).addMapMarker(world, removedMarker.getPos(), MapMarkerEntity.fromWorldBlock(world, removedMarker.getPos()));
    }

    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void pinlib$AddCustomMarker(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (context.getWorld().isClient)
            return;
        MapStateAccessor mapState = (MapStateAccessor) FilledMapItem.getOrCreateMapState(context.getStack(), context.getWorld());
        if (mapState == null)
            return;
        MapMarkerEntity mapMarker = MapMarkerEntity.fromWorldBlock(context.getWorld(), context.getBlockPos());
        if (mapState.addMapMarker(context.getWorld(), context.getBlockPos(), mapMarker)) {
            PinLib.LOGGER.info("Added map marker with id [{}] at: [{}]", mapMarker.getId().toString(), context.getBlockPos().toShortString());
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if ((mapMarker = mapState.removeMapMarker(
                null,
                context.getBlockPos().getX(),
                context.getBlockPos().getZ(),
                !(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof MapMarkedBlock),
                null
        )) != null) {
            PinLib.LOGGER.info("Removed map marker with id [{}] at: [{}]", mapMarker.getId(), context.getBlockPos().toShortString());
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
