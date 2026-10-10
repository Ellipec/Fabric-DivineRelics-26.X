package ellipec.divinerelics.mixin;

import ellipec.divinerelics.client.JarngreiprClientAnimation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class JarngreiprPlayerModelMixin
        extends HumanoidModel<AvatarRenderState> {

    protected JarngreiprPlayerModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(
            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V",
            at = @At("TAIL")
    )
    private void jarngreiprAnimateArm(
            AvatarRenderState state,
            CallbackInfo ci
    ) {
        JarngreiprClientAnimation.State animationState =
                JarngreiprClientAnimation.getState();

        // Brutal Swing Charging Animation
        if (animationState == JarngreiprClientAnimation.State.CHARGING) {
            float progress =
                    JarngreiprClientAnimation.getChargeProgress();

            float outwardAngle =
                    (float) Math.toRadians(-45.0f);

            float backwardAngle =
                    (float) Math.toRadians(-70.0f * progress);

            this.rightArm.xRot = outwardAngle + backwardAngle;
            this.rightArm.yRot =
                    (float) Math.toRadians(-10.0f * progress);
            this.rightArm.zRot =
                    (float) Math.toRadians(-15.0f * progress);

            return;
        }



        // Brutal Swing: Tray pose with a leftward sweep
        if (animationState == JarngreiprClientAnimation.State.SWINGING) {
            float progress =
                    JarngreiprClientAnimation.getSwingProgress(0.0f);

            float easedProgress =
                    progress * progress * (3.0f - 2.0f * progress);

            // Extend the arm straight outward.
            this.rightArm.xRot =
                    (float) Math.toRadians(-90.0f);

            // Sweep 45 degrees across the player to their left.
            this.rightArm.yRot =
                    (float) Math.toRadians(
                            45.0f - 45.0f * easedProgress
                    );

            // Rotate the arm into the tray-holding pose.
            this.rightArm.zRot =
                    (float) Math.toRadians(-90.0f);

            return;
        }





        // Brutal Swing: snap back to the normal arm pose.
        if (animationState == JarngreiprClientAnimation.State.RECOVERY) {
            this.rightArm.xRot = 0.0f;
            this.rightArm.yRot = 0.0f;
            this.rightArm.zRot = 0.0f;
            return;
        }

        // Existing Overhead animation - preserved
        if (!JarngreiprClientAnimation.isPlaying()) {
            return;
        }

        int tick =
                JarngreiprClientAnimation.getElapsedTicks();

        float angle;

        if (tick <= 4) {
            float progress = tick / 4.0f;
            angle = (float) Math.toRadians(-160.0f * progress);

        } else if (tick <= 8) {
            angle = (float) Math.toRadians(-160.0f);

        } else {
            float progress = (tick - 9) / 6.0f;
            progress = Math.min(progress, 1.0f);

            angle = (float) Math.toRadians(
                    20.0f * (1.0f - progress)
            );
        }

        this.rightArm.xRot = angle;
        this.rightArm.yRot = 0.0f;
        this.rightArm.zRot = 0.0f;
    }
}