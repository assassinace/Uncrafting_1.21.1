package net.assassinace.uncrafting.client;

import net.assassinace.uncrafting.client.gui.ExtendedInventoryScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
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

        if (ignoreNextScreen) {
            ignoreNextScreen = false;
            return;
        }

        // Only intercept survival inventory (not creative)
        if (screen instanceof InventoryScreen) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ignoreNextScreen = true;
                event.setCanceled(true);
                Minecraft.getInstance().setScreen(new ExtendedInventoryScreen(player));
            }
        }
    }
}
