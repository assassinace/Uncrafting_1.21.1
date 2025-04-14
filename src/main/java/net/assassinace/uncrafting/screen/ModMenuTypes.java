package net.assassinace.uncrafting.screen;

import net.assassinace.uncrafting.Uncrafting;
import net.assassinace.uncrafting.screen.custom.UncraftingTableMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Uncrafting.MOD_ID);

    public static final RegistryObject<MenuType<UncraftingTableMenu>> UNCRAFTING_TABLE_MENU =
            MENUS.register("uncrafting_table_menu",
                    () -> new MenuType<>(
                            (windowId, inv) -> {
                                throw new IllegalStateException("Uncrafting Table must be opened via BlockEntity#createMenu()");
                            },
                            FeatureFlags.VANILLA_SET
                    ));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
