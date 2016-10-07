package thut.wearables.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import thut.api.network.PacketHandler;
import thut.wearables.ThutWearables;
import thut.wearables.inventory.ContainerWearables;
import thut.wearables.network.PacketGui;

public class GuiWearables extends InventoryEffectRenderer
{
    public static final ResourceLocation background = new ResourceLocation(ThutWearables.MODID,
            "textures/gui/wearables.png");

    final GuiScreen                      baseGui;

    public GuiWearables(EntityPlayer player, GuiScreen base)
    {
        super(new ContainerWearables(player));
        this.baseGui = base;
        this.allowUserInput = true;
    }

    /** Called from the main game loop to update the screen. */
    @Override
    public void updateScreen()
    {
        this.updateActivePotionEffects();
    }

    /** Adds the buttons (and other controls) to the screen in question. */
    @Override
    public void initGui()
    {
        this.buttonList.clear();
        super.initGui();
    }

    public void initToOther(GuiScreen other)
    {
        this.mc = other.mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = other.width;
        this.height = other.height;
        initGui();
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of
     * the items) */
    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        // No foreground layer
    }

    /** Draws the screen and all the components in it. */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /** Draws either a gradient over the background screen (when it exists) or a
     * flat gradient over background.png */
    @Override
    public void drawDefaultBackground()
    {
        // No background either
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int k = this.guiLeft;
        int l = this.guiTop;
        this.drawTexturedModalRect(k - 32, l, 0, 0, 60, 100);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    /** Called when the mouse is clicked. Args : mouseX, mouseY,
     * clickedButton */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        Slot slot = getSlotAtPosition(mouseX, mouseY);
        if (slot != null)
        {
            PacketGui packet = new PacketGui();
            packet.data.setInteger("S", slot.getSlotIndex());
            PacketHandler.packetPipeline.sendToServer(packet);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
    }

    /** Returns whether the mouse is over the given slot. */
    private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY)
    {
        return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
    }

    /** Returns the slot at the given coordinates or null if there is none. */
    private Slot getSlotAtPosition(int x, int y)
    {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i)
        {
            Slot slot = this.inventorySlots.inventorySlots.get(i);
            if (this.isMouseOverSlot(slot, x, y)) { return slot; }
        }
        return null;
    }
}
