package uwu.lopyluna.obelisk.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class DnDesireCosmeticLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public String[] uuidList;

    static final Minecraft mc = Minecraft.getInstance();

    public DnDesireCosmeticLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer, String[] uuidList) {
        super(pRenderer);
        this.uuidList = uuidList;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull AbstractClientPlayer pLivingEntity,
                       float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (Arrays.stream(uuidList).toList().contains(pLivingEntity.getUUID().toString()) && !pLivingEntity.isInvisible()) {
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entitySolid(pLivingEntity.getSkin().texture()));
            int i = LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F);
            pPoseStack.pushPose();



            this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, i);
            pPoseStack.popPose();
        }
    }
}