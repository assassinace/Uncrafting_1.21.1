package net.assassinace.uncrafting.item.custom;

import com.google.common.collect.ImmutableMap;
import net.assassinace.uncrafting.item.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class ModArmorItem extends ArmorItem {
    private static final Map<Holder<ArmorMaterial>, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<Holder<ArmorMaterial>, List<MobEffectInstance>>())
                    .put(ArmorMaterials.LEATHER,
                            List.of(new MobEffectInstance(MobEffects.JUMP, 220, 1, false, false),
                                    new MobEffectInstance(MobEffects.SLOW_FALLING, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 220, 0, false, false)))
                    .put(ArmorMaterials.IRON,
                            List.of(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.ABSORPTION, 220, 0, false, false)))
                    .put(ArmorMaterials.CHAIN,
                            List.of(new MobEffectInstance(MobEffects.INVISIBILITY, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 220, 0, false, false)))
                    .put(ModArmorMaterials.COPPER_ARMOR_MATERIAL,
                            List.of(new MobEffectInstance(MobEffects.WATER_BREATHING, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 220, 0, false, false)))
                    .put(ArmorMaterials.GOLD,
                            List.of(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.LUCK, 220, 0, false, false)))
                    .put(ArmorMaterials.DIAMOND,
                            List.of(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.DIG_SPEED, 220, 0, false, false)))
                    .put(ModArmorMaterials.EMERALD_ARMOR_MATERIAL,
                            List.of(new MobEffectInstance(MobEffects.DIG_SPEED, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.SATURATION, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.LUCK, 220, 0, false, false)))
                    .put(ArmorMaterials.NETHERITE,
                            List.of(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.DIG_SPEED, 220, 0, false, false),
                                    new MobEffectInstance(MobEffects.HEALTH_BOOST, 220, 0, false, false)))
                    .build();

    public ModArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        if(!level.isClientSide() && hasFullSuitOfArmorOn(player)) {
            evaluateArmorEffects(player);
        }
    }

    private boolean isPlayerInVillage(Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getPoiManager().find(
                    poiType -> true, // Match any POI type
                    pos -> true,     // Match any position
                    player.blockPosition(),
                    32,
                    PoiManager.Occupancy.ANY
            ).isPresent();
        }
        return false;
    }




    private void evaluateArmorEffects(Player player) {
        for(Map.Entry<Holder<ArmorMaterial>, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            Holder<ArmorMaterial> armorMaterial = entry.getKey();
            List<MobEffectInstance> baseEffects = entry.getValue();

            if (hasPlayerCorrectArmorOn(armorMaterial, player)) {
                // Handle EMERALD armor conditionally
                if (armorMaterial == ModArmorMaterials.EMERALD_ARMOR_MATERIAL) {
                    if (isPlayerInVillage(player)) {
                        player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 220, 0, false, false));
                    }
                    baseEffects.stream()
                            .filter(effect -> effect.getEffect() != MobEffects.HERO_OF_THE_VILLAGE)
                            .forEach(effect -> player.addEffect(new MobEffectInstance(effect.getEffect(),
                                    effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible())));
                    continue;
                }


                // Handle COPPER armor conditionally
                if (armorMaterial == ModArmorMaterials.COPPER_ARMOR_MATERIAL) {
                    if (player.isInWater()) {
                        baseEffects.forEach(effect -> player.addEffect(new MobEffectInstance(effect.getEffect(),
                                effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible())));
                    } else {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 220, 0, false, false));
                    }
                    continue;
                }

                // Apply all other armor effects normally
                addEffectToPlayer(player, baseEffects);
            }
        }
    }


    private void addEffectToPlayer(Player player, List<MobEffectInstance> mapEffect) {
        boolean hasPlayerEffect = mapEffect.stream().allMatch(effect -> player.hasEffect(effect.getEffect()));

        if(!hasPlayerEffect) {
            for (MobEffectInstance effect : mapEffect) {
                player.addEffect(new MobEffectInstance(effect.getEffect(),
                        effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
            }
        }
    }

    private boolean hasPlayerCorrectArmorOn(Holder<ArmorMaterial> mapArmorMaterial, Player player) {
        for(ItemStack armorStack : player.getArmorSlots()) {
            if(!(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }

        ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem());
        ArmorItem leggings = ((ArmorItem) player.getInventory().getArmor(1).getItem());
        ArmorItem chestplate = ((ArmorItem) player.getInventory().getArmor(2).getItem());
        ArmorItem helmet = ((ArmorItem) player.getInventory().getArmor(3).getItem());

        return boots.getMaterial() == mapArmorMaterial && leggings.getMaterial() == mapArmorMaterial
                && chestplate.getMaterial() == mapArmorMaterial && helmet.getMaterial() == mapArmorMaterial;
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);

        return !boots.isEmpty() && !leggings.isEmpty() && !chestplate.isEmpty() && !helmet.isEmpty();
    }
}