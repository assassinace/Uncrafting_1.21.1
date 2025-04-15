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
                    output.accept(ModBlocks.UNCRAFTING_TABLE.get());
                    output.accept(ModItems.SAWDUST.get());
                    output.accept(ModItems.COAL_DUST.get());
                    output.accept(ModItems.COAL_NUGGET.get());
                    output.accept(ModItems.RAW_COPPER_DUST.get());
                    output.accept(ModItems.COPPER_DUST.get());
                    output.accept(ModItems.RAW_COPPER_NUGGET.get());
                    output.accept(ModItems.COPPER_NUGGET.get());
                    output.accept(ModItems.RAW_IRON_DUST.get());
                    output.accept(ModItems.IRON_DUST.get());
                    output.accept(ModItems.RAW_IRON_NUGGET.get());
                    output.accept(ModItems.RAW_GOLD_DUST.get());
                    output.accept(ModItems.GOLD_DUST.get());
                    output.accept(ModItems.RAW_GOLD_NUGGET.get());
                    output.accept(ModItems.RAW_DIAMOND_DUST.get());
                    output.accept(ModItems.DIAMOND_DUST.get());
                    output.accept(ModItems.RAW_DIAMOND_NUGGET.get());
                    output.accept(ModItems.DIAMOND_NUGGET.get());
                    output.accept(ModItems.RAW_DIAMOND.get());
                    output.accept(ModItems.RAW_EMERALD_DUST.get());
                    output.accept(ModItems.EMERALD_DUST.get());
                    output.accept(ModItems.RAW_EMERALD_NUGGET.get());
                    output.accept(ModItems.EMERALD_NUGGET.get());
                    output.accept(ModItems.RAW_EMERALD.get());
                    output.accept(ModItems.NETHERITE_DUST.get());
                    output.accept(ModItems.NETHERITE_NUGGET.get());
                }).build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
