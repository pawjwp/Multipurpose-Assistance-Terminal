package net.pawjwp.mat.client;

import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.pawjwp.mat.MultipurposeAssistanceTerminal;
import net.pawjwp.mat.item.MatItems;
import net.pawjwp.mat.item.TerminalItem;


@Mod.EventBusSubscriber(
        modid = MultipurposeAssistanceTerminal.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class MatItemProperties {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    MatItems.MAT.get(),
                    ResourceLocation.parse("angle"),
                    new CompassItemPropertyFunction((level, stack, entity) -> {
                        return TerminalItem.hasTarget(stack) ? TerminalItem.getTargetPosition(stack.getOrCreateTag()): null;
                    })
            );
        });
    }
}
