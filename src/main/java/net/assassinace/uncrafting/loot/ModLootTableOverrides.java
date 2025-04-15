package net.assassinace.uncrafting.loot;

import net.assassinace.uncrafting.Uncrafting;
import net.assassinace.uncrafting.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Uncrafting.MOD_ID)
public class ModLootTableOverrides {

    private static final Map<ResourceLocation, OreDropConfig> ORE_OVERRIDES = Map.ofEntries(
            // Coal
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/coal_ore"),
                    new OreDropConfig(() -> Items.COAL, ModItems.COAL_DUST::get, ModItems.COAL_NUGGET::get)),
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_coal_ore"),
                    new OreDropConfig(() -> Items.COAL, ModItems.COAL_DUST::get, ModItems.COAL_NUGGET::get)),

            // Copper
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/copper_ore"),
                    new OreDropConfig(() -> Items.RAW_COPPER, ModItems.RAW_COPPER_DUST::get, ModItems.RAW_COPPER_NUGGET::get)),
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_copper_ore"),
                    new OreDropConfig(() -> Items.RAW_COPPER, ModItems.RAW_COPPER_DUST::get, ModItems.RAW_COPPER_NUGGET::get)),

            // Iron
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/iron_ore"),
                    new OreDropConfig(() -> Items.RAW_IRON, ModItems.RAW_IRON_DUST::get, ModItems.RAW_IRON_NUGGET::get)),
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_iron_ore"),
                    new OreDropConfig(() -> Items.RAW_IRON, ModItems.RAW_IRON_DUST::get, ModItems.RAW_IRON_NUGGET::get)),

            // Gold
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/gold_ore"),
                    new OreDropConfig(() -> Items.RAW_GOLD, ModItems.RAW_GOLD_DUST::get, ModItems.RAW_GOLD_NUGGET::get)),
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_gold_ore"),
                    new OreDropConfig(() -> Items.RAW_GOLD, ModItems.RAW_GOLD_DUST::get, ModItems.RAW_GOLD_NUGGET::get)),
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/nether_gold_ore"),
                    new OreDropConfig(() -> Items.RAW_GOLD, ModItems.RAW_GOLD_DUST::get, ModItems.RAW_GOLD_NUGGET::get)),

            // Diamond
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/diamond_ore"),
                    new OreDropConfig(ModItems.RAW_DIAMOND::get, ModItems.RAW_DIAMOND_DUST::get, ModItems.RAW_DIAMOND_NUGGET::get)),
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_diamond_ore"),
                    new OreDropConfig(ModItems.RAW_DIAMOND::get, ModItems.RAW_DIAMOND_DUST::get, ModItems.RAW_DIAMOND_NUGGET::get)),

            // Emerald
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/emerald_ore"),
                    new OreDropConfig(ModItems.RAW_EMERALD::get, ModItems.RAW_EMERALD_DUST::get, ModItems.RAW_EMERALD_NUGGET::get)),
            Map.entry(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/deepslate_emerald_ore"),
                    new OreDropConfig(ModItems.RAW_EMERALD::get, ModItems.RAW_EMERALD_DUST::get, ModItems.RAW_EMERALD_NUGGET::get))
    );

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation tableId = event.getName();

        // Apply sawdust drop to all logs
        if (tableId.getNamespace().equals("minecraft") && tableId.getPath().startsWith("blocks/") && tableId.getPath().endsWith("_log")) {
            event.getTable().addPool(LootPool.lootPool()
                    .name("uncrafting_sawdust_log_bonus")
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(ModItems.SAWDUST.get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                    .build());
            System.out.println("[Uncrafting] Added sawdust bonus drop to: " + tableId);
        }

        // ORE OVERRIDES
        if (ORE_OVERRIDES.containsKey(tableId)) {
            try {
                Field poolsField = LootTable.class.getDeclaredField("pools");
                poolsField.setAccessible(true);
                List<?> pools = (List<?>) poolsField.get(event.getTable());
                pools.clear();
                System.out.println("[Uncrafting] Cleared and replaced loot for: " + tableId);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            OreDropConfig config = ORE_OVERRIDES.get(tableId);

            event.getTable().addPool(LootPool.lootPool()
                    .name("uncrafting_main")
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(config.mainDrop().get()))
                    .build());

            event.getTable().addPool(LootPool.lootPool()
                    .name("uncrafting_dust")
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(config.dust().get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                    .build());

            event.getTable().addPool(LootPool.lootPool()
                    .name("uncrafting_nugget")
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(config.nugget().get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
                    .build());
        }
    }
    private record OreDropConfig(Supplier<Item> mainDrop,
                                 Supplier<Item> dust,
                                 Supplier<Item> nugget) {}
}
