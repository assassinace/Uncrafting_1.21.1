package net.assassinace.uncrafting.client;

import net.assassinace.uncrafting.client.gui.ExtendedInventoryScreen;
import net.assassinace.uncrafting.menu.ExtendedInventoryMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "uncrafting", value = Dist.CLIENT)
public class ClientScreenInterceptor {

    private static boolean ignoreNextScreen = false;

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        Screen screen = event.getScreen();
        Player player = Minecraft.getInstance().player;

        // Only intercept survival inventory (not creative)
        if (screen instanceof InventoryScreen && player != null && !player.isCreative()) {
            event.setCanceled(true);

            int windowId = 0; // Use a real counter if multiplayer support is needed
            Inventory inv = player.getInventory();
            ExtendedInventoryMenu menu = new ExtendedInventoryMenu(windowId, inv);
            Minecraft.getInstance().setScreen(new ExtendedInventoryScreen(menu, inv, player.getDisplayName()));
        }
    }

}
