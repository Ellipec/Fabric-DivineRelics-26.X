package ellipec.divinerelics.entity;

import ellipec.divinerelics.item.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class DragonScaleEntity extends ThrowableItemProjectile {

    private int airborneTicks = 0;
    private int stuckTicks = 0;

    private boolean stuckInBlock = false;
    private boolean stuckInEntity = false;

    private Entity stuckEntity;
    private Vec3 stuckOffset = Vec3.ZERO;

    // Remembers the direction the scale was travelling.
    private Vec3 renderDirection = Vec3.ZERO;

    public DragonScaleEntity(
            EntityType<? extends ThrowableItemProjectile> type,
            Level level
    ) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DRAGON_SCALE;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {

        // Never hit the player who fired the scale.
        if (entity == this.getOwner()) {
            return false;
        }

        // Don't hit anything while already attached.
        if (stuckInBlock || stuckInEntity) {
            return false;
        }

        return super.canHitEntity(entity);
    }

    @Override
    public void tick() {

        if (stuckInBlock) {

            stuckTicks++;

            if (stuckTicks >= 100) {
                this.discard();
                return;
            }

            this.setDeltaMovement(0, 0, 0);
            this.setNoGravity(true);

            return;
        }

        if (stuckInEntity) {

            stuckTicks++;

            if (stuckTicks >= 100) {
                this.discard();
                return;
            }

            if (stuckEntity == null || !stuckEntity.isAlive()) {
                this.discard();
                return;
            }

            // Move with the entity that was hit.
            Vec3 entityPosition = stuckEntity.position();

            this.setPos(
                    entityPosition.x + stuckOffset.x,
                    entityPosition.y + stuckOffset.y,
                    entityPosition.z + stuckOffset.z
            );

            this.setDeltaMovement(0, 0, 0);
            this.setNoGravity(true);

            return;
        }

        airborneTicks++;

        if (airborneTicks >= 100) {
            this.discard();
            return;
        }

        super.tick();

        // Remember the direction while the scale is flying.
        Vec3 movement = this.getDeltaMovement();

        if (movement.lengthSqr() > 0.0001) {
            this.renderDirection = movement.normalize();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {

        Entity entity = hitResult.getEntity();

        // Safety check so the owner can never be affected.
        if (entity == this.getOwner()) {
            return;
        }

        // Remember the direction BEFORE stopping the projectile.
        Vec3 movement = this.getDeltaMovement();

        if (movement.lengthSqr() > 0.0001) {
            this.renderDirection = movement.normalize();
        }

        if (entity instanceof LivingEntity livingEntity) {

            // Remove existing effects first.
            livingEntity.removeEffect(MobEffects.SLOWNESS);
            livingEntity.removeEffect(MobEffects.WEAKNESS);

            // Slowness II for 3 seconds.
            livingEntity.addEffect(
                    new MobEffectInstance(
                            MobEffects.SLOWNESS,
                            60,
                            1
                    )
            );

            // Weakness I for 3 seconds.
            livingEntity.addEffect(
                    new MobEffectInstance(
                            MobEffects.WEAKNESS,
                            60,
                            0
                    )
            );

            // Reset Minecraft's short damage immunity so that
            // multiple scales can damage the same target.
            livingEntity.invulnerableTime = 0;
        }

        // 8 HP = 4 hearts.
        entity.hurt(
                this.damageSources().thrown(
                        this,
                        this.getOwner()
                ),
                8.0f
        );

        // Remember the entity we hit.
        this.stuckEntity = entity;

        // Store where the scale hit relative to the entity.
        this.stuckOffset = this.position().subtract(
                entity.position()
        );

        // Stop the projectile.
        this.setDeltaMovement(0, 0, 0);
        this.setNoGravity(true);

        this.stuckInEntity = true;
        this.stuckTicks = 0;
    }

    @Override
    protected void onHit(HitResult hitResult) {

        // Remember the direction BEFORE stopping the projectile.
        Vec3 movement = this.getDeltaMovement();

        if (movement.lengthSqr() > 0.0001) {
            this.renderDirection = movement.normalize();
        }

        super.onHit(hitResult);

        if (hitResult.getType() == HitResult.Type.BLOCK) {

            this.setDeltaMovement(0, 0, 0);
            this.setNoGravity(true);

            this.stuckInBlock = true;
            this.stuckTicks = 0;
        }
    }

    public Vec3 getRenderDirection() {
        return this.renderDirection;
    }
}