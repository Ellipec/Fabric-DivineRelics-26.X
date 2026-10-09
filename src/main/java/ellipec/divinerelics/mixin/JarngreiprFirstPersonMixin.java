package ellipec.divinerelics.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import ellipec.divinerelics.client.JarngreiprClientAnimation;
import ellipec.divinerelics.item.ModItems;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class JarngreiprFirstPersonMixin {

    @Inject(
            method = "submitArmWithItem",
            at = @At("HEAD")
    )
    private void jarngreiprFirstPersonArm(
            AbstractClientPlayer player,
            float partialTicks,
            float xRot,
            InteractionHand hand,
            float swingProgress,
            ItemStack stack,
            float equippedProgress,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            CallbackInfo ci
    ) {

        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }

        if (!stack.is(ModItems.JARNGREIPR)) {
            return;
        }

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
                    -160.0f * progress;

        } else if (tick <= 8) {

            angle = -160.0f;

        } else {

            float progress =
                    (tick - 9) / 6.0f;

            progress =
                    Math.min(
                            progress,
                            1.0f
                    );

            angle =
                    20.0f * (1.0f - progress);
        }

        poseStack.mulPose(
                Axis.XP.rotationDegrees(angle)
        );
    }
}