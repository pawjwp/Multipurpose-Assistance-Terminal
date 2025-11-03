package net.pawjwp.mat.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.pawjwp.mat.datagen.loot.MatBlockLootTables;

import java.util.List;
import java.util.Set;

public class MatLootTables {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(MatBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}