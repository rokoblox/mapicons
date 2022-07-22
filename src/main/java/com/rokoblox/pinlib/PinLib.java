package com.rokoblox.pinlib;

import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PinLib implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("PinLib");

    private static final Registry<MapMarker> registry = FabricRegistryBuilder.createDefaulted(MapMarker.class, new Identifier("pinlib", "map_markers"), new Identifier("pinlib", "default")).buildAndRegister();

    /**
     * This is invoked by fabric; do not call.
     */
    @Override
    public void onInitialize() {
        createStaticMarker(new Identifier("pinlib", "default"));
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
        return Registry.register(registry, id, new MapMarker(id, false));
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
        return Registry.register(registry, id, new MapMarker(id, true));
    }

    /**
     * @TODO: 22/07/2022
     * @param mapState Modified MapState
     * @param marker Added MapMarker
     * @param pos Block Position to place marker
     * @return Provided MapState
     */
    public static MapState addMapMarker(MapState mapState, MapMarker marker, BlockPos pos) {
        return mapState;
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
        return registry.get(id);
    }
}
