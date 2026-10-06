package ellipec.divinerelics.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class DragonScaleRenderer
        extends EntityRenderer<DragonScaleEntity, DragonScaleRenderer.DragonScaleRenderState> {

    private final ItemModelResolver itemModelResolver;

    public DragonScaleRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public DragonScaleRenderState createRenderState() {
        return new DragonScaleRenderState();
    }

    @Override
    public void extractRenderState(
            DragonScaleEntity entity,
            DragonScaleRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);

        // Use the remembered direction, even after the projectile stops.
        state.movement = entity.getRenderDirection();

        // GROUND keeps the scale at the correct item size.
        this.itemModelResolver.updateForNonLiving(
                state.item,
                entity.getItem(),
                ItemDisplayContext.GROUND,
                entity
        );
    }

    @Override
    public void submit(
            DragonScaleRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera
    ) {
        poseStack.pushPose();

        Vec3 direction = state.movement;

        if (direction.lengthSqr() > 0.0001) {

            direction = direction.normalize();

            float yaw = (float) Math.atan2(
                    direction.z,
                    direction.x
            );

            float pitch = (float) Math.asin(
                    -direction.y
            );

            poseStack.mulPose(
                    new Quaternionf()
                            .rotateY(-yaw)
                            .rotateZ(pitch)
            );
        }

        state.item.submit(
                poseStack,
                submitNodeCollector,
                15728880,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }

    public static class DragonScaleRenderState
            extends ThrownItemRenderState {

        public Vec3 movement = Vec3.ZERO;
    }
}