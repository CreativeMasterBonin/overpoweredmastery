package net.rk.overpoweredmastery.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.menu.MultiAssemblerMenu;

public class MultiAssemblerScreen extends AbstractContainerScreen<MultiAssemblerMenu> {
    // image size is 176 x - 166 y - NO ADDED WHITE SPACE

    public static final ResourceLocation BACKGROUND_LOCATION =
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/gui/multi_assembler.png");

    public MultiAssemblerScreen(MultiAssemblerMenu menu, Inventory playerInventory, Component component) {
        super(menu, playerInventory, Component.translatable("screen.overpoweredmastery.multi_assembler").withStyle(ChatFormatting.WHITE));
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = this.leftPos + 8;
        this.titleLabelY = this.topPos + 8;
        this.inventoryLabelX = this.leftPos + 8;
        this.inventoryLabelY = this.topPos + 68;
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_LOCATION,
                i, j, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 176, 166);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderContents(guiGraphics, mouseX, mouseY, partialTick);
        this.renderCarriedItem(guiGraphics, mouseX, mouseY);
        this.renderSnapbackItem(guiGraphics);

        try{
            if(this.menu.isAssembling()){
                int movementY = 12 + (int)(Mth.sin((int)(partialTick * 0.95f)) * 2);
                guiGraphics.renderFakeItem(new ItemStack(Items.LIME_STAINED_GLASS_PANE),
                        this.leftPos + 134,this.topPos + movementY,9991);
            }
            else{
                guiGraphics.renderFakeItem(new ItemStack(Items.RED_STAINED_GLASS_PANE),
                        this.leftPos + 134,this.topPos + 12,9991);
            }

            guiGraphics.drawString(this.font,
                    Component.literal("Assembly Step: " + String.valueOf(this.menu.getAssemblyProgress())),
                    this.leftPos + 30,this.topPos + 56,
                    0xFFFFFFFF,false);

            guiGraphics.drawString(this.font,
                    Component.literal(" / " + String.valueOf(this.menu.getAssemblyTime())),
                    this.leftPos + 124,this.topPos + 56,
                    0xFFFFFFFF,false);
        }
        catch(Exception e){
            guiGraphics.drawString(this.font,Component.literal(e.getMessage()),2,4,0xFFFF0000,false);
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void containerTick() {

    }
}
