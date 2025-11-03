package net.pawjwp.mat.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.mat.MultipurposeAssistanceTerminal;
import net.pawjwp.mat.block.MatBlocks;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class MatItems {
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_ITEMS = new LinkedHashSet<>();;

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MultipurposeAssistanceTerminal.MOD_ID);

    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> block = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(block);
        return block;
    }

    // Item registry
    public static final RegistryObject<Item> MAT = ITEMS.register("mat",
            () -> new TerminalItem(new Item.Properties()));
    public static final RegistryObject<Item> LOCATION_DATA_CHIP = ITEMS.register("location_data_chip",
            () -> new Item(new Item.Properties()));


    // Block item registry
    public static final RegistryObject<Item> DARK_PLASTEEL_BLOCK = registerWithTab("dark_plasteel_block",
            () -> new BlockItem(MatBlocks.DARK_PLASTEEL_BLOCK.get(), basicItem()));
    public static final RegistryObject<Item> LIGHT_PLASTEEL_BLOCk = registerWithTab("light_plasteel_block",
            () -> new BlockItem(MatBlocks.LIGHT_PLASTEEL_BLOCK.get(), basicItem()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static Item.Properties basicItem() {
        return new Item.Properties();
    }
}
