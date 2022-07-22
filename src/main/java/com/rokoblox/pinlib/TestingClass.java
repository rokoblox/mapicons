package com.rokoblox.pinlib;

import com.rokoblox.pinlib.mapmarker.MapMarkedBlock;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

public class TestingClass {

    public static final MapMarker TESTMARKER = PinLib.createDynamicMarker(new Identifier("pinlib", "testmarker"));
    public static final Block TESTBLOCK = new TestBlock(FabricBlockSettings.of(Material.STONE));
    public static void init(Logger LOGGER) {
        Registry.register(Registry.BLOCK, new Identifier("pinlib", "testblock"), TESTBLOCK);
    }
}

class TestBlock extends Block implements MapMarkedBlock {
    public TestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        MapState mapState = FilledMapItem.getOrCreateMapState(player.getStackInHand(hand), world);
        if (mapState != null) {
            PinLib.LOGGER.info("Adding custom marker to mapstate");
            PinLib.addMapMarker(mapState, world, TestingClass.TESTMARKER, pos);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Text getMapMarkerName() {
        return null;
    }

    @Override
    public MapMarker getCustomMarker() {
        return TestingClass.TESTMARKER;
    }
}
