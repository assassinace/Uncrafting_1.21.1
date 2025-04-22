package net.assassinace.uncrafting.item;

import net.assassinace.uncrafting.util.ModTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {
    public static final Tier EMERALD = new ForgeTier(1800, 8.5f, 3.5f, 18,
            ModTags.Blocks.NEEDS_EMERALD_TOOL, () -> Ingredient.of(Items.EMERALD),
            ModTags.Blocks.INCORRECT_FOR_EMERALD_TOOL);

    public static final Tier COPPER = new ForgeTier(190, 5.0f, 1.5f, 12,
            ModTags.Blocks.NEEDS_COPPER_TOOL, () -> Ingredient.of(Items.COPPER_INGOT),
            ModTags.Blocks.INCORRECT_FOR_COPPER_TOOL);
}
