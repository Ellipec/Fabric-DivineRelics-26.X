package ellipec.divinerelics.mixin;

import ellipec.divinerelics.DivineRelicsClient;
import ellipec.divinerelics.item.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class JarngreiprMomentumRenderMixin {

    @Inject(
            method = "extractRenderState",
            at = @At("TAIL")
    )
    private void applyJarngreiprMomentumGlow(
            Entity entity,
            EntityRenderState state,
            float partialTick,
            CallbackInfo ci
    ) {

        if (!isJarngreiprActive()) {
            return;
        }

        int momentum =
                DivineRelicsClient.getMomentum(
                        entity.getId()
                );

        int color =
                getMomentumColor(momentum);

        if (color != EntityRenderState.NO_OUTLINE) {
            state.outlineColor = color;
        }
    }

    private static boolean isJarngreiprActive() {

        var minecraft =
                net.minecraft.client.Minecraft.getInstance();

        return minecraft.player != null
                && minecraft.player.getMainHandItem()
                .is(ModItems.JARNGREIPR);
    }

    private static int getMomentumColor(
            int momentum
    ) {

        return switch (momentum) {
            case 2 -> 0xFF00FF00;
            case 3 -> 0xFFFFFF00;
            case 4 -> 0xFFFF5B00;
            case 5 -> 0xFFEC0D08;
            case 6 -> 0xFF8B0000;
            default -> EntityRenderState.NO_OUTLINE;
        };
    }
}