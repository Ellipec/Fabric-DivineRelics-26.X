package ellipec.divinerelics.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GustHitboxEntity extends Entity {

    private static final float DAMAGE = 2.0f;
    private static final double KNOCKBACK_STRENGTH = 1.0;

    private GustEntity gust;

    public GustHitboxEntity(
            EntityType<? extends GustHitboxEntity> entityType,
            Level level
    ) {
        super(entityType, level);

        this.setNoGravity(true);
    }

    /**
     * Sets the Gust that created this hitbox.
     */
    public void setGust(GustEntity gust) {
        this.gust = gust;
    }

    /**
     * Gets the Gust that created this hitbox.
     */
    public GustEntity getGust() {
        return this.gust;
    }

    @Override
    public void tick() {
        super.tick();

        // The GustEntity controls this hitbox's position.
        this.setDeltaMovement(0, 0, 0);

        // Only check for targets on the server.
        if (!this.level().isClientSide()) {
            checkForEntityHit();
        }
    }

    /**
     * Checks whether this hitbox is touching an entity.
     */
    private void checkForEntityHit() {

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        List<LivingEntity> entities =
                serverLevel.getEntitiesOfClass(
                        LivingEntity.class,
                        this.getBoundingBox(),
                        entity -> entity.isAlive()
                );

        for (LivingEntity entity : entities) {

            // Don't hit the entity that fired the Gust.
            if (gust != null && entity == gust.getOwner()) {
                continue;
            }

            // Don't hit an entity while it is immune to
            // further Gust damage.
            if (gust != null && gust.isGustImmune(entity)) {
                continue;
            }

            // Create the damage source using the player who fired the Gust.
            DamageSource damageSource =
                    serverLevel.damageSources().mobAttack(
                            gust != null && gust.getOwner() != null
                                    ? gust.getOwner()
                                    : entity
                    );

            // Reset Minecraft's short damage immunity so that
            // multiple Gust hitboxes can damage the same target.
            entity.invulnerableTime = 0;

            // Deal damage.
            if (entity.hurtServer(
                    serverLevel,
                    damageSource,
                    DAMAGE
            )) {

                // Push the entity in the direction the Gust is travelling.
                if (gust != null) {

                    Vec3 gustDirection =
                            gust.getGustDirection();

                    entity.push(
                            gustDirection.x * KNOCKBACK_STRENGTH,
                            0.15,
                            gustDirection.z * KNOCKBACK_STRENGTH
                    );

                    entity.hurtMarked = true;

                    // Start the Gust's delayed immunity timer.
                    //
                    // The target can still be hit by other
                    // hitboxes during the initial 0.5 second window.
                    gust.startGustImmunity(entity);

                    // Tell the Gust to watch this entity for a
                    // possible wall impact.
                    gust.trackWallImpact(entity);
                }

                // Only this hitbox disappears after
                // successfully damaging an entity.
                this.discard();

                return;
            }
        }
    }

    @Override
    protected void defineSynchedData(
            SynchedEntityData.Builder builder
    ) {
    }

    @Override
    protected void readAdditionalSaveData(
            ValueInput input
    ) {
    }

    @Override
    protected void addAdditionalSaveData(
            ValueOutput output
    ) {
    }

    @Override
    public boolean hurtServer(
            ServerLevel level,
            DamageSource source,
            float amount
    ) {
        return false;
    }
}