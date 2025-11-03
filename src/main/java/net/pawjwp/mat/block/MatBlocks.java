package net.pawjwp.mat.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.mat.MultipurposeAssistanceTerminal;

public class MatBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MultipurposeAssistanceTerminal.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> DARK_PLASTEEL_BLOCK = BLOCKS.register("dark_plasteel_block",
            () -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK).strength(1.8F, 6.0F).mapColor(MapColor.COLOR_PURPLE)));
    public static final RegistryObject<Block> LIGHT_PLASTEEL_BLOCK = BLOCKS.register("light_plasteel_block",
            () -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK).strength(1.8F, 6.0F).mapColor(MapColor.SNOW)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
