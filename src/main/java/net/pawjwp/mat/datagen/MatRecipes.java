package net.pawjwp.mat.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.pawjwp.mat.datagen.recipe.MatCraftingRecipes;

import java.util.function.Consumer;

public class MatRecipes extends RecipeProvider implements IConditionBuilder {
    public MatRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        MatCraftingRecipes.register(consumer);
    }
}
