package net.assassinace.uncrafting.item;

import net.assassinace.uncrafting.Uncrafting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
public static final Holder<ArmorMaterial> EMERALD_ARMOR_MATERIAL = register("emerald", Util.make(new EnumMap<>(ArmorItem.Type.class),
        attribute -> {
            attribute.put(ArmorItem.Type.BOOTS,3);
            attribute.put(ArmorItem.Type.LEGGINGS,6);
            attribute.put(ArmorItem.Type.CHESTPLATE,8);
            attribute.put(ArmorItem.Type.HELMET,3);
            attribute.put(ArmorItem.Type.BODY,11);
        }), 14, 2.5f, 0.0f, () -> Items.EMERALD);

    public static final Holder<ArmorMaterial> COPPER_ARMOR_MATERIAL = register("copper", Util.make(new EnumMap<>(ArmorItem.Type.class),
            attribute -> {
                attribute.put(ArmorItem.Type.BOOTS,2);
                attribute.put(ArmorItem.Type.LEGGINGS,4);
                attribute.put(ArmorItem.Type.CHESTPLATE,5);
                attribute.put(ArmorItem.Type.HELMET,2);
                attribute.put(ArmorItem.Type.BODY,8);
            }), 11, 0.0f, 0.0f, () -> Items.COPPER_INGOT);


    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> typeProtection,
                                                  int enchantability, float toughness, float knockbackResistance,
                                                  Supplier<Item> ingredientItem) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Uncrafting.MOD_ID, name);
        Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_DIAMOND;
        Supplier<Ingredient> ingredient = () -> Ingredient.of(ingredientItem.get());
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(location));

        EnumMap<ArmorItem.Type, Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            typeMap.put(type, typeProtection.get(type));
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, location,
                new ArmorMaterial(typeProtection, enchantability, equipSound, ingredient, layers, toughness, knockbackResistance));
    }
}
