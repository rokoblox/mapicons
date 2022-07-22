package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.PinLib;
import com.rokoblox.pinlib.access.MapIconAccessor;
import net.minecraft.item.map.MapIcon;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapUpdateS2CPacket.class)
public class MapUpdateS2CPacketMixin {
    @Inject(method = "method_43883", at = @At(value = "RETURN"))
    private static void pinlib$ReadCustomMarkerStates(PacketByteBuf buf3, CallbackInfoReturnable<MapIcon> cir) {
        ((MapIconAccessor)cir.getReturnValue()).setCustomMarker(PinLib.get(buf3.readIdentifier()));
    }

    @Inject(method = "method_34136", at = @At(value = "RETURN"))
    private static void pinlib$WriteCustomMarkerStates(PacketByteBuf b, MapIcon icon, CallbackInfo ci) {
        b.writeIdentifier(((MapIconAccessor)icon).getCustomMarker().getId());
    }
}
