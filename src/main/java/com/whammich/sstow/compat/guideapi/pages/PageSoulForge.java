package com.whammich.sstow.compat.guideapi.pages;

import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.base.PageBase;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import com.whammich.sstow.utils.SFRecipeHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class PageSoulForge extends PageBase {

    public ItemStack input;
    public ItemStack output;
    public ItemStack secondary;

    public PageSoulForge(ItemStack input) {
        this.input = input;
        this.output = SFRecipeHandler.smelting().getSmeltingResult(input);
        this.secondary = SFRecipeHandler.smelting().getSecondaryResult(input);
    }

    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("sstow:textures/gui/recipe_elements.png"));
        guiBase.drawTexturedModalRect(guiLeft + 30, guiTop + 60, 0, 58, 110, 52);
        guiBase.drawCenteredString(fontRenderer, StatCollector.translateToLocal("guide.forge.smelting"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);

        int inputX = 37 + guiLeft + guiBase.xSize / 7;
        int inputY = 40 + guiTop + guiBase.ySize / 5;

        // Draw the input item
        GuiHelper.drawItemStack(this.input, inputX, inputY);

        // If there is no output, display a fire block
        if(this.output == null)
            this.output = new ItemStack(Blocks.fire);

        int outputX = 93 + guiLeft + guiBase.xSize / 7;
        int outputY = 26 + guiTop + guiBase.xSize / 5;

        // Draw the output item
        GuiHelper.drawItemStack(this.output, outputX, outputY);

        // Draw the output secondary if it isn't null
        if (secondary != null)
            GuiHelper.drawItemStack(this.secondary, outputX, outputY + 28);

        // Rendering the tooltips down here so they don't cause ugly render issues with ItemStacks

        // Draw input's tooltip
        if(GuiHelper.isMouseBetween(mouseX, mouseY, inputX, inputY, 15, 15))
            guiBase.renderToolTip(this.input, mouseX, mouseY);
        // Draw output's tooltip
        if(GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY, 15, 15))
            guiBase.renderToolTip(this.output, outputX, outputY);
        // Draw secondaries tooltip if it isn't null
        if (secondary != null)
            if (GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY + 20, 15, 15))
                guiBase.renderToolTip(this.secondary, outputX, outputY);
    }
}
