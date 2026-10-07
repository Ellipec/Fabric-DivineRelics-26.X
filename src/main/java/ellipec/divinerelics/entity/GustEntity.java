package ellipec.divinerelics.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GustEntity extends Entity implements GeoEntity {

    private final AnimatableInstanceCache geoCache =
            GeckoLibUtil.createInstanceCache(this);

    private final List<GustHitboxEntity> hitboxes =
            new ArrayList<>();

    // The entity that fired the Gust.
    private LivingEntity owner;

    // Entities that have been hit and are currently being
    // watched for a wall impact.
    private final List<LivingEntity> wallImpactTargets =
            new ArrayList<>();

    // Stores the tick when each entity was first hit by this Gust.
    private final Map<UUID, Long> gustImmunityTimes =
            new HashMap<>();

    public GustEntity(
            EntityType<? extends GustEntity> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    /**
     * Sets the entity that fired the Gust.
     */
    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    /**
     * Gets the entity that fired the Gust.
     */
    public LivingEntity getOwner() {
        return this.owner;
    }

    /**
     * Checks whether an entity is currently immune
     * to further Gust damage.
     *
     * The first 10 ticks after the first successful hit
     * are a multi-hit window.
     *
     * After that, the entity becomes immune for 100 ticks.
     */
    public boolean isGustImmune(LivingEntity entity) {

        Long firstHitTick =
                gustImmunityTimes.get(entity.getUUID());

        if (firstHitTick == null) {
            return false;
        }

        long elapsed =
                (long) this.tickCount - firstHitTick;

        // First 10 ticks = 0.5 seconds.
        // Multiple Gust hitboxes can hit during this time.
        if (elapsed < 10) {
            return false;
        }

        // From tick 10 to tick 109 = 5 seconds of immunity.
        if (elapsed < 110) {
            return true;
        }

        // Immunity has expired.
        gustImmunityTimes.remove(entity.getUUID());

        return false;
    }

    /**
     * Starts the Gust's immunity timer after
     * the entity's first successful hit.
     */
    public void startGustImmunity(LivingEntity entity) {

        // Only start the timer on the first successful hit.
        if (!gustImmunityTimes.containsKey(entity.getUUID())) {

            gustImmunityTimes.put(
                    entity.getUUID(),
                    (long) this.tickCount
            );
        }
    }

    /**
     * Adds an entity to the list of targets that should
     * be watched for a wall impact.
     */
    public void trackWallImpact(LivingEntity entity) {

        if (!wallImpactTargets.contains(entity)) {
            wallImpactTargets.add(entity);
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Move the Gust forward.
        this.move(MoverType.SELF, this.getDeltaMovement());

        // Gradually slow the Gust down.
        this.setDeltaMovement(
                this.getDeltaMovement().scale(0.98)
        );

        // Keep every hitbox attached to the Gust.
        if (!this.level().isClientSide()) {
            updateHitboxPositions();

            checkWallImpacts();
        }

        // Remove the Gust and all of its hitboxes after 150 ticks.
        if (!this.level().isClientSide() && this.tickCount >= 150) {

            removeHitboxes();

            this.discard();
        }
    }

    /**
     * Checks whether any entity that was hit by the Gust
     * has collided with a solid block.
     */
    private void checkWallImpacts() {

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Iterator<LivingEntity> iterator =
                wallImpactTargets.iterator();

        while (iterator.hasNext()) {

            LivingEntity entity = iterator.next();

            // Stop watching dead or removed entities.
            if (!entity.isAlive() || entity.isRemoved()) {
                iterator.remove();
                continue;
            }

            // Check the entity's current bounding box against
            // solid blocks.
            if (entity.horizontalCollision) {

                DamageSource damageSource =
                        serverLevel.damageSources().mobAttack(
                                owner != null ? owner : entity
                        );

                // Prevent the bonus damage from being blocked
                // by Minecraft's normal damage immunity.
                entity.invulnerableTime = 0;

                // Wall impact bonus damage.
                entity.hurtServer(serverLevel, damageSource, 4.0f);

                // Only apply the wall impact once.
                iterator.remove();
            }
        }
    }

    /**
     * Creates all of the Gust's hitboxes immediately
     * when the Gust is fired.
     */
    public void createHitboxes() {

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        // Don't create them twice.
        if (!this.hitboxes.isEmpty()) {
            return;
        }

        // Hitbox 1
        createHitbox(serverLevel, -0.45, 0.50, 0.00);

        // Hitbox 2
        createHitbox(serverLevel, -0.325, 0.625, 0.175);

        // Hitbox 3
        createHitbox(serverLevel, -0.20, 0.75, 0.30);

        // Hitbox 4
        createHitbox(serverLevel, -0.075, 0.875, 0.475);

        // Hitbox 5
        createHitbox(serverLevel, 0.05, 1.00, 0.55);

        // Hitbox 6
        createHitbox(serverLevel, 0.175, 1.125, 0.55);

        // Hitbox 7
        createHitbox(serverLevel, 0.30, 1.25, 0.55);

        // Hitbox 8
        createHitbox(serverLevel, 0.425, 1.375, 0.50);

        // Hitbox 9
        createHitbox(serverLevel, 0.55, 1.50, 0.35);

        // Hitbox 10
        createHitbox(serverLevel, 0.675, 1.625, 0.175);

        // Hitbox 11
        createHitbox(serverLevel, 0.80, 1.75, 0.00);
    }

    /**
     * Creates one hitbox at a local position.
     */
    private void createHitbox(
            ServerLevel serverLevel,
            double x,
            double y,
            double z
    ) {

        GustHitboxEntity hitbox =
                new GustHitboxEntity(
                        ModEntities.GUST_HITBOX,
                        serverLevel
                );

        // Give the hitbox a reference to this Gust.
        hitbox.setGust(this);

        hitbox.setPos(
                this.localToWorld(x, y, z)
        );

        this.hitboxes.add(hitbox);

        serverLevel.addFreshEntity(hitbox);
    }

    /**
     * Updates the positions of all hitboxes so that
     * they follow the Gust as it moves and rotates.
     */
    private void updateHitboxPositions() {

        // Hitbox 1
        if (this.hitboxes.size() >= 1) {
            this.hitboxes.get(0).setPos(
                    this.localToWorld(-0.45, 0.50, 0.0)
            );
        }

        // Hitbox 2
        if (this.hitboxes.size() >= 2) {
            this.hitboxes.get(1).setPos(
                    this.localToWorld(-0.325, 0.625, 0.175)
            );
        }

        // Hitbox 3
        if (this.hitboxes.size() >= 3) {
            this.hitboxes.get(2).setPos(
                    this.localToWorld(-0.20, 0.75, 0.30)
            );
        }

        // Hitbox 4
        if (this.hitboxes.size() >= 4) {
            this.hitboxes.get(3).setPos(
                    this.localToWorld(-0.075, 0.875, 0.475)
            );
        }

        // Hitbox 5
        if (this.hitboxes.size() >= 5) {
            this.hitboxes.get(4).setPos(
                    this.localToWorld(0.05, 1.00, 0.55)
            );
        }

        // Hitbox 6
        if (this.hitboxes.size() >= 6) {
            this.hitboxes.get(5).setPos(
                    this.localToWorld(0.175, 1.125, 0.55)
            );
        }

        // Hitbox 7
        if (this.hitboxes.size() >= 7) {
            this.hitboxes.get(6).setPos(
                    this.localToWorld(0.30, 1.25, 0.55)
            );
        }

        // Hitbox 8
        if (this.hitboxes.size() >= 8) {
            this.hitboxes.get(7).setPos(
                    this.localToWorld(0.425, 1.375, 0.50)
            );
        }

        // Hitbox 9
        if (this.hitboxes.size() >= 9) {
            this.hitboxes.get(8).setPos(
                    this.localToWorld(0.55, 1.50, 0.35)
            );
        }

        // Hitbox 10
        if (this.hitboxes.size() >= 10) {
            this.hitboxes.get(9).setPos(
                    this.localToWorld(0.675, 1.625, 0.175)
            );
        }

        // Hitbox 11
        if (this.hitboxes.size() >= 11) {
            this.hitboxes.get(10).setPos(
                    this.localToWorld(0.80, 1.75, 0.0)
            );
        }
    }

    /**
     * Removes all of the Gust's hitboxes.
     */
    private void removeHitboxes() {

        for (GustHitboxEntity hitbox : this.hitboxes) {

            if (!hitbox.isRemoved()) {
                hitbox.discard();
            }
        }

        this.hitboxes.clear();
    }

    /**
     * Gets the direction the Gust is facing.
     */
    public Vec3 getGustDirection() {

        float yaw =
                this.getYRot() *
                        ((float) Math.PI / 180F);

        float pitch =
                this.getXRot() *
                        ((float) Math.PI / 180F);

        double x =
                -Math.sin(yaw) *
                        Math.cos(pitch);

        double y =
                -Math.sin(pitch);

        double z =
                Math.cos(yaw) *
                        Math.cos(pitch);

        return new Vec3(x, y, z).normalize();
    }

    /**
     * Converts a point from the Gust's local coordinates
     * into world coordinates.
     *
     * Local X = left/right across the crescent.
     * Local Y = up/down across the crescent.
     * Local Z = thickness/depth of the Gust.
     */
    public Vec3 localToWorld(
            double x,
            double y,
            double z
    ) {

        Vec3 forward =
                getGustDirection();

        Vec3 up =
                new Vec3(0, 1, 0);

        if (Math.abs(forward.dot(up)) > 0.99) {
            up = new Vec3(1, 0, 0);
        }

        Vec3 right =
                forward.cross(up).normalize();

        Vec3 realUp =
                right.cross(forward).normalize();

        return this.position()
                .add(right.scale(x))
                .add(realUp.scale(y))
                .add(forward.scale(z));
    }

    /**
     * Checks whether a world-space point is inside the
     * custom crescent-shaped collision volume.
     */
    public boolean isPointInsideGust(
            Vec3 worldPoint
    ) {

        Vec3 forward =
                getGustDirection();

        Vec3 up =
                new Vec3(0, 1, 0);

        if (Math.abs(forward.dot(up)) > 0.99) {
            up = new Vec3(1, 0, 0);
        }

        Vec3 right =
                forward.cross(up).normalize();

        Vec3 realUp =
                right.cross(forward).normalize();

        Vec3 relative =
                worldPoint.subtract(this.position());

        double localX =
                relative.dot(right);

        double localY =
                relative.dot(realUp);

        double localZ =
                relative.dot(forward);

        // Thickness of the Gust.
        if (Math.abs(localZ) > 0.18) {
            return false;
        }

        // Outer circle.
        double outerRadius = 1.5;

        double outerDistance =
                Math.sqrt(
                        localX * localX +
                                localY * localY
                );

        if (outerDistance > outerRadius) {
            return false;
        }

        // Inner cut-out.
        double innerRadius = 1.15;
        double innerOffsetX = 0.45;

        double innerX =
                localX - innerOffsetX;

        double innerDistance =
                Math.sqrt(
                        innerX * innerX +
                                localY * localY
                );

        if (innerDistance < innerRadius) {
            return false;
        }

        return true;
    }

    /**
     * Gets a point a certain distance in front of the Gust.
     */
    public Vec3 getCollisionPoint(
            double distance
    ) {

        return this.position().add(
                this.getGustDirection()
                        .scale(distance)
        );
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers
    ) {
        // No animations yet.
    }

    @Override
    public AnimatableInstanceCache
    getAnimatableInstanceCache() {

        return this.geoCache;
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