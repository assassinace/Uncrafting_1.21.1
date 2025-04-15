package net.assassinace.uncrafting.screen.custom.slot;

import net.assassinace.uncrafting.block.entity.custom.UncraftingTableBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class OutputSlot extends SlotItemHandler {
    private final UncraftingTableBlockEntity blockEntity;

    public OutputSlot(UncraftingTableBlockEntity blockEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(@NotNull Player pPlayer, @NotNull ItemStack pStack) {
        super.onTake(pPlayer, pStack);

        ItemStack input = blockEntity.getItemHandler().getStackInSlot(0);
        if (input.isEmpty()) return; // Prevent crash if slot is empty or null

        int totalOutputItems = 0;
        for (int i = 1; i <= 9; i++) {
            ItemStack output = blockEntity.getItemHandler().getStackInSlot(i);
            if (!output.isEmpty()) {
                totalOutputItems += output.getCount();
            }
        }

        final int recipeOutputCount = 4; // or calculate dynamically
        int remainingInputs = totalOutputItems / recipeOutputCount;

        int currentInputs = input.getCount();
        int inputsToRemove = currentInputs - remainingInputs;

        if (inputsToRemove > 0) {
            blockEntity.getItemHandler().extractItem(0, inputsToRemove, false);
        }

        blockEntity.updateUncraftingOutputs();
    }


}
