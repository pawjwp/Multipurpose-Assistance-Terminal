package net.pawjwp.mat.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.pawjwp.mat.MultipurposeAssistanceTerminal;

public class MatCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MultipurposeAssistanceTerminal.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAT_TAB = CREATIVE_MODE_TABS.register("mat_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(MatItems.MAT.get()))
            .title(Component.translatable("creativetab.mat_tab"))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(MatItems.MAT.get());
                pOutput.accept(MatItems.LOCATION_DATA_CHIP.get());
            })
            .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
