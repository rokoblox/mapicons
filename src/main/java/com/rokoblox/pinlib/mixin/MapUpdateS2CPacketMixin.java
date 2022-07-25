package com.rokoblox.pinlib.mixin;

import com.rokoblox.pinlib.PinLib;
import com.rokoblox.pinlib.access.MapIconAccessor;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.minecraft.item.map.MapIcon;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapUpdateS2CPacket.class)
public class MapUpdateS2CPacketMixin {
    @Inject(method = "method_43883", at = @At(value = "RETURN"))
    private static void pinlib$ReadCustomMarkerStates(PacketByteBuf buf3, CallbackInfoReturnable<MapIcon> cir) {
        Identifier id = buf3.readIdentifier();
        if (!id.getPath().equals("null"))
            ((MapIconAccessor) cir.getReturnValue()).setCustomMarker(PinLib.get(id));
    }

    @Inject(method = "method_34136", at = @At(value = "RETURN"))
    private static void pinlib$WriteCustomMarkerStates(PacketByteBuf b, MapIcon icon, CallbackInfo ci) {
        MapMarker type = ((MapIconAccessor) icon).getCustomMarkerType();
        b.writeIdentifier(type != null ? type.getId() : new Identifier("pinlib", "null"));
    }
}
