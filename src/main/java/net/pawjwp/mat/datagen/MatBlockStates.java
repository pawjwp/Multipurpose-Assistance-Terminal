package net.pawjwp.mat.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.pawjwp.mat.MultipurposeAssistanceTerminal;
import net.pawjwp.mat.block.MatBlocks;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class MatBlockStates extends BlockStateProvider {

    public MatBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MultipurposeAssistanceTerminal.MOD_ID, exFileHelper);
    }

    private String blockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return fromNamespaceAndPath(MultipurposeAssistanceTerminal.MOD_ID, "block/" + path);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(MatBlocks.LIGHT_PLASTEEL_BLOCK.get());
        this.simpleBlock(MatBlocks.DARK_PLASTEEL_BLOCK.get());
    }
}
