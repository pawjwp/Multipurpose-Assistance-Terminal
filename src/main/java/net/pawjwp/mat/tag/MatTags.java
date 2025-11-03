package net.pawjwp.mat.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MatTags {
    // Block tags
    public static final TagKey<Block> STORAGE_BLOCKS_BLOCK_PLASTEEL =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/plasteel"));
    public static final TagKey<Block> STORAGE_BLOCKS_BLOCK_DARK_PLASTEEL =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/dark_plasteel"));
    public static final TagKey<Block> STORAGE_BLOCKS_BLOCK_LIGHT_PLASTEEL =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/light_plasteel"));



    // Item tags
    public static final TagKey<Item> STORAGE_BLOCKS_ITEM_PLASTEEL =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/plasteel"));
    public static final TagKey<Item> STORAGE_BLOCKS_ITEM_DARK_PLASTEEL =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/dark_plasteel"));
    public static final TagKey<Item> STORAGE_BLOCKS_ITEM_LIGHT_PLASTEEL =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/light_plasteel"));

    public static final TagKey<Item> DUSTS_REDSTONE =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "dusts/redstone"));
}
