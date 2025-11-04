package net.pawjwp.mat.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.pawjwp.mat.MultipurposeAssistanceTerminal;
import net.pawjwp.mat.item.MatItems;
import net.pawjwp.mat.item.TerminalItem;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MatItemModels extends ItemModelProvider {
    public static final String GENERATED = "item/generated";

    public MatItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MultipurposeAssistanceTerminal.MOD_ID, existingFileHelper);
    }

    // Structure and functions taken from:
    // https://github.com/vectorwing/FarmersDelight/blob/1.20/src/main/java/vectorwing/farmersdelight/data/ItemModels.java

    @Override
    protected void registerModels() {
        Set<Item> items = ForgeRegistries.ITEMS.getValues().stream().filter(i -> MultipurposeAssistanceTerminal.MOD_ID.equals(ForgeRegistries.ITEMS.getKey(i).getNamespace()))
                .collect(Collectors.toSet());

        // If needed in the future, exclude specific items here
        // items.remove(MatItems.EXAMPLE_ITEM.get());

        // MAT
        terminalModel(MatItems.MAT.get(), "mat");
        items.remove(MatItems.MAT.get());

        // Blocks whose items look alike
        takeAll(items, i -> i instanceof BlockItem).forEach(item -> blockBasedModel(item, ""));

        // Remaining items
        items.forEach(item -> itemGeneratedModel(item, resourceItem(itemName(item))));
    }

    private String itemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    public void blockBasedModel(Item item, String suffix) {
        withExistingParent(itemName(item), resourceBlock(itemName(item) + suffix));
    }

    public void terminalModel(Item terminalItem, String baseTextureName) {
        final String baseItemName = itemName(terminalItem);
        final String trackingItemName = baseTextureName + "_tracking";
        final String questingItemName = baseTextureName + "_questing";
        final String atlasItemName = baseTextureName + "_atlas";

        ItemModelBuilder baseModel = withExistingParent(baseItemName, GENERATED)
                .texture("layer0", resourceItem(baseItemName));
        ItemModelBuilder trackingModel = withExistingParent(trackingItemName, GENERATED)
                .texture("layer0", resourceItem(trackingItemName));
        ItemModelBuilder questingModel = withExistingParent(questingItemName, GENERATED)
                .texture("layer0", resourceItem(questingItemName));
        ItemModelBuilder atlasModel = withExistingParent(atlasItemName, GENERATED)
                .texture("layer0", resourceItem(atlasItemName));

        for (int i = 0; i <= 32; i++) {
            int frame = (i + 16) & 31;
            String modelName = baseTextureName + "_tracking_" + String.format("%02d", frame);

            float angle = 0.0f;
            if (i != 0) {
                angle = (2f * i - 1f) / 64f;

                withExistingParent(modelName, ResourceLocation.parse(GENERATED))
                        .texture("layer0", resourceItem(modelName));
            }

            baseModel.override()
                    .predicate(ResourceLocation.parse("mode"), TerminalItem.MODE_TRACKING)
                    .predicate(ResourceLocation.parse("angle"), angle)
                    .model(new ModelFile.UncheckedModelFile(resourceItem(modelName)));
        }

        baseModel.override()
                .predicate(ResourceLocation.parse("mode"), TerminalItem.MODE_QUESTING)
                .model(new ModelFile.ExistingModelFile(resourceItem(questingItemName), existingFileHelper));
        baseModel.override()
                .predicate(ResourceLocation.parse("mode"), TerminalItem.MODE_ATLAS)
                .model(new ModelFile.ExistingModelFile(resourceItem(atlasItemName), existingFileHelper));
    }

    public void itemGeneratedModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), GENERATED).texture("layer0", texture);
    }

    public ResourceLocation resourceBlock(String path) {
        return ResourceLocation.fromNamespaceAndPath(MultipurposeAssistanceTerminal.MOD_ID, "block/" + path);
    }

    public ResourceLocation resourceItem(String path) {
        return ResourceLocation.fromNamespaceAndPath(MultipurposeAssistanceTerminal.MOD_ID, "item/" + path);
    }

    @SafeVarargs
    public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
        List<T> ret = Arrays.asList(items);

        for(T item : items) {
            if (!src.contains(item)) {
                MultipurposeAssistanceTerminal.LOGGER.warn("Item {} not found in set", item);
            }
        }

        if (!src.removeAll(ret)) {
            MultipurposeAssistanceTerminal.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
        }

        return ret;
    }

    public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList();
        Iterator<T> iter = src.iterator();

        while(iter.hasNext()) {
            T item = (T)iter.next();
            if (pred.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }

        if (ret.isEmpty()) {
            MultipurposeAssistanceTerminal.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
        }

        return ret;
    }
}
