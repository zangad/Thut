package thut.tech.client;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import thut.api.maths.Vector3;
import thut.tech.client.render.RenderLift;
import thut.tech.client.render.ControllerRenderer;
import thut.tech.common.CommonProxy;
import thut.tech.common.TechCore;
import thut.tech.common.blocks.lift.ControllerTile;
import thut.tech.common.entity.EntityLift;

public class ClientProxy extends CommonProxy
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void RenderBounds(final DrawBlockHighlightEvent event)
    {
        if (!(event.getTarget() instanceof BlockRayTraceResult)) return;
        final BlockRayTraceResult target = (BlockRayTraceResult) event.getTarget();
        ItemStack held;
        final PlayerEntity player = Minecraft.getInstance().player;
        if (!(held = player.getHeldItemMainhand()).isEmpty() || !(held = player.getHeldItemOffhand()).isEmpty())
        {
            BlockPos pos = target.getPos();
            if (pos == null || held.getItem() != TechCore.LIFT) return;
            if (!player.world.getBlockState(pos).getMaterial().isSolid())
            {
                final Vec3d loc = player.getPositionVector().add(0, player.getEyeHeight(), 0).add(player.getLookVec()
                        .scale(2));
                pos = new BlockPos(loc);
            }

            if (held.getTag() != null && held.getTag().contains("min"))
            {
                BlockPos min = Vector3.readFromNBT(held.getTag().getCompound("min"), "").getPos();
                BlockPos max = pos;
                AxisAlignedBB box = new AxisAlignedBB(min, max);
                min = new BlockPos(box.minX, box.minY, box.minZ);
                max = new BlockPos(box.maxX, box.maxY, box.maxZ).add(1, 1, 1);
                box = new AxisAlignedBB(min, max);
                final float partialTicks = event.getPartialTicks();
                final double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
                final double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
                final double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
                box = box.offset(-d0, -d1 - player.getEyeHeight(), -d2);
                GlStateManager.enableBlend();
                GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO);
                GlStateManager.color4f(0.0F, 0.0F, 0.0F, 0.4F);
                GlStateManager.lineWidth(2.0F);
                GlStateManager.disableTexture();
                GlStateManager.depthMask(false);
                GlStateManager.color4f(1.0F, 0.0F, 0.0F, 1F);
                final Tessellator tessellator = Tessellator.getInstance();
                final BufferBuilder vertexbuffer = tessellator.getBuffer();
                vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
                vertexbuffer.pos(box.minX, box.minY, box.minZ).endVertex();
                vertexbuffer.pos(box.maxX, box.minY, box.minZ).endVertex();
                vertexbuffer.pos(box.maxX, box.minY, box.maxZ).endVertex();
                vertexbuffer.pos(box.minX, box.minY, box.maxZ).endVertex();
                vertexbuffer.pos(box.minX, box.minY, box.minZ).endVertex();
                tessellator.draw();
                vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
                vertexbuffer.pos(box.minX, box.maxY, box.minZ).endVertex();
                vertexbuffer.pos(box.maxX, box.maxY, box.minZ).endVertex();
                vertexbuffer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
                vertexbuffer.pos(box.minX, box.maxY, box.maxZ).endVertex();
                vertexbuffer.pos(box.minX, box.maxY, box.minZ).endVertex();
                tessellator.draw();
                vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
                vertexbuffer.pos(box.minX, box.minY, box.minZ).endVertex();
                vertexbuffer.pos(box.minX, box.maxY, box.minZ).endVertex();
                vertexbuffer.pos(box.maxX, box.minY, box.minZ).endVertex();
                vertexbuffer.pos(box.maxX, box.maxY, box.minZ).endVertex();
                vertexbuffer.pos(box.maxX, box.minY, box.maxZ).endVertex();
                vertexbuffer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
                vertexbuffer.pos(box.minX, box.minY, box.maxZ).endVertex();
                vertexbuffer.pos(box.minX, box.maxY, box.maxZ).endVertex();
                tessellator.draw();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture();
                GlStateManager.disableBlend();
            }
        }
    }

    @Override
    public void setupClient(final FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        RenderingRegistry.registerEntityRenderingHandler(EntityLift.class, (manager) -> new RenderLift(manager));

        ClientRegistry.bindTileEntitySpecialRenderer(ControllerTile.class, new ControllerRenderer<>());
    }
}
