package net.assassinace.uncrafting.item;

import net.assassinace.uncrafting.Uncrafting;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Uncrafting.MOD_ID);

    // Wood Items
    public static final RegistryObject<Item> SAWDUST = ITEMS.register("sawdust",
            () -> new Item(new Item.Properties()));

    // Coal Items
    public static final RegistryObject<Item> COAL_DUST = ITEMS.register("coal_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COAL_NUGGET = ITEMS.register("coal_nugget",
            () -> new Item(new Item.Properties()));

    // Copper Items
    public static final RegistryObject<Item> RAW_COPPER_DUST = ITEMS.register("raw_copper_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_DUST = ITEMS.register("copper_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_COPPER_NUGGET = ITEMS.register("raw_copper_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_SWORD = ITEMS.register("copper_sword",
            () -> new SwordItem(ModToolTiers.COPPER, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.COPPER,3,-2.4f))));
    public static final RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe",
            () -> new PickaxeItem(ModToolTiers.COPPER, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.COPPER,1,-2.8f))));
    public static final RegistryObject<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel",
            () -> new ShovelItem(ModToolTiers.COPPER, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.COPPER,1.5f,-3.0f))));
    public static final RegistryObject<Item> COPPER_AXE = ITEMS.register("copper_axe",
            () -> new AxeItem(ModToolTiers.COPPER, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.COPPER,6,-3.2f))));
    public static final RegistryObject<Item> COPPER_HOE = ITEMS.register("copper_hoe",
            () -> new HoeItem(ModToolTiers.COPPER, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.COPPER,0,-3.0f))));
    public static final RegistryObject<Item> COPPER_HELMET = ITEMS.register("copper_helmet",
            () -> new ArmorItem(ModArmorMaterials.COPPER_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final RegistryObject<Item> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate",
            () -> new ArmorItem(ModArmorMaterials.COPPER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final RegistryObject<Item> COPPER_LEGGINGS = ITEMS.register("copper_leggings",
            () -> new ArmorItem(ModArmorMaterials.COPPER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final RegistryObject<Item> COPPER_BOOTS = ITEMS.register("copper_boots",
            () -> new ArmorItem(ModArmorMaterials.COPPER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

    // Iron Items
    public static final RegistryObject<Item> RAW_IRON_DUST = ITEMS.register("raw_iron_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_IRON_NUGGET = ITEMS.register("raw_iron_nugget",
            () -> new Item(new Item.Properties()));

    // Gold Items
    public static final RegistryObject<Item> RAW_GOLD_DUST = ITEMS.register("raw_gold_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_DUST = ITEMS.register("gold_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_GOLD_NUGGET = ITEMS.register("raw_gold_nugget",
            () -> new Item(new Item.Properties()));

    // Diamond Items
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

    // Emerald Items
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
    public static final RegistryObject<Item> EMERALD_SWORD = ITEMS.register("emerald_sword",
            () -> new SwordItem(ModToolTiers.EMERALD, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.EMERALD,3,-2.4f))));
    public static final RegistryObject<Item> EMERALD_PICKAXE = ITEMS.register("emerald_pickaxe",
            () -> new PickaxeItem(ModToolTiers.EMERALD, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.EMERALD,1,-2.8f))));
    public static final RegistryObject<Item> EMERALD_SHOVEL = ITEMS.register("emerald_shovel",
            () -> new ShovelItem(ModToolTiers.EMERALD, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.EMERALD,1.5f,-3.0f))));
    public static final RegistryObject<Item> EMERALD_AXE = ITEMS.register("emerald_axe",
            () -> new AxeItem(ModToolTiers.EMERALD, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.EMERALD,6,-3.2f))));
    public static final RegistryObject<Item> EMERALD_HOE = ITEMS.register("emerald_hoe",
            () -> new HoeItem(ModToolTiers.EMERALD, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.EMERALD,0,-3.0f))));
    public static final RegistryObject<Item> EMERALD_HELMET = ITEMS.register("emerald_helmet",
            () -> new ArmorItem(ModArmorMaterials.EMERALD_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(35))));
    public static final RegistryObject<Item> EMERALD_CHESTPLATE = ITEMS.register("emerald_chestplate",
            () -> new ArmorItem(ModArmorMaterials.EMERALD_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(35))));
    public static final RegistryObject<Item> EMERALD_LEGGINGS = ITEMS.register("emerald_leggings",
            () -> new ArmorItem(ModArmorMaterials.EMERALD_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(35))));
    public static final RegistryObject<Item> EMERALD_BOOTS = ITEMS.register("emerald_boots",
            () -> new ArmorItem(ModArmorMaterials.EMERALD_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(35))));

    // Netherite Items
    public static final RegistryObject<Item> NETHERITE_DUST = ITEMS.register("netherite_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_NUGGET = ITEMS.register("netherite_nugget",
            () -> new Item(new Item.Properties()));

    // ALL ITEMS ABOVE THIS LINE
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
