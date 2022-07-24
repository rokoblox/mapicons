package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.PinLib;
import com.rokoblox.pinlib.access.MapIconAccessor;
import com.rokoblox.pinlib.access.MapStateAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarkedBlock;
import com.rokoblox.pinlib.mapmarker.MapMarkerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FilledMapItem.class)
public class FilledMapItemMixin {
    @Unique
    private MapMarkedBlock pinlib$map_marked_block_cache;

    @ModifyArgs(method = "updateColors", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/map/MapState;removeBanner(Lnet/minecraft/world/BlockView;II)V"))
    private void pinlib$UpdateCustomMarkers(Args args, World world, Entity entity, MapState state) {
        BlockPos removalPos;
        if ((removalPos = ((MapStateAccessor)state).removeMapMarker(world, args.get(1), args.get(2))) != null)
            ((MapStateAccessor)state).addMapMarker(world, removalPos, null);
    }

    @ModifyVariable(method = "useOnBlock", at = @At(value = "STORE"), ordinal = 0)
    private BlockState pinlib$CheckForCustomMarkerBlock(BlockState state, ItemUsageContext context) {
        if (context.getWorld().isClient)
            return state;
        if (state.getBlock() instanceof MapMarkedBlock mapMarkedBlock)
            pinlib$map_marked_block_cache = mapMarkedBlock;
        return state;
    }

    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void pinlib$AddCustomMarker(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (pinlib$map_marked_block_cache == null)
            return;

        MapMarkerEntity mapMarker;
        Text displayName = null;
        if (pinlib$map_marked_block_cache instanceof Nameable namedBlock)
            displayName = namedBlock.getDisplayName();

        if ((mapMarker = PinLib.tryAddMapMarker(context.getStack(), context.getWorld(), context.getBlockPos(), pinlib$map_marked_block_cache.getCustomMarker(), displayName)) != null) {
            MapIconAccessor iconAccessor = ((MapIconAccessor)mapMarker.getIcon());
            if (iconAccessor != null)
                iconAccessor.color(pinlib$map_marked_block_cache.getMarkerColor());
            pinlib$map_marked_block_cache = null;
            cir.setReturnValue(ActionResult.SUCCESS);
        }
        pinlib$map_marked_block_cache = null;
        cir.setReturnValue(ActionResult.FAIL);
    }
}
