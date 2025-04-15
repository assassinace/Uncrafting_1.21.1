package net.assassinace.uncrafting.item;

import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.ItemStack;

import static net.assassinace.uncrafting.item.ModItems.*;

@Mod.EventBusSubscriber(modid = "uncrafting")
public class ModFuelHandler {

    @SubscribeEvent
    public static void onFuelBurn(FurnaceFuelBurnTimeEvent event) {
        ItemStack fuel = event.getItemStack();

        if (fuel.is(COAL_NUGGET.get())) {
            // Coal burns for 1600 ticks → nugget = 1600 / 9 = ~177
            event.setBurnTime(177);
        } else if (fuel.is(COAL_DUST.get())) {
            // Dust = 1/9 of nugget = 177 / 9 = ~19
            event.setBurnTime(19);
        } else if (fuel.is(SAWDUST.get())) {
            // Stick burns for 100 ticks → sawdust = 100 / 6 = ~16
            event.setBurnTime(16);
        }
    }
}
