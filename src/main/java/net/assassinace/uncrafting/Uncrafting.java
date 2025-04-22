package net.assassinace.uncrafting;

import com.mojang.logging.LogUtils;
import net.assassinace.uncrafting.block.ModBlocks;
import net.assassinace.uncrafting.block.entity.ModBlockEntities;
import net.assassinace.uncrafting.item.ModCreativeModeTabs;
import net.assassinace.uncrafting.item.ModItems;
import net.assassinace.uncrafting.screen.ModMenuTypes;
import net.assassinace.uncrafting.screen.custom.UncraftingTableScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Uncrafting.MOD_ID)
public class Uncrafting {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "uncrafting";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public Uncrafting() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {

            event.accept(ModItems.SAWDUST);

            event.accept(ModItems.COAL_DUST);
            event.accept(ModItems.COAL_NUGGET);

            event.accept(ModItems.RAW_COPPER_DUST);
            event.accept(ModItems.COPPER_DUST);
            event.accept(ModItems.RAW_COPPER_NUGGET);
            event.accept(ModItems.COPPER_NUGGET);

            event.accept(ModItems.RAW_IRON_DUST);
            event.accept(ModItems.IRON_DUST);
            event.accept(ModItems.RAW_IRON_NUGGET);

            event.accept(ModItems.RAW_GOLD_DUST);
            event.accept(ModItems.GOLD_DUST);
            event.accept(ModItems.RAW_GOLD_NUGGET);

            event.accept(ModItems.RAW_DIAMOND_DUST);
            event.accept(ModItems.DIAMOND_DUST);
            event.accept(ModItems.RAW_DIAMOND_NUGGET);
            event.accept(ModItems.DIAMOND_NUGGET);
            event.accept(ModItems.RAW_DIAMOND);

            event.accept(ModItems.RAW_EMERALD_DUST);
            event.accept(ModItems.EMERALD_DUST);
            event.accept(ModItems.RAW_EMERALD_NUGGET);
            event.accept(ModItems.EMERALD_NUGGET);
            event.accept(ModItems.RAW_EMERALD);

            event.accept(ModItems.NETHERITE_DUST);
            event.accept(ModItems.NETHERITE_NUGGET);

        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.UNCRAFTING_TABLE);
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.COPPER_SHOVEL);
            event.accept(ModItems.COPPER_PICKAXE);
            event.accept(ModItems.COPPER_AXE);
            event.accept(ModItems.COPPER_HOE);

            event.accept(ModItems.EMERALD_SHOVEL);
            event.accept(ModItems.EMERALD_PICKAXE);
            event.accept(ModItems.EMERALD_AXE);
            event.accept(ModItems.EMERALD_HOE);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.COPPER_SWORD);
            event.accept(ModItems.EMERALD_SWORD);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.UNCRAFTING_TABLE_MENU.get(), UncraftingTableScreen::new);
        }
    }
}