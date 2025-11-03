package net.pawjwp.mat.datagen.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.pawjwp.mat.item.MatItems;
import net.pawjwp.mat.tag.MatTags;

import java.util.function.Consumer;

public class MatCraftingRecipes {
    public static void register(Consumer<FinishedRecipe> consumer) {
        // Shaped
        /*        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MatItems.EXAMPLE_BLOCK.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', MatItems.OTHER_ITEM.get())
                .unlockedBy("has_other_item", InventoryChangeTrigger.TriggerInstance.hasItems(MatItems.OTHER_ITEM.get()))
                .save(consumer);*/

        // Shapeless
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, MatItems.MAT.get())
                .requires(MatTags.STORAGE_BLOCKS_ITEM_PLASTEEL)
                .requires(MatTags.DUSTS_REDSTONE)
                .unlockedBy("has_other_item", InventoryChangeTrigger.TriggerInstance.hasItems(MatItems.LIGHT_PLASTEEL_BLOCk.get()))
                .save(consumer);
    }
}
