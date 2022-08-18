package com.rokoblox.pinlib;

import com.rokoblox.pinlib.mapmarker.IMapMarkedBlock;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class TestingClass {

    public static MapMarker TESTMARKER;
    public static Block TESTBLOCK;
    public static boolean isRed;
    public static void init() {
        TESTMARKER = PinLib.createDynamicMarker(new Identifier("pinlib", "testmarker"));
        TESTBLOCK = new TestBlock(FabricBlockSettings.of(Material.STONE));
        Registry.register(Registry.BLOCK, new Identifier("pinlib", "testblock"), TESTBLOCK);
        ServerTickEvents.END_SERVER_TICK.register(server -> isRed = !isRed);
    }
}

class TestBlock extends Block implements IMapMarkedBlock {
    public TestBlock(Settings settings) {
        super(settings);
    }

    @Override // Default method returns default marker (useful for quick debugging).
    public MapMarker getCustomMarker() {
        return PinLib.get(new Identifier("pinlib", "testmarker"));
    }

    @Override // Default method returns 0xFFFFFFFFL (white)
    public long getMarkerColor(BlockView world, BlockPos pos) {
        // Use net.minecraft.util.math.ColorHelper to get integer from RGB values.
        return 0xFFFFFFFFL; // (argb) -> 255, 255, 0, 0
    }

    @Override
    public Text getDisplayName(BlockView world, BlockPos pos) {
        // It's a good idea to not leave style as null since the loaded
        // map markers from NBT always load with an empty style, thus
        // the game thinks the loaded display name and the one here
        // are unequal causing an unnecessary update to the map marker.
        return Text.literal("Hey there!").setStyle(Style.EMPTY);
    }
}
