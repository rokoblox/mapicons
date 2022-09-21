package com.rokoblox.pinlib;

import com.rokoblox.pinlib.access.MapStateAccessor;
import com.rokoblox.pinlib.mapmarker.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

import java.util.HashMap;

public class PinLib implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("PinLib");
    private static final Registry<MapMarker> MAP_MARKER_REGISTRY = FabricRegistryBuilder.createDefaulted(MapMarker.class, new Identifier("pinlib", "map_markers"), new Identifier("pinlib", "default")).buildAndRegister();
    private static final Registry<MapMarkedBlock> MAP_MARKED_BLOCKS_REGISTRY = FabricRegistryBuilder.createSimple(MapMarkedBlock.class, new Identifier("pinlib", "map_marked_blocks")).buildAndRegister();
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
        return Registry.register(MAP_MARKER_REGISTRY, id, new MapMarker(id, false));
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
        return Registry.register(MAP_MARKER_REGISTRY, id, new MapMarker(id, true));
    }

    /**
     * Creates and registers a {@link com.rokoblox.pinlib.mapmarker.MapMarkedBlock}
     * for the provided block with
     * the provided methods.
     *
     * @param entry               Block to register
     * @param markerProvider      You can use `() -> PinLib.getDefaultMarker()` as a default value.
     * @param colorProvider       You can use `(world, pos) -> 0xFFFFFFFFL` as a default value.
     * @param displayNameProvider You can use `(BlockView world, BlockPos pos) -> null` as a default value.
     * @return Provided entry
     */
    public static MapMarkedBlock registerMapMarkedBlock(Block entry, CustomMarkerProvider markerProvider, MarkerColorProvider colorProvider, MarkerDisplayNameProvider displayNameProvider) {
        Identifier id = Registry.BLOCK.getId(entry);
        if (id == Registry.BLOCK.getDefaultId())
            LOGGER.warn("Registering default ID [{}] as a map marked block, this might be because the provided block entry was not registered as a block first.", id.toString());
        LOGGER.info("Registering block with id [{}] as a map marked block.", id.toString());
        return Registry.register(MAP_MARKED_BLOCKS_REGISTRY, id, new MapMarkedBlock(entry, markerProvider, colorProvider, displayNameProvider));
    }

    /**
     * Returns a {@link MapMarkedBlock} for the provided
     * block if the block was previously registered with
     * registerMapMarkedBlock(...) or null if not.
     * <p>
     * This method uses caching for performance,
     * manually optimizing results is not needed.
     * </p>
     *
     * @param block Block to find {@link MapMarkedBlock} for
     * @return Matching {@link MapMarkedBlock}, null if none was found in registry.
     */
    public static MapMarkedBlock getMapMarkedBlock(Block block) {
        return MapMarkedBlockCache.fromBlock(block);
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
    @SuppressWarnings("deprecation")
    public static boolean tryUseOnMarkableBlock(ItemStack stack, World world, BlockPos blockPos) {
        MapStateAccessor mapState = (MapStateAccessor) FilledMapItem.getOrCreateMapState(stack, world);
        if (mapState == null)
            return false;
        MapMarkerEntity mapMarker = MapMarkerEntity.fromWorldBlock(world, blockPos);
        BlockState blockState = world.getBlockState(blockPos);
        if (mapState.addMapMarker(world, blockPos, mapMarker)) {
            if (mapMarker == null)
                return false;
            PinLib.LOGGER.info("Added map marker with id [{}] at: [{}]", mapMarker.getId().toString(), blockPos.toShortString());
            return true;
        } else if ((mapMarker = mapState.removeMapMarker(
                null,
                blockPos.getX(),
                blockPos.getZ(),
                !(blockState.getBlock() instanceof IMapMarkedBlock) && (getMapMarkedBlock(blockState.getBlock()) == null),
                null
        )) != null) {
            PinLib.LOGGER.info("Removed map marker with id [{}] at: [{}]", mapMarker.getId(), blockPos.toShortString());
            return true;
        }
        return false;
    }

    public static MapMarker get(Identifier id) {
        return MAP_MARKER_REGISTRY.get(id);
    }

    public static MapMarker getDefaultMarker() {
        return DEFAULT_MARKER;
    }

    static class MapMarkedBlockCache {
        private static final HashMap<Block, MapMarkedBlock> cache = new HashMap<>();

        public static MapMarkedBlock fromBlock(Block block) {
            if (cache.containsKey(block))
                return cache.get(block);
            else {
                MapMarkedBlock entry = MAP_MARKED_BLOCKS_REGISTRY.get(Registry.BLOCK.getId(block));
                cache.put(block, entry);
                return entry;
            }
        }
    }
}