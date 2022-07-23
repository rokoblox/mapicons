package com.rokoblox.pinlib;

import com.rokoblox.pinlib.mapmarker.MapMarkedBlock;
import com.rokoblox.pinlib.mapmarker.MapMarker;
import com.rokoblox.pinlib.mapmarker.NamedMapMarkedBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

import javax.naming.Name;

public class TestingClass {

    public static final MapMarker TESTMARKER = PinLib.createDynamicMarker(new Identifier("pinlib", "testmarker"));
    public static final Block TESTBLOCK = new TestBlock(FabricBlockSettings.of(Material.STONE));
    public static void init(Logger LOGGER) {
        Registry.register(Registry.BLOCK, new Identifier("pinlib", "testblock"), TESTBLOCK);
    }
}

class TestBlock extends Block implements NamedMapMarkedBlock {
    public TestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (PinLib.tryAddMapMarker(player.getStackInHand(hand), world, pos, TestingClass.TESTMARKER, null) != null)
            return ActionResult.SUCCESS;
        return ActionResult.FAIL;
    }

    @Override
    public Text getMapMarkerName() {
        return Text.literal("Hello world!");
    }

    @Override
    public MapMarker getCustomMarker() {
        return TestingClass.TESTMARKER;
    }
}
