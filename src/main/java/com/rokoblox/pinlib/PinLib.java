package com.rokoblox.pinlib;

import com.rokoblox.pinlib.access.MapStateAccessor;
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
        TestingClass.init(LOGGER); // Used for testing purposes only.
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
        MapMarkerEntity mapMarker;
        if (markerType == null)
            mapMarker = MapMarkerEntity.fromWorldBlock(world, pos);
        else
            mapMarker = new MapMarkerEntity(markerType, pos, displayName);
        return ((MapStateAccessor) mapState).addMapMarker(world, pos, mapMarker) ? mapMarker : null;
    }

    /**
     * Tries to add a map marker to the
     * provided ItemStack if it's a valid
     * map and contains a valid MapState.
     *
     * @param stack      The item stack which the marker will be added to
     * @param world      Not sure what this does, it's just something minecraft has in its code that apparently changes icon rotation based on lighting...?
     * @param markerType Map marker type
     * @return Provided MapState
     */
    public static MapMarkerEntity tryAddMapMarker(ItemStack stack, @Nullable World world, BlockPos pos, @Nullable MapMarker markerType, @Nullable Text displayName) {
        MapState mapState = FilledMapItem.getOrCreateMapState(stack, world);
        if (mapState != null) {
            return addMapMarker(mapState, world, pos, markerType, displayName);
        }
        return null;
    }

    /**
     * @TODO: 22/07/2022
     * @param mapState Modified MapState
     * @param marker Removed MapMarker
     * @param pos Block Position to remove marker
     * @return Provided MapState
     */
    public static MapState removeMapMarker(MapState mapState, MapMarker marker, BlockPos pos) {
        return mapState;
    }

    public static MapMarker get(Identifier id) {
        return REGISTRY.get(id);
    }
    public static MapMarker getDefaultMarker() {
        return DEFAULT_MARKER;
    }
}
