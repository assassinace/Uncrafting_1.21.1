package net.assassinace.uncrafting.menu;

import com.mojang.datafixers.util.Pair;
import net.assassinace.uncrafting.screen.ModMenuTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ExtendedInventoryMenu extends RecipeBookMenu<ExtendedInventoryMenu.ExtendedContainer, Recipe<ExtendedInventoryMenu.ExtendedContainer>> {

    public static class ExtendedContainer extends TransientCraftingContainer implements RecipeInput {
        public ExtendedContainer(AbstractContainerMenu menu, int width, int height) {
            super(menu, width, height);
        }

        @Override
        public int size() {
            return this.getContainerSize();
        }

        @Override
        public boolean isEmpty() {
            for (int i = 0; i < this.getContainerSize(); ++i) {
                if (!this.getItem(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
            InventoryMenu.EMPTY_ARMOR_SLOT_HELMET,
            InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
            InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS
    };

    private final ExtendedContainer craftSlots = new ExtendedContainer(this, 2, 2);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ItemStackHandler customSlots = new ItemStackHandler(2); // Elytra, Backpack
    private final Player player;

    public ExtendedInventoryMenu(int containerId, Inventory playerInventory) {
        super(ModMenuTypes.EXTENDED_INVENTORY_MENU.get(), containerId);
        this.player = playerInventory.player;

        this.addSlot(new ResultSlot(player, craftSlots, resultSlots, 0, 154, 28));

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new Slot(craftSlots, j + i * 2, 116 + j * 18, 18 + i * 18));
            }
        }

        for (int i = 0; i < 4; i++) {
            EquipmentSlot slotType = EquipmentSlot.values()[5 - i];
            final int index = 39 - i;
            final int texIndex = i;
            this.addSlot(new Slot(playerInventory, index, 8, 8 + i * 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.canEquip(slotType, player);
                }

                @Override
                public boolean mayPickup(@NotNull Player player) {
                    ItemStack itemstack = this.getItem();
                    return itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[texIndex]);
                }
            });
        }

        this.addSlot(new SlotItemHandler(customSlots, 0, 96, 8) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == Items.ELYTRA;
            }
        });

        this.addSlot(new SlotItemHandler(customSlots, 1, 116, 8) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem().getDescriptionId().contains("shulker_box");
            }
        });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addSlot(new Slot(playerInventory, 40, 96, 62) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

    @Override
    public void fillCraftSlotsStackedContents(@NotNull StackedContents helper) {
        this.craftSlots.fillStackedContents(helper);
    }

    @Override
    public void clearCraftingContent() {
        this.resultSlots.clearContent();
        this.craftSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(RecipeHolder<Recipe<ExtendedContainer>> recipe) {
        return recipe.value().matches(this.craftSlots, this.player.level());
    }

    @Override public int getResultSlotIndex() { return 0; }
    @Override public int getGridWidth() { return 2; }
    @Override public int getGridHeight() { return 2; }
    @Override public int getSize() { return 5; }
    @Override public @NotNull RecipeBookType getRecipeBookType() { return RecipeBookType.CRAFTING; }
    @Override public boolean stillValid(@NotNull Player player) { return true; }
    @Override public boolean shouldMoveToInventory(int index) { return index != 0; }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    public ItemStackHandler getCustomSlots() {
        return customSlots;
    }

    public ExtendedContainer getCraftingGrid() {
        return craftSlots;
    }

    public ResultContainer getResultContainer() {
        return resultSlots;
    }
}
