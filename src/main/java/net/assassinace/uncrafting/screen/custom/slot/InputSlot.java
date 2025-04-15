package net.assassinace.uncrafting.screen.custom.slot;

import net.assassinace.uncrafting.block.entity.custom.UncraftingTableBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class InputSlot extends SlotItemHandler {
    private final UncraftingTableBlockEntity blockEntity;

    public InputSlot(UncraftingTableBlockEntity blockEntity, IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return !blockEntity.hasOutputItems(); // Only allow placement if outputs are empty
    }
}
