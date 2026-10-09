package ellipec.divinerelics.entity;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class DivineSlashRenderer extends GeoEntityRenderer<DivineSlashEntity, EntityRenderState> {

    public DivineSlashRenderer(EntityRendererProvider.Context context) {
        super(context, new DivineSlashModel());
    }

    @Override
    public boolean shouldRender(
            DivineSlashEntity entity,
            Frustum frustum,
            double camX,
            double camY,
            double camZ
    ) {
        return true;
    }

    @Override
    public void adjustRenderPose(RenderPassInfo<EntityRenderState> renderPassInfo) {
        super.adjustRenderPose(renderPassInfo);

        PoseStack poseStack = renderPassInfo.poseStack();

        float pitch = renderPassInfo.renderState()
                .getOrDefaultGeckolibData(
                        com.geckolib.constant.DataTickets.ENTITY_PITCH,
                        0.0f
                );

        // Make the model follow the Gust's vertical direction.
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

        // Rotate the crescent by -45 degrees around its Z axis.
        poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0f));
    }

    @Override
    public @Nullable RenderType getRenderType(
            EntityRenderState renderState,
            Identifier texture
    ) {
        return RenderTypes.eyes(texture);
    }
}