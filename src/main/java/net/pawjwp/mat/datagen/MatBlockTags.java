package net.pawjwp.mat.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.pawjwp.mat.MultipurposeAssistanceTerminal;
import net.pawjwp.mat.tag.MatTags;
import org.jetbrains.annotations.Nullable;
import net.pawjwp.mat.block.MatBlocks;

import java.util.concurrent.CompletableFuture;

public class MatBlockTags extends BlockTagsProvider {

    public MatBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MultipurposeAssistanceTerminal.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        // Mineability Tags
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                MatBlocks.
                        LIGHT_PLASTEEL_BLOCK.get(),
                MatBlocks.DARK_PLASTEEL_BLOCK.get()
        );

        // Forge Tags
        this.tag(MatTags.STORAGE_BLOCKS_BLOCK_PLASTEEL).add(
                MatBlocks.LIGHT_PLASTEEL_BLOCK.get(),
                MatBlocks.DARK_PLASTEEL_BLOCK.get()
        );
        this.tag(MatTags.STORAGE_BLOCKS_BLOCK_LIGHT_PLASTEEL).add(MatBlocks.LIGHT_PLASTEEL_BLOCK.get());
        this.tag(MatTags.STORAGE_BLOCKS_BLOCK_DARK_PLASTEEL).add(MatBlocks.DARK_PLASTEEL_BLOCK.get());

    }
}
