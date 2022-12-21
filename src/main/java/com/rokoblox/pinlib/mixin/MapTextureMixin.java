package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.access.MapIconAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.map.MapIcon;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(targets = "net.minecraft.client.render.MapRenderer$MapTexture")
public class MapTextureMixin {
    @Unique
    private @Nullable MapMarker pinlib$custom_marker_cache;
    @Unique
    private long pinlib$cached_color = -1;
    @Unique
    private long pinlib$cached_text_color = -1;

    @ModifyVariable(method = "draw", at = @At(value = "LOAD", ordinal = 4), ordinal = 0)
    private MapIcon pinlib$CheckForCustomMarker(MapIcon icon) {
        pinlib$custom_marker_cache = ((MapIconAccessor) icon).getCustomMarkerType();
        if (pinlib$custom_marker_cache != null) {
            pinlib$cached_color = ((MapIconAccessor) icon).getColor();
            pinlib$cached_text_color = ((MapIconAccessor) icon).getTextColor();
        }
        return icon;
    }

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 1)
    private float modify_g(float x) {
        return pinlib$custom_marker_cache != null ? 1.0f : x;
    }

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 2)
    private float modify_h(float x) {
        return pinlib$custom_marker_cache != null ? 0.0f : x;
    }

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 3)
    private float modify_l(float x) {
        return pinlib$custom_marker_cache != null ? 0.0f : x;
    }

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 4)
    private float modify_m(float x) {
        return pinlib$custom_marker_cache != null ? 1.0f : x;
    }

    @ModifyArg(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 1))
    private RenderLayer pinlib$CustomRenderLayer(RenderLayer rl) {
        if (pinlib$custom_marker_cache != null) {
            rl = pinlib$custom_marker_cache.getIconRenderLayer();
            pinlib$custom_marker_cache = null;
        }
        return rl;
    }

    @ModifyArgs(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;",
                            ordinal = 1
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V",
                            ordinal = 0
                    )
            )
    )
    private void pinlib$CustomIconColor(Args args) {
        if (pinlib$cached_color == -1L) return;
        args.set(0, (int) (pinlib$cached_color >> 16 & 0xFF)); // r
        args.set(1, (int) (pinlib$cached_color >> 8 & 0xFF)); // g
        args.set(2, (int) (pinlib$cached_color & 0xFF)); // b
        args.set(3, (int) (pinlib$cached_color >>> 24)); // a
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", ordinal = 0))
    private void pinlib$ClearCustomIconColor(CallbackInfo ci) {
        pinlib$cached_color = -1L;
    }

    @ModifyArg(method = "draw", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I"
    ), index = 3)
    private int pinlib$CustomTextColor(int c) {
        if (pinlib$cached_text_color != -1L) {
            int intval = (int) pinlib$cached_text_color;
            pinlib$cached_text_color = -1L;
            return intval;
        }
        return (int) 0xFFFFFFFFL;
    }
}
