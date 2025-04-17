package net.assassinace.uncrafting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.assassinace.uncrafting.Uncrafting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class ExtendedInventoryScreen extends InventoryScreen {
    private static final ResourceLocation EXTENDED_INVENTORY =
            ResourceLocation.fromNamespaceAndPath(Uncrafting.MOD_ID, "textures/gui/player_inventory/extended_inventory.png");

    public ExtendedInventoryScreen(Player pPlayer) {
        super(pPlayer);
    }

    @Override
    protected void init() {

        // Positioning + recipe book layout logic
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        // This ensures the recipe book is aligned correctly
        this.leftPos = this.getRecipeBookComponent().updateScreenPosition(this.width, this.imageWidth);
        super.init();
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EXTENDED_INVENTORY);

        // Draw the background texture
        guiGraphics.blit(EXTENDED_INVENTORY, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            RenderSystem.enableDepthTest(); // ← CRITICAL to allow proper 3D rendering
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    guiGraphics,
                    this.leftPos + 51,        // X position
                    this.topPos + 75,         // Y position
                    30,                       // scale
                    this.leftPos + 51 - mouseX,
                    this.topPos + 75 - 50 - mouseY,
                    180.0F,
                    180.0F,
                    0.0F,
                    player
            );
        }
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("container.inventory");
    }
}