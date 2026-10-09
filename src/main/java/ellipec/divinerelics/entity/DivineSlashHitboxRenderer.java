package ellipec.divinerelics.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class DivineSlashHitboxRenderer
        extends EntityRenderer<DivineSlashHitboxEntity, EntityRenderState> {

    public DivineSlashHitboxRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(
            EntityRenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState cameraRenderState)
    {

    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}