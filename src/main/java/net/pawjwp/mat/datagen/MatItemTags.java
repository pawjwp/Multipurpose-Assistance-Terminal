package net.pawjwp.mat.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.pawjwp.mat.MultipurposeAssistanceTerminal;
import net.pawjwp.mat.item.MatItems;
import net.pawjwp.mat.tag.MatTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MatItemTags extends ItemTagsProvider {
    public MatItemTags(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, MultipurposeAssistanceTerminal.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        // Minecraft Tags
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(
                MatItems.MAT.get()
        );

        // Forge tags
        this.tag(MatTags.STORAGE_BLOCKS_ITEM_PLASTEEL).add(
                MatItems.DARK_PLASTEEL_BLOCK.get(),
                MatItems.LIGHT_PLASTEEL_BLOCk.get()
        );
        this.tag(MatTags.STORAGE_BLOCKS_ITEM_DARK_PLASTEEL).add(MatItems.DARK_PLASTEEL_BLOCK.get());
        this.tag(MatTags.STORAGE_BLOCKS_ITEM_LIGHT_PLASTEEL).add(MatItems.LIGHT_PLASTEEL_BLOCk.get());

    }
}
