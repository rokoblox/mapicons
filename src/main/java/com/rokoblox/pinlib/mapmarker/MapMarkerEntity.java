package com.rokoblox.pinlib.mapmarker;

import com.rokoblox.pinlib.PinLib;
import net.minecraft.block.BlockState;
import net.minecraft.item.map.MapIcon;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    protected long textColor = 0xFFFFFFFF;
    private MapIcon mapIcon;

    private MapMarkerEntity(MapMarker type, Identifier id, BlockPos pos, @Nullable Text displayName, long color, long textColor) {
        this.type = type;
        this.id = id;
        this.pos = pos;
        this.displayName = displayName;
        this.color = color;
        this.textColor = textColor;
    }

    public MapMarkerEntity(MapMarker type, BlockPos pos, @Nullable Text displayName, long color, long textColor) {
        this(type, type.getId(), pos, displayName, color, textColor);
    }

    public MapMarkerEntity(MapMarker type, BlockPos pos, @Nullable Text displayName) {
        this.type = type;
        this.id = type.getId();
        this.pos = pos;
        this.displayName = displayName;
    }

    @Nullable
    public static MapMarkerEntity fromWorldBlock(World world, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        MapMarkedBlock mapMarkedBlock = PinLib.getMapMarkedBlock(blockState.getBlock());
        if (mapMarkedBlock != null) {
            MapMarker type = mapMarkedBlock.markerProvider.run();
            Text text = mapMarkedBlock.markerDisplayNameProvider.run(world, blockPos);
            return new MapMarkerEntity(type, blockPos, text, mapMarkedBlock.markerColorProvider.run(world, blockPos), mapMarkedBlock.textColorProvider.run(world, blockPos));
        } else if (blockState.getBlock() instanceof @SuppressWarnings("deprecation")IMapMarkedBlock deprecatedMapMarkedBlock) {
            MapMarker type = deprecatedMapMarkedBlock.getCustomMarker();
            Text text = deprecatedMapMarkedBlock.getDisplayName(world, blockPos);
            return new MapMarkerEntity(type, blockPos, text, deprecatedMapMarkedBlock.getMarkerColor(world, blockPos), 0xFFFFFFFF);
        }
        return null;
    }

    public static MapMarkerEntity fromNbt(NbtCompound nbt) {
        Identifier id = new Identifier(nbt.getString("Id"));
        MapMarker markerType = PinLib.get(id);
        BlockPos blockPos = NbtHelper.toBlockPos(nbt.getCompound("Pos"));
        long color = nbt.getLong("Color");
        long textColor = nbt.contains("TextColor") ? nbt.getLong("TextColor") : 0xFFFFFFFF;
        MutableText text = nbt.contains("DisplayName") ? Text.Serializer.fromJson(nbt.getString("DisplayName")) : null;
        return new MapMarkerEntity(markerType, id, blockPos, text, color, textColor);
    }

    public NbtCompound getNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("Id", this.id.toString());
        nbtCompound.put("Pos", NbtHelper.fromBlockPos(this.pos));
        if (this.mapIcon != null) {
            nbtCompound.putLong("Color", this.color);
            nbtCompound.putLong("TextColor", this.textColor);
        } else {
            nbtCompound.putLong("Color", 0xFFFFFFFFL);
            nbtCompound.putLong("TextColor", 0xFFFFFFFFL);
        }
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
                && (this.color == mapMarkerEntity.color)
                && (this.textColor == mapMarkerEntity.textColor);
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

    public long getTextColor() {
        return this.textColor;
    }
}

