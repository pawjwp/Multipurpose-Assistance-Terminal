package net.pawjwp.mat.datagen;

import net.pawjwp.mat.MultipurposeAssistanceTerminal;
import net.minecraft.data.DataGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MultipurposeAssistanceTerminal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new MatRecipes(packOutput));
        generator.addProvider(event.includeServer(), MatLootTables.create(packOutput));

        generator.addProvider(event.includeClient(), new MatBlockStates(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new MatItemModels(packOutput, existingFileHelper));

        MatBlockTags blockTagGenerator = generator.addProvider(event.includeServer(),
                new MatBlockTags(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new MatItemTags(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
    }
}
