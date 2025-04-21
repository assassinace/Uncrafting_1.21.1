package net.assassinace.uncrafting.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.assassinace.uncrafting.Uncrafting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.assassinace.uncrafting.menu.ExtendedInventoryMenu;

import javax.annotation.Nullable;

public class ExtendedInventoryScreen extends EffectRenderingInventoryScreen<ExtendedInventoryMenu> implements RecipeUpdateListener {

    private static final ResourceLocation EXTENDED_INVENTORY =
            ResourceLocation.fromNamespaceAndPath(Uncrafting.MOD_ID, "textures/gui/player_inventory/extended_inventory.png");
    private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();

    private float xMouse, yMouse;
    private boolean widthTooNarrow, buttonClicked;

    public ExtendedInventoryScreen(ExtendedInventoryMenu inv, Inventory playerInv, Component name) {
            super(inv, playerInv, name);
            this.imageWidth = 256;
            this.imageHeight = 256;
            this.titleLabelX = 115;
            this.titleLabelY = 8;
        }


    @Override
    public void containerTick() {
        this.recipeBookComponent.tick();
    }

    @Override
    protected void init() {
        this.widthTooNarrow = this.width < 379;

        // Correct GUI positioning
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        // Initialize recipe book
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);

        // Only adjust leftPos if book is visible
        if (this.recipeBookComponent.isVisible() && !this.widthTooNarrow) {
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        }

        super.init();

        // Add the toggle button
        this.addRenderableWidget(new ImageButton(this.leftPos + 124, this.topPos + 62, 20, 18,
                RecipeBookComponent.RECIPE_BUTTON_SPRITES, btn -> {
            this.recipeBookComponent.toggleVisibility();
            if (this.recipeBookComponent.isVisible() && !this.widthTooNarrow) {
                this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            } else {
                this.leftPos = (this.width - this.imageWidth) / 2;
            }
            btn.setPosition(this.leftPos + 124, this.topPos + 62);
            this.buttonClicked = true;
        }));

        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            this.recipeBookComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        } else {
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            this.recipeBookComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            this.recipeBookComponent.renderGhostRecipe(pGuiGraphics, this.leftPos, this.topPos, false, pPartialTick);
        }

        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        this.recipeBookComponent.renderTooltip(pGuiGraphics, this.leftPos, this.topPos, pMouseX, pMouseY);
        this.xMouse = (float)pMouseX;
        this.yMouse = (float)pMouseY;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pGuiGraphics.blit(EXTENDED_INVENTORY, i, j, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        renderEntityInInventoryFollowsMouse(pGuiGraphics, i + 26, j + 8, i + 75, j + 78, 30, 0.0625F, this.xMouse, this.yMouse, this.minecraft.player);
    }

    public static void renderEntityInInventoryFollowsMouse(GuiGraphics pGuiGraphics, int pX1, int pY1, int pX2, int pY2, int pScale, float pYOffset, float pMouseX, float pMouseY, LivingEntity pEntity) {
        float f = (float)(pX1 + pX2) / 2.0F;
        float f1 = (float)(pY1 + pY2) / 2.0F;
        float f2 = (float)Math.atan((double)((f - pMouseX) / 40.0F));
        float f3 = (float)Math.atan((double)((f1 - pMouseY) / 40.0F));
        renderEntityInInventoryFollowsAngle(pGuiGraphics, pX1, pY1, pX2, pY2, pScale, pYOffset, f2, f3, pEntity);
    }

    public static void renderEntityInInventoryFollowsAngle(GuiGraphics pGuiGraphics, int x1, int y1, int x2, int y2, int scale, float yOffset, float angleX, float angleY, LivingEntity entity) {
        float cx = (float)(x1 + x2) / 2.0F;
        float cy = (float)(y1 + y2) / 2.0F;
        pGuiGraphics.enableScissor(x1, y1, x2, y2);

        Quaternionf rotationZ = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf rotationX = new Quaternionf().rotateX(angleY * 20.0F * (float)(Math.PI / 180.0));
        rotationZ.mul(rotationX);

        float originalYBodyRot = entity.yBodyRot;
        float originalYRot = entity.getYRot();
        float originalXRot = entity.getXRot();
        float originalYHeadRotO = entity.yHeadRotO;
        float originalYHeadRot = entity.yHeadRot;

        entity.yBodyRot = 180.0F + angleX * 20.0F;
        entity.setYRot(180.0F + angleX * 40.0F);
        entity.setXRot(-angleY * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();

        Vector3f translate = new Vector3f(0.0F, entity.getBbHeight() / 2.0F + yOffset, 0.0F);
        renderEntityInInventory(pGuiGraphics, cx, cy, scale, translate, rotationZ, rotationX, entity);

        entity.yBodyRot = originalYBodyRot;
        entity.setYRot(originalYRot);
        entity.setXRot(originalXRot);
        entity.yHeadRotO = originalYHeadRotO;
        entity.yHeadRot = originalYHeadRot;

        pGuiGraphics.disableScissor();
    }

    public static void renderEntityInInventory(GuiGraphics pGuiGraphics, float x, float y, int scale, Vector3f translate, Quaternionf pose, @Nullable Quaternionf cameraOrientation, LivingEntity entity) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(x, y, 50.0);
        pGuiGraphics.pose().mulPose(new Matrix4f().scaling((float)scale, (float)scale, (float)(-scale)));
        pGuiGraphics.pose().translate(translate.x, translate.y, translate.z);
        pGuiGraphics.pose().mulPose(pose);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (cameraOrientation != null) {
            cameraOrientation.conjugate();
            dispatcher.overrideCameraOrientation(cameraOrientation);
        }
        dispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, pGuiGraphics.pose(), pGuiGraphics.bufferSource(), 15728880));
        pGuiGraphics.flush();
        dispatcher.setRenderShadow(true);
        pGuiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() ? false : super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.mouseReleased(mouseX, mouseY, button);
        }
    }

   // @Override
    //protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int button) {
   //     boolean flag = mouseX < guiLeft || mouseY < guiTop || mouseX >= guiLeft + this.imageWidth || mouseY >= guiTop + this.imageHeight;
   //     return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, button) && flag;
  //  }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}