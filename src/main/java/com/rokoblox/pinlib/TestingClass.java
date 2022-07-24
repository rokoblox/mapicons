package com.rokoblox.pinlib;

import com.rokoblox.pinlib.mapmarker.MapMarkedBlock;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Nameable;
import net.minecraft.util.registry.Registry;

public class TestingClass {

    public static MapMarker TESTMARKER;
    public static Block TESTBLOCK;
    public static void init() {
        TESTMARKER = PinLib.createDynamicMarker(new Identifier("pinlib", "testmarker"));
        TESTBLOCK = new TestBlock(FabricBlockSettings.of(Material.STONE));
        Registry.register(Registry.BLOCK, new Identifier("pinlib", "testblock"), TESTBLOCK);
    }
}

class TestBlock extends Block implements MapMarkedBlock, Nameable {
    public TestBlock(Settings settings) {
        super(settings);
    }

    @Override // Default method returns default marker (useful for quick debugging).
    public MapMarker getCustomMarker() {
        return TestingClass.TESTMARKER;
    }

    @Override // Default method returns 0xFFFFFFFF (white)
    public int getMarkerColor() {
        // Use net.minecraft.util.math.ColorHelper to get integer from RGB values.
        return 0xFFFF0000; // (argb) -> 255, 255, 0, 0
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Hello world!");
    }
}
