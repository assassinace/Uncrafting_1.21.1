package net.assassinace.uncrafting.screen.custom;

import net.assassinace.uncrafting.block.ModBlocks;
import net.assassinace.uncrafting.block.entity.custom.UncraftingTableBlockEntity;
import net.assassinace.uncrafting.screen.ModMenuTypes;
import net.assassinace.uncrafting.screen.custom.slot.InputSlot;
import net.assassinace.uncrafting.screen.custom.slot.OutputSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class UncraftingTableMenu extends AbstractContainerMenu {
    public boolean shouldReturnOutputs = false;
    private final Player linkedPlayer;
    public final UncraftingTableBlockEntity blockEntity;
    private final Level level;

    public static final int OUTPUT_SLOT_START = 1;
    public static final int OUTPUT_SLOT_END = 10;

    public UncraftingTableMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public UncraftingTableMenu(int pContainerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.UNCRAFTING_TABLE_MENU.get(), pContainerId);
        this.blockEntity = ((UncraftingTableBlockEntity) blockEntity);
        this.level = inv.player.level();
        this.linkedPlayer = inv.player;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new InputSlot(this.blockEntity, this.blockEntity.getItemHandler(), 0, 35, 35));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = 1 + row * 3 + col;
                int x = 93 + col * 18;
                int y = 17 + row * 18;
                this.addSlot(new OutputSlot(this.blockEntity, this.blockEntity.getItemHandler(), index, x, y));
            }
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.UNCRAFTING_TABLE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 7 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 7 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < 36) {
            if (!moveItemStackTo(sourceStack, 36, 46, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(sourceStack, 0, 36, false)) {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            if (!blockEntity.wasInputConsumed()) {
                // Return input
                ItemStack input = blockEntity.getItemHandler().getStackInSlot(0);
                if (!input.isEmpty()) {
                    boolean success = player.getInventory().add(input);
                    if (!success) {
                        player.drop(input, false);
                    }
                    blockEntity.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
                }
            } else {
                // Return remaining outputs
                for (int i = OUTPUT_SLOT_START; i < OUTPUT_SLOT_END; i++) {
                    ItemStack out = blockEntity.getItemHandler().getStackInSlot(i);
                    if (!out.isEmpty()) {
                        boolean success = player.getInventory().add(out);
                        if (!success) {
                            player.drop(out, false);
                        }
                        blockEntity.getItemHandler().setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }

            blockEntity.setChanged(); // Ensure changes are persisted
        }
    }




    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (shouldReturnOutputs && !linkedPlayer.level().isClientSide) {
            shouldReturnOutputs = false;

            for (int i = OUTPUT_SLOT_START; i < OUTPUT_SLOT_END; i++) {
                ItemStack stack = blockEntity.getItemHandler().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    net.minecraftforge.items.ItemHandlerHelper.giveItemToPlayer(linkedPlayer, stack.copy());
                    blockEntity.getItemHandler().setStackInSlot(i, ItemStack.EMPTY);
                }
            }

            blockEntity.setChanged();
            level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
        }
    }

    public void onOutputTaken(int slotIndex) {
        if (blockEntity.getExpectedOutput().isEmpty()) return;

        List<ItemStack> expected = blockEntity.getExpectedOutput();
        int setsTaken = Integer.MAX_VALUE; // we’ll find the minimum # of full recipe sets remaining

        for (int i = 0; i < 9; i++) {
            int outputSlotIndex = i + OUTPUT_SLOT_START;
            ItemStack expectedStack = expected.get(i);
            ItemStack actualStack = getSlot(outputSlotIndex).getItem();

            if (!expectedStack.isEmpty()) {
                int expectedPerSet = expectedStack.getCount();

                if (expectedPerSet == 0) continue; // no item to compare

                // How many *full* sets are left in this slot?
                int remainingSets = actualStack.getCount() / expectedPerSet;

                setsTaken = Math.min(setsTaken, remainingSets);
            }
        }

        // Number of sets consumed = how many we removed from the grid
        int currentInputCount = blockEntity.getItemHandler().getStackInSlot(0).getCount();
        int inputUsed = blockEntity.getLastRecipeOutputCount();

        int expectedMaxSets = currentInputCount / inputUsed;
        int setsRemoved = expectedMaxSets - setsTaken;

        if (setsRemoved > 0) {
            blockEntity.setSuppressOutputUpdate(true);
            blockEntity.getItemHandler().extractItem(0, setsRemoved, false);
            blockEntity.setSuppressOutputUpdate(false);
            blockEntity.updateUncraftingOutputs();
        }

        // Clean up if input can't support even 1 more recipe
        ItemStack input = blockEntity.getItemHandler().getStackInSlot(0);
        if (input.isEmpty() || !blockEntity.canStillUncraft(input)) {
            blockEntity.setSuppressOutputUpdate(true);
            blockEntity.clearOutputs();
            blockEntity.setSuppressOutputUpdate(false);
        }
    }
}
