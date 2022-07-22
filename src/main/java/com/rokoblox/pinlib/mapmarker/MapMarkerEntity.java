package com.rokoblox.pinlib.mapmarker;

import com.rokoblox.pinlib.PinLib;
import net.minecraft.block.BlockState;
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
 * A custom map marker.
 * <p>
 * Keeps track of a custom map marker in a MapState.
 */
public class MapMarkerEntity {

    private final MapMarker type;
    private final Identifier id; // To preserve the original ID even if it was missing in the currently loaded registry.
    private final BlockPos pos;
    protected @Nullable Text displayName;
    protected int color = 0xFFFFFF;

    public MapMarkerEntity(MapMarker type, BlockPos pos, @Nullable Text displayName, int color) {
        this.type = type;
        this.id = type.getId();
        this.pos = pos;
        this.displayName = displayName;
        this.color = color;
    }

    private MapMarkerEntity(MapMarker type, Identifier id, BlockPos pos, @Nullable Text displayName, int color) {
        this.type = type;
        this.id = id;
        this.pos = pos;
        this.displayName = displayName;
        this.color = color;
    }

    public MapMarkerEntity(MapMarker type, BlockPos pos, @Nullable Text displayName) {
        this.type = type;
        this.id = type.getId();
        this.pos = pos;
        this.displayName = displayName;
    }

    @Nullable
    public static MapMarkerEntity fromWorldBlock(BlockView blockView, BlockPos blockPos) {
        BlockState blockState = blockView.getBlockState(blockPos);
        if (blockState.getBlock() instanceof MapMarkedBlock mapMarkedBlock) {
            MapMarker type = mapMarkedBlock.getCustomMarker();
            Text text = mapMarkedBlock.getMapMarkerName();
            return new MapMarkerEntity(type, blockPos, text);
        }
        return null;
    }

    public static MapMarkerEntity fromNbt(NbtCompound nbt) {
        Identifier id = new Identifier(nbt.getString("Id"));
        MapMarker markerType = PinLib.get(id);
        BlockPos blockPos = NbtHelper.toBlockPos(nbt.getCompound("Pos"));
        int color = nbt.getInt("Color");
        MutableText text = nbt.contains("DisplayName") ? Text.Serializer.fromJson(nbt.getString("DisplayName")) : null;
        return new MapMarkerEntity(markerType, id, blockPos, text, color);
    }

    public NbtCompound getNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("Id", this.id.toString());
        nbtCompound.put("Pos", NbtHelper.fromBlockPos(this.pos));
        nbtCompound.putInt("Color", this.color);
        if (this.displayName != null) {
            nbtCompound.putString("DisplayName", Text.Serializer.toJson(this.displayName));
        }
        return nbtCompound;
    }

    public MapMarker getType() {
        return type;
    }

    public Identifier getId() {
        return id;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Text displayName) {
        this.displayName = displayName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        MapMarkerEntity mapMarkerEntity = (MapMarkerEntity) o;
        return Objects.equals(this.type, mapMarkerEntity.type)
                && Objects.equals(this.id, mapMarkerEntity.id)
                && Objects.equals(this.pos, mapMarkerEntity.pos)
                && Objects.equals(this.displayName, mapMarkerEntity.displayName)
                && (this.color == mapMarkerEntity.color);
    }

    public int hashCode() {
        return Objects.hash("pinLibMapMarkerEntity", this.type, this.id, this.pos, this.displayName, this.color);
    }

}
