
package ellipec.divinerelics.item;

import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import ellipec.divinerelics.client.JarngreiprClientAnimation;
import net.minecraft.world.item.ItemDisplayContext;

public class JarngreiprRenderer extends GeoItemRenderer<JarngreiprItem> {

    public JarngreiprRenderer() {
        super(new JarngreiprModel());
    }

    @Override
    public void adjustRenderPose(RenderPassInfo renderPassInfo) {
        super.adjustRenderPose(renderPassInfo);

        PoseStack poseStack = renderPassInfo.poseStack();

        ItemDisplayContext perspective =
                (ItemDisplayContext) renderPassInfo.getOrDefaultGeckolibData(
                        DataTickets.ITEM_RENDER_PERSPECTIVE,
                        ItemDisplayContext.NONE
                );

        if (perspective == ItemDisplayContext.GUI) {
            poseStack.translate(-0.5f, -0.5f, 0.0f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0f));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            return;
        }

        if (perspective == ItemDisplayContext.GROUND) {
            poseStack.translate(0.0f, 1.0f, 0.05f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
            poseStack.scale(2.0f, 2.0f, 2.0f);
            return;
        }

        poseStack.translate(-0.12f, -1.35f, 0.05f);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));

        boolean thirdPerson =
                perspective == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                        || perspective == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;

        boolean firstPerson =
                perspective == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                        || perspective == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;

        if (thirdPerson) {
            poseStack.scale(2.0f, 2.0f, 2.0f);
        }

        if (firstPerson) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        }

        if (!firstPerson && !thirdPerson) {
            return;
        }

        JarngreiprClientAnimation.State state =
                JarngreiprClientAnimation.getState();

        if (state == JarngreiprClientAnimation.State.CHARGING) {

            // Keep the weapon in its normal hand position while charging.

        } else if (state == JarngreiprClientAnimation.State.SWINGING) {

            // Rotate the axe 90 degrees around the X-axis.
            poseStack.mulPose(
                    Axis.YP.rotationDegrees(-90.0f)
            );

        } else if (state == JarngreiprClientAnimation.State.RECOVERY) {

            // No extra weapon rotation during recovery.
        }
    }
}
