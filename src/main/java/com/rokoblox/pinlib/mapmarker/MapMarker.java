package com.rokoblox.pinlib.mapmarker;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.map.MapIcon;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A custom marker.
 * <p>
 * Keeps track of custom markers in a map state.
 */
public class MapMarker {
    private final Identifier id;
    private final BlockPos pos;
    @Nullable
    private final Text name;

    public MapMarker(Identifier id, BlockPos pos, @Nullable Text name) {
        this.id = id;
        this.pos = pos;
        this.name = name;
    }

    public static MapMarker fromNbt(NbtCompound nbt) {
        Identifier id = new Identifier(nbt.getString("CustomID"));
        BlockPos blockPos = NbtHelper.toBlockPos(nbt.getCompound("Pos"));
        MutableText text = nbt.contains("Name") ? Text.Serializer.fromJson(nbt.getString("Name")) : null;
        return new MapMarker(id, blockPos, text);
    }

    @Nullable
    public static MapMarker fromWorldBlock(BlockView blockView, BlockPos blockPos) {
        BlockEntity blockEntity = blockView.getBlockEntity(blockPos);
        if (blockEntity instanceof MapMarkedBlockEntity mapMarkedBlockEntity) {
            Text text = Text.literal(mapMarkedBlockEntity.getMapMarkerName());
            return new MapMarker(mapMarkedBlockEntity.getCustomMapMarkerId(), blockPos, text);
        }
        return null;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public MapIcon.Type getIconType() {
        return MapIcon.Type.byId((byte) 27);
    }

    @Nullable
    public Text getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        MapMarker mapMarker = (MapMarker)o;
        return Objects.equals(this.pos, mapMarker.pos) && Objects.equals(this.name, mapMarker.name);
    }

    public int hashCode() {
        return Objects.hash(this.pos, this.name);
    }

    public NbtCompound getNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.put("Pos", NbtHelper.fromBlockPos(this.pos));
        if (this.name != null) {
            nbtCompound.putString("Name", Text.Serializer.toJson(this.name));
        }
        return nbtCompound;
    }

    public String getKey() {
        return "-" + this.pos.getX() + "," + this.pos.getY() + "," + this.pos.getZ();
    }
}
