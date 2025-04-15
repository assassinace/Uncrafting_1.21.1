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

public class UncraftingTableMenu extends AbstractContainerMenu {
    public boolean shouldReturnOutputs = false;
    private Player linkedPlayer; // to store the player using the menu
    public final UncraftingTableBlockEntity blockEntity;
    private final Level level;

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

        // Output slots (3x3 grid)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = 1 + row * 3 + col;
                int x = 93 + col * 18;
                int y = 17 + row * 18;
                this.addSlot(new OutputSlot((UncraftingTableBlockEntity) blockEntity, ((UncraftingTableBlockEntity) blockEntity).getItemHandler(), index, x, y));
            }
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 10;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.UNCRAFTING_TABLE.get());
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
    public void removed(Player pPlayer) {
        super.removed(pPlayer);

        if (!pPlayer.level().isClientSide) {
            // Return input item
            ItemStack input = blockEntity.getItemHandler().getStackInSlot(0);
            if (!input.isEmpty()) {
                boolean success = pPlayer.getInventory().add(input);
                if (!success) {
                    pPlayer.drop(input, false);
                }
                blockEntity.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
            }

            // Return remaining output items
            for (int i = 1; i <= 9; i++) {
                ItemStack stack = blockEntity.getItemHandler().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    boolean success = pPlayer.getInventory().add(stack);
                    if (!success) {
                        pPlayer.drop(stack, false);
                    }
                    blockEntity.getItemHandler().setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }
    }
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (shouldReturnOutputs && !linkedPlayer.level().isClientSide) {
            shouldReturnOutputs = false;

            for (int i = 1; i <= 9; i++) {
                ItemStack stack = blockEntity.getItemHandler().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    net.minecraftforge.items.ItemHandlerHelper.giveItemToPlayer(linkedPlayer, stack.copy());
                    blockEntity.getItemHandler().setStackInSlot(i, ItemStack.EMPTY);
                }
            }

            blockEntity.setChanged();
            level.sendBlockUpdated(
                    blockEntity.getBlockPos(),
                    blockEntity.getBlockState(),
                    blockEntity.getBlockState(),
                    3
            );
        }
    }
}