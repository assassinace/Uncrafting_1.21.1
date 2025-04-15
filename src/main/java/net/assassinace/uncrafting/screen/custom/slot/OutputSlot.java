package net.assassinace.uncrafting.screen.custom.slot;

import net.assassinace.uncrafting.block.entity.custom.UncraftingTableBlockEntity;
import net.assassinace.uncrafting.screen.custom.UncraftingTableMenu;
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
    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);

        if (player.level().isClientSide) return;

        blockEntity.setSuppressOutputUpdate(true);
        blockEntity.getItemHandler().extractItem(0, 1, false);
        blockEntity.setSuppressOutputUpdate(false);

        // Flag to return all outputs in the next tick from the menu
        if (player.containerMenu instanceof UncraftingTableMenu menu) {
            menu.shouldReturnOutputs = true;
        }
    }
}

