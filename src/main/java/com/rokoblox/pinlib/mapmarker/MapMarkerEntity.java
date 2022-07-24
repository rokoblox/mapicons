package com.rokoblox.pinlib.mapmarker;

import com.rokoblox.pinlib.PinLib;
import com.rokoblox.pinlib.access.MapIconAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.item.map.MapIcon;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Nameable;
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
    protected long color = 0xFFFFFFFFL;
    private MapIcon mapIcon;

    private MapMarkerEntity(MapMarker type, Identifier id, BlockPos pos, @Nullable Text displayName, long color) {
        this.type = type;
        this.id = id;
        this.pos = pos;
        this.displayName = displayName;
        this.color = color;
    }

    public MapMarkerEntity(MapMarker type, BlockPos pos, @Nullable Text displayName, long color) {
        this(type, type.getId(), pos, displayName, color);
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
            Text text = null;
            if (mapMarkedBlock instanceof Nameable namedMapMarkedBlock)
                text = namedMapMarkedBlock.getDisplayName();
            return new MapMarkerEntity(type, blockPos, text);
        }
        return null;
    }

    public static MapMarkerEntity fromNbt(NbtCompound nbt) {
        Identifier id = new Identifier(nbt.getString("Id"));
        MapMarker markerType = PinLib.get(id);
        BlockPos blockPos = NbtHelper.toBlockPos(nbt.getCompound("Pos"));
        long color = nbt.getLong("Color");
        MutableText text = nbt.contains("DisplayName") ? Text.Serializer.fromJson(nbt.getString("DisplayName")) : null;
        return new MapMarkerEntity(markerType, id, blockPos, text, color);
    }

    public NbtCompound getNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("Id", this.id.toString());
        nbtCompound.put("Pos", NbtHelper.fromBlockPos(this.pos));
        if (this.mapIcon != null)
            nbtCompound.putLong("Color", ((MapIconAccessor) this.mapIcon).getColor());
        else
            nbtCompound.putLong("Color", 0xFFFFFFFFL);
        if (this.displayName != null) {
            nbtCompound.putString("DisplayName", Text.Serializer.toJson(this.displayName));
        }
        return nbtCompound;
    }

    public MapMarker getType() {
        return type;
    }

    /**
     * Gets the actual ID of the marker, this
     * is sometimes different from the ID of
     * the marker type when a world with a
     * marker that was previously registered
     * but not anymore is loaded.
     * The original marker ID stays in the
     * saved NBT data to preserve the original
     * marker type if the original mod is loaded
     * again.
     * @return Identifier
     */
    public Identifier getId() {
        return id;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Nullable
    public Text getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable Text displayName) {
        this.displayName = displayName;
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

    public String getKey() {
        return this.id.toString() + "." + this.pos.getX() + "," + this.pos.getY() + "," + this.pos.getZ();
    }

    public void setIcon(MapIcon icon) {
        this.mapIcon = icon;
    }

    public MapIcon getIcon() {
        return this.mapIcon;
    }

    public long getColor() {
        return this.color;
    }
}
