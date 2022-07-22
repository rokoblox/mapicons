package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.access.MapIconAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.map.MapIcon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.client.render.MapRenderer$MapTexture")
public class MapTextureMixin {
    @Unique
    private MapMarker pinlib$custom_marker_cache;

    @ModifyVariable(method = "draw", at = @At("LOAD"), ordinal = 0)
    private MapIcon pinlib$CheckForCustomMarker(MapIcon icon) {
        pinlib$custom_marker_cache = ((MapIconAccessor) icon).getCustomMarker();
        return icon;
    }

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 1)
    private float modify_g(float x) {
        return pinlib$custom_marker_cache != null ? 0.0f : x;
    }

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 2)
    private float modify_h(float x) {
        return pinlib$custom_marker_cache != null ? 0.0f : x;
    }

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 3)
    private float modify_l(float x) {
        return pinlib$custom_marker_cache != null ? 1.0f : x;
    }

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 4)
    private float modify_m(float x) {
        return pinlib$custom_marker_cache != null ? 1.0f : x;
    }

    @ModifyArg(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 1))
    private RenderLayer pinlib$CustomRenderLayer(RenderLayer rl) {
        if (pinlib$custom_marker_cache != null) {
            pinlib$custom_marker_cache = null;
            return pinlib$custom_marker_cache.getIconRenderLayer();
        } else return rl;
    }
}
