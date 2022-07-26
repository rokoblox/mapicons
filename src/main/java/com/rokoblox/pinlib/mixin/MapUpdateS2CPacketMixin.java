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
        MapIconAccessor iconAccessor = (MapIconAccessor) cir.getReturnValue();
        if (!id.getPath().equals("null"))
            iconAccessor.setCustomMarker(PinLib.get(id));
        iconAccessor.color(buf3.readLong());
    }

    @Inject(method = "method_34136", at = @At(value = "RETURN"))
    private static void pinlib$WriteCustomMarkerStates(PacketByteBuf b, MapIcon icon, CallbackInfo ci) {
        MapIconAccessor iconAccessor = (MapIconAccessor) icon;
        MapMarker type = iconAccessor.getCustomMarkerType();
        b.writeIdentifier(type != null ? type.getId() : new Identifier("pinlib", "null"));
        b.writeLong(iconAccessor.getColor());
    }
}
