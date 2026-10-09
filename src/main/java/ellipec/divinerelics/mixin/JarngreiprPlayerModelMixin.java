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

        if (!JarngreiprClientAnimation.isPlaying()) {
            return;
        }

        int tick =
                JarngreiprClientAnimation.getElapsedTicks();

        float angle;

        if (tick <= 4) {

            float progress =
                    tick / 4.0f;

            angle =
                    (float) Math.toRadians(
                            -160.0f * progress
                    );

        } else if (tick <= 8) {

            angle =
                    (float) Math.toRadians(
                            -160.0f
                    );

        } else {

            float progress =
                    (tick - 9) / 6.0f;

            progress =
                    Math.min(
                            progress,
                            1.0f
                    );

            angle =
                    (float) Math.toRadians(
                            20.0f * (1.0f - progress)
                    );
        }

        this.rightArm.xRot = angle;
        this.rightArm.yRot = 0.0f;
        this.rightArm.zRot = 0.0f;
    }
}