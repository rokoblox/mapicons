package com.rokoblox.pinlib;

import com.rokoblox.pinlib.access.MapStateAccessor;
import com.rokoblox.pinlib.mapmarker.IMapMarkedBlock;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import com.rokoblox.pinlib.mapmarker.MapMarkerEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class PinLib implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("PinLib");
    private static final Registry<MapMarker> REGISTRY = FabricRegistryBuilder.createDefaulted(MapMarker.class, new Identifier("pinlib", "map_markers"), new Identifier("pinlib", "default")).buildAndRegister();
    private static final MapMarker DEFAULT_MARKER = createStaticMarker(new Identifier("pinlib", "default"));

    /**
     * This is invoked by fabric; do not call.
     */
    @Override
    public void onInitialize() {
//        TestingClass.init(); // Used for testing purposes only.
    }

    /**
     * Returns a static map marker that does not
     * auto-update and can only be removed manually.
     * Allows permanent display names.
     * <p></p>
     * <p>
     *     <b>Hint:</b> use for static points of interest
     *     such as naturally-generating structures.
     * </p>
     *
     * @param id Unique map marker identifier
     * @return Static MapMarker
     */
    public static MapMarker createStaticMarker(Identifier id) {
        LOGGER.info("Registering new static map marker with id [{}]", id.toString());
        return Registry.register(REGISTRY, id, new MapMarker(id, false));
    }

    /**
     * Returns a dynamic map marker that automatically
     * updates but can be removed manually.
     *
     * <p>
     *     Allows dynamic display names.
     * </p>
     * <p></p>
     * <p>
     *     <b>Hint:</b> use for dynamic points of interest
     *     such as player-placed blocks.
     * </p>
     *
     * @param id Unique map marker identifier
     * @return Static MapMarker
     */
    public static MapMarker createDynamicMarker(Identifier id) {
        LOGGER.info("Registering new dynamic map marker with id [{}]", id.toString());
        return Registry.register(REGISTRY, id, new MapMarker(id, true));
    }

    /**
     * Adds a map marker to the provided
     * MapState (You could use the simpler
     * tryAddMapMarker() instead).
     *
     * @param mapState   Modified MapState
     * @param pos        Block Position to place marker
     * @param markerType Map marker type
     * @return Provided MapState
     */
    public static MapMarkerEntity addMapMarker(MapState mapState, @Nullable World world, BlockPos pos, @Nullable MapMarker markerType, @Nullable Text displayName) {
        MapMarkerEntity mapMarker = null;
        if (markerType == null && world != null)
            mapMarker = MapMarkerEntity.fromWorldBlock(world, pos);
        else if (markerType != null)
            mapMarker = new MapMarkerEntity(markerType, pos, displayName);
        if (mapMarker == null)
            return null;
        boolean success = ((MapStateAccessor) mapState).addMapMarker(world, pos, mapMarker);
        if (success)
            PinLib.LOGGER.info("Added map marker with id [{}] at: [{}]", mapMarker.getId().toString(), pos.toShortString());
        return success ? mapMarker : null;
    }

    /**
     * Tries to add a map marker to the
     * provided ItemStack if it's a valid
     * map and contains a valid MapState.
     *
     * @param stack      The item stack which the marker will be added to
     * @param world      World to get the map state from
     * @param markerType Map marker type
     * @return Provided MapState
     */
    public static MapMarkerEntity tryAddMapMarker(ItemStack stack, World world, BlockPos pos, @Nullable MapMarker markerType, @Nullable Text displayName) {
        MapState mapState = FilledMapItem.getOrCreateMapState(stack, world);
        if (mapState != null) {
            return addMapMarker(mapState, world, pos, markerType, displayName);
        }
        return null;
    }

    /**
     * Removes a map marker at the specified
     * BlockPos, if markerType is not null it
     * will only remove the marker at that
     * position if it's of that type.
     *
     * @param mapState   State to edit
     * @param x          X position of the marker
     * @param z          Z position on the marker
     * @param markerType The markerType to remove, null for any.
     * @return Provided MapState
     */
    public static boolean removeMapMarker(MapState mapState, int x, int z, @Nullable MapMarker markerType) {
        MapMarkerEntity removeMapMarker = ((MapStateAccessor) mapState).removeMapMarker(null, x, z, false, markerType);
        if (removeMapMarker != null)
            PinLib.LOGGER.info("Removed map marker with id [{}] at: [{}]", removeMapMarker.getId(), removeMapMarker.getPos().toShortString());
        return removeMapMarker != null;
    }

    /**
     * Tries to remove a map marker from the
     * provided ItemStack if it's a valid
     * map and contains a valid MapState.
     *
     * @param stack      The item stack which the marker will be removed from
     * @param world      World to get the map state from
     * @param markerType Map marker type
     * @return Is successful?
     */
    public static boolean tryRemoveMapMarker(ItemStack stack, World world, BlockPos pos, @Nullable MapMarker markerType, @Nullable Text displayName) {
        MapState mapState = FilledMapItem.getOrCreateMapState(stack, world);
        if (mapState != null) {
            return removeMapMarker(mapState, pos.getX(), pos.getZ(), markerType);
        }
        return false;
    }

    /**
     * Attempts to add a map marker on the provided
     * stack if it is a valid map with a valid map
     * state.
     * <p>
     * If there is already a map marker in the given
     * location, it will try to remove it instead.
     * </p>
     * This behaves as if the player clicked on a
     * block that implements {@link IMapMarkedBlock}
     * with a {@link FilledMapItem}.
     *
     * @param stack    ItemStack to try to get a map state from
     * @param world    World containing the block
     * @param blockPos Position of the block
     * @return Whether the action succeeded or not
     */
    public static boolean tryUseOnMarkableBlock(ItemStack stack, World world, BlockPos blockPos) {
        MapStateAccessor mapState = (MapStateAccessor) FilledMapItem.getOrCreateMapState(stack, world);
        if (mapState == null)
            return false;
        MapMarkerEntity mapMarker = MapMarkerEntity.fromWorldBlock(world, blockPos);
        if (mapState.addMapMarker(world, blockPos, mapMarker)) {
            if (mapMarker == null)
                return false;
            PinLib.LOGGER.info("Added map marker with id [{}] at: [{}]", mapMarker.getId().toString(), blockPos.toShortString());
            return true;
        } else if ((mapMarker = mapState.removeMapMarker(
                null,
                blockPos.getX(),
                blockPos.getZ(),
                !(world.getBlockState(blockPos).getBlock() instanceof IMapMarkedBlock),
                null
        )) != null) {
            PinLib.LOGGER.info("Removed map marker with id [{}] at: [{}]", mapMarker.getId(), blockPos.toShortString());
            return true;
        }
        return false;
    }

    public static MapMarker get(Identifier id) {
        return REGISTRY.get(id);
    }

    public static MapMarker getDefaultMarker() {
        return DEFAULT_MARKER;
    }
}
