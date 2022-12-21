package com.rokoblox.pinlib;

import com.rokoblox.pinlib.mapmarker.IMapMarkedBlock;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registry;
import net.minecraft.world.BlockView;

public class TestingClass {

    public static MapMarker TESTMARKER;
    public static Block TESTBLOCK;
    public static boolean isRed;
    public static void init() {
        TESTMARKER = PinLib.createDynamicMarker(new Identifier("pinlib", "testmarker"));
        TESTBLOCK = new TestBlock(FabricBlockSettings.of(Material.STONE));
        Registry.register(Registries.BLOCK, new Identifier("pinlib", "testblock"), TESTBLOCK);

        // Map marked blocks should NOT be registered like this,
        // this is just to demonstrate how to switch from the
        // deprecated IMapMarkedBlock to the new registering system.
        PinLib.registerMapMarkedBlock(
                TESTBLOCK,
                () -> TESTMARKER,
                (world, pos) -> 0xFFFFFFFFL,
                (world, pos) -> Text.literal("Hello world!").setStyle(Style.EMPTY)
        );
        ServerTickEvents.END_SERVER_TICK.register(server -> isRed = !isRed);
    }
}

class TestBlock extends Block {
    public TestBlock(Settings settings) {
        super(settings);
    }
}

// Deprecated IMapMarkedBlock implementation, use above example instead.
//class TestBlock extends Block implements IMapMarkedBlock {
//    public TestBlock(Settings settings) {
//        super(settings);
//    }
//
//    @Override // Default method returns default marker (useful for quick debugging).
//    public MapMarker getCustomMarker() {
//        return PinLib.get(new Identifier("pinlib", "testmarker"));
//    }
//
//    @Override // Default method returns 0xFFFFFFFFL (white)
//    public long getMarkerColor(BlockView world, BlockPos pos) {
//        // Use net.minecraft.util.math.ColorHelper to get integer from RGB values.
//        return 0xFFFFFFFFL; // (argb) -> 255, 255, 0, 0
//    }
//
//    @Override
//    public Text getDisplayName(BlockView world, BlockPos pos) {
//        // It's a good idea to not leave style as null since the loaded
//        // map markers from NBT always load with an empty style, thus
//        // the game thinks the loaded display name and the one here
//        // are unequal causing an unnecessary update to the map marker.
//        return Text.literal("Hey there!").setStyle(Style.EMPTY);
//    }
//}
