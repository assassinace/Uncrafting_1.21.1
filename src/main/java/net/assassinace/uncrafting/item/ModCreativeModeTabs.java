package net.assassinace.uncrafting.item;

import net.assassinace.uncrafting.Uncrafting;
import net.assassinace.uncrafting.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Uncrafting.MOD_ID);

public static final RegistryObject<CreativeModeTab> UNCRAFTING_TABLE = CREATIVE_MODE_TABS.register("uncrafting_tab",
        () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.UNCRAFTING_TABLE.get()))
                .title(Component.translatable("creativetab.uncrafting.uncrafting"))
                .displayItems((ItemDisplayParameters, output) -> {
                    output.accept(ModItems.DIAMOND_NUGGET.get());
                    output.accept(ModBlocks.UNCRAFTING_TABLE.get());

                }).build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
