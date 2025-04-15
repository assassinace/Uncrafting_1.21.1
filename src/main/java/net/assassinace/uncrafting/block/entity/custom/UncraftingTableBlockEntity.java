package net.assassinace.uncrafting.block.entity.custom;

import net.assassinace.uncrafting.block.entity.ModBlockEntities;
import net.assassinace.uncrafting.screen.custom.UncraftingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class UncraftingTableBlockEntity extends BlockEntity implements MenuProvider {
    private boolean suppressOutputUpdate = false;

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 64;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide() && !suppressOutputUpdate) {
                updateUncraftingOutputs();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_START = 1;
    private static final int OUTPUT_END = 9;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public UncraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.UNCRAFTING_TABLE_BE.get(), pPos, pBlockState);
             }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
                super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.uncrafting.uncrafting_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new UncraftingTableMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void clearOutputs() {
        suppressOutputUpdate = true;
        try {
            for (int i = OUTPUT_START; i <= OUTPUT_END; i++) {
                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            }
        } finally {
            suppressOutputUpdate = false;
        }
    }


    public void updateUncraftingOutputs() {
        if (level == null || level.isClientSide) return;

        suppressOutputUpdate = true;
        try {
            ItemStack input = itemHandler.getStackInSlot(INPUT_SLOT);

            if (input.isEmpty() || input.isEnchanted() || (input.isDamageableItem() && input.isDamaged())) {
                clearOutputs();
                return;
            }

            List<RecipeHolder<CraftingRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
            Optional<RecipeHolder<CraftingRecipe>> match = recipes.stream()
                    .filter(r -> {
                        ItemStack result = r.value().getResultItem(level.registryAccess());
                        return ItemStack.isSameItemSameComponents(result, input)
                                && input.getCount() >= result.getCount(); // ← new check
                    })
                    .findFirst();


            if (match.isEmpty()) {
                clearOutputs();
                return;
            }

            CraftingRecipe recipe = match.get().value();
            List<Ingredient> ingredients = recipe.getIngredients();
            int recipeOutputCount = recipe.getResultItem(level.registryAccess()).getCount();
            if (input.getCount() < recipeOutputCount) {
                clearOutputs();
                return;
            }
            int multiplier = input.getCount() / recipeOutputCount;


            for (int i = 0; i < 9; i++) {
                if (i < ingredients.size()) {
                    ItemStack[] matches = ingredients.get(i).getItems();
                    if (matches.length > 0) {
                        ItemStack copy = matches[0].copy();
                        copy.setCount(multiplier);
                        itemHandler.setStackInSlot(i + 1, copy);
                    } else {
                        itemHandler.setStackInSlot(i + 1, ItemStack.EMPTY);
                    }
                } else {
                    itemHandler.setStackInSlot(i + 1, ItemStack.EMPTY);
                }
            }
        } finally {
            suppressOutputUpdate = false;
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public LazyOptional<IItemHandler> getLazyItemHandler() {
        return lazyItemHandler;
    }
    public boolean hasOutputItems() {
        for (int i = OUTPUT_START; i <= OUTPUT_END; i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    public void setSuppressOutputUpdate(boolean value) {
        this.suppressOutputUpdate = value;
    }
}