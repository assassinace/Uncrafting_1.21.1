package net.assassinace.uncrafting.item;

import net.assassinace.uncrafting.Uncrafting;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Uncrafting.MOD_ID);

    public static final RegistryObject<Item> SAWDUST = ITEMS.register("sawdust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COAL_DUST = ITEMS.register("coal_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COAL_NUGGET = ITEMS.register("coal_nugget",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_COPPER_DUST = ITEMS.register("raw_copper_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_DUST = ITEMS.register("copper_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_COPPER_NUGGET = ITEMS.register("raw_copper_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_IRON_DUST = ITEMS.register("raw_iron_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_IRON_NUGGET = ITEMS.register("raw_iron_nugget",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_GOLD_DUST = ITEMS.register("raw_gold_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_DUST = ITEMS.register("gold_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_GOLD_NUGGET = ITEMS.register("raw_gold_nugget",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_DIAMOND_DUST = ITEMS.register("raw_diamond_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_DUST = ITEMS.register("diamond_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_DIAMOND_NUGGET = ITEMS.register("raw_diamond_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_NUGGET = ITEMS.register("diamond_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_DIAMOND = ITEMS.register("raw_diamond",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_EMERALD_DUST = ITEMS.register("raw_emerald_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EMERALD_DUST = ITEMS.register("emerald_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_EMERALD_NUGGET = ITEMS.register("raw_emerald_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EMERALD_NUGGET = ITEMS.register("emerald_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_EMERALD = ITEMS.register("raw_emerald",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NETHERITE_DUST = ITEMS.register("netherite_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_NUGGET = ITEMS.register("netherite_nugget",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
