package ellipec.divinerelics.mixin;

import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.powers.passive.JarngreiprPassives;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class JarngreiprMomentumMixin {

    private LivingEntity jarngreipr$currentTarget;
    private boolean jarngreipr$fullyChargedAttack;

    @Inject(
            method = "attack",
            at = @At("HEAD")
    )
    private void jarngreiprStartAttack(
            Entity target,
            CallbackInfo ci
    ) {
        Player player =
                (Player) (Object) this;

        this.jarngreipr$fullyChargedAttack =
                player.getAttackStrengthScale(0.5F) >= 1.0F;

        if (target instanceof LivingEntity livingTarget) {
            this.jarngreipr$currentTarget = livingTarget;
        } else {
            this.jarngreipr$currentTarget = null;
        }
    }

    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private boolean jarngreiprModifyDamage(
            Entity target,
            DamageSource source,
            float damage
    ) {
        Player player =
                (Player) (Object) this;

        if (player.level().isClientSide()) {
            return target.hurtOrSimulate(
                    source,
                    damage
            );
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return target.hurtOrSimulate(
                    source,
                    damage
            );
        }

        if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
            return target.hurtOrSimulate(
                    source,
                    damage
            );
        }

        if (!(target instanceof LivingEntity livingTarget)) {
            return target.hurtOrSimulate(
                    source,
                    damage
            );
        }

        float multiplier =
                JarngreiprPassives.getDamageMultiplier(
                        serverPlayer,
                        livingTarget
                );

        return target.hurtOrSimulate(
                source,
                damage * multiplier
        );
    }

    @Inject(
            method = "attack",
            at = @At("TAIL")
    )
    private void jarngreiprFinishAttack(
            Entity target,
            CallbackInfo ci
    ) {
        Player player =
                (Player) (Object) this;

        if (player.level().isClientSide()) {
            return;
        }

        if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
            return;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (!(target instanceof LivingEntity livingTarget)) {
            return;
        }

        if (!livingTarget.isAlive()) {
            return;
        }

        if (this.jarngreipr$fullyChargedAttack) {
            JarngreiprPassives.recordHit(
                    serverPlayer,
                    livingTarget
            );
        } else {
            JarngreiprPassives.resetTimer(
                    serverPlayer,
                    livingTarget
            );
        }
    }
}