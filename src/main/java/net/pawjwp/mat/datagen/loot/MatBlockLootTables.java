package net.pawjwp.mat.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.mat.block.MatBlocks;

import java.util.Set;

public class MatBlockLootTables extends BlockLootSubProvider {
    public MatBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(MatBlocks.DARK_PLASTEEL_BLOCK.get());
        this.dropSelf(MatBlocks.LIGHT_PLASTEEL_BLOCK.get());
        //this.dropOther(MATBlocks.EXAMPLE_BLOCK.get(), MATItems.OTHER_ITEM.get());

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return MatBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}