package net.assassinace.uncrafting.block.entity;

import net.assassinace.uncrafting.Uncrafting;
import net.assassinace.uncrafting.block.ModBlocks;
import net.assassinace.uncrafting.block.entity.custom.UncraftingTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,Uncrafting.MOD_ID);

    public static final RegistryObject<BlockEntityType<UncraftingTableBlockEntity>> UNCRAFTING_TABLE_BE =
            BLOCK_ENTITIES.register("uncrafting_table_be", () -> BlockEntityType.Builder.of(
                    UncraftingTableBlockEntity::new, ModBlocks.UNCRAFTING_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}