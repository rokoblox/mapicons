package com.rokoblox.pinlib;

import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PinLib implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("PinLib");

    private static final Registry<MapMarker> registry = FabricRegistryBuilder.createDefaulted(MapMarker.class, new Identifier("pinlib", "map_markers"), new Identifier("pinlib", "default")).buildAndRegister();

    /**
     * This should only be invoked by fabric; do not invoke.
     */
    @Override
    public void onInitialize() {
    }
}
