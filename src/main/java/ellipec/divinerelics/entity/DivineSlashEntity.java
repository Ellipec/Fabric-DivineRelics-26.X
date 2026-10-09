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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DivineSlashEntity extends Entity implements GeoEntity {

    private final AnimatableInstanceCache geoCache =
            GeckoLibUtil.createInstanceCache(this);

    private final List<DivineSlashHitboxEntity> hitboxes =
            new ArrayList<>();

    private LivingEntity owner;

    private final List<LivingEntity> wallImpactTargets =
            new ArrayList<>();

    private static final double COLLISION_SIZE = 0.05;
    private static final float DAMAGE = 20.4f;
    private static final double KNOCKBACK_STRENGTH = 3.0;

    public DivineSlashEntity(
            EntityType<? extends DivineSlashEntity> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    public void triggerEntityHit() {

        if (this.isRemoved()) {
            return;
        }

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Set<LivingEntity> targets =
                new HashSet<>();

        for (DivineSlashHitboxEntity hitbox : hitboxes) {

            if (hitbox.isRemoved()) {
                continue;
            }

            List<LivingEntity> entities =
                    serverLevel.getEntitiesOfClass(
                            LivingEntity.class,
                            hitbox.getBoundingBox(),
                            entity -> entity.isAlive()
                    );

            for (LivingEntity entity : entities) {

                if (entity == owner) {
                    continue;
                }

                targets.add(entity);
            }
        }

        if (owner == null) {
            removeHitboxes();
            this.discard();
            return;
        }

        DamageSource damageSource =
                serverLevel.damageSources().mobAttack(owner);
        Vec3 direction =
                getDivineSlashDirection();

        for (LivingEntity entity : targets) {

            entity.invulnerableTime = 0;

            if (entity.hurtServer(
                    serverLevel,
                    damageSource,
                    DAMAGE
            )) {

                entity.push(
                        direction.x * KNOCKBACK_STRENGTH,
                        0.15,
                        direction.z * KNOCKBACK_STRENGTH
                );

                entity.hurtMarked = true;

                trackWallImpact(entity);
            }
        }

        removeHitboxes();
        this.discard();
    }

    public void trackWallImpact(LivingEntity entity) {

        if (!wallImpactTargets.contains(entity)) {
            wallImpactTargets.add(entity);
        }
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 movement =
                this.getDeltaMovement();

        Vec3 nextPosition =
                this.position().add(movement);

        if (!this.level().isClientSide()) {

            if (collidesWithBlocks(nextPosition)) {
                removeHitboxes();
                this.discard();
                return;
            }
        }

        this.setPos(nextPosition);

        this.setDeltaMovement(
                movement.scale(0.98)
        );

        if (!this.level().isClientSide()) {
            updateHitboxPositions();
            checkWallImpacts();
        }

        if (!this.level().isClientSide() && this.tickCount >= 150) {
            removeHitboxes();
            this.discard();
        }
    }

    private boolean collidesWithBlocks(Vec3 position) {

        double[][] points = {
                {-0.45, 0.50, 0.00},
                {-0.325, 0.625, 0.175},
                {-0.20, 0.75, 0.30},
                {-0.075, 0.875, 0.475},
                {0.05, 1.00, 0.55},
                {0.175, 1.125, 0.55},
                {0.30, 1.25, 0.55},
                {0.425, 1.375, 0.50},
                {0.55, 1.50, 0.35},
                {0.675, 1.625, 0.175},
                {0.80, 1.75, 0.00}
        };

        for (double[] point : points) {

            Vec3 worldPoint =
                    localToWorldAtPosition(
                            position,
                            point[0],
                            point[1],
                            point[2]
                    );

            AABB box =
                    new AABB(
                            worldPoint.x - COLLISION_SIZE,
                            worldPoint.y - COLLISION_SIZE,
                            worldPoint.z - COLLISION_SIZE,
                            worldPoint.x + COLLISION_SIZE,
                            worldPoint.y + COLLISION_SIZE,
                            worldPoint.z + COLLISION_SIZE
                    );

            if (!this.level().getBlockCollisions(this, box)
                    .iterator()
                    .hasNext()) {
                continue;
            }

            return true;
        }

        return false;
    }

    private Vec3 localToWorldAtPosition(
            Vec3 position,
            double x,
            double y,
            double z
    ) {

        Vec3 forward =
                getDivineSlashDirection();

        Vec3 up =
                new Vec3(0, 1, 0);

        if (Math.abs(forward.dot(up)) > 0.99) {
            up = new Vec3(1, 0, 0);
        }

        Vec3 right =
                forward.cross(up).normalize();

        Vec3 realUp =
                right.cross(forward).normalize();

        return position
                .add(right.scale(x))
                .add(realUp.scale(y))
                .add(forward.scale(z));
    }

    private void checkWallImpacts() {

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Iterator<LivingEntity> iterator =
                wallImpactTargets.iterator();

        while (iterator.hasNext()) {

            LivingEntity entity =
                    iterator.next();

            if (!entity.isAlive() || entity.isRemoved()) {
                iterator.remove();
                continue;
            }

            if (entity.horizontalCollision) {

                DamageSource damageSource =
                        serverLevel.damageSources().mobAttack(
                                owner != null ? owner : entity
                        );

                entity.invulnerableTime = 0;

                entity.hurtServer(
                        serverLevel,
                        damageSource,
                        4.0f
                );

                iterator.remove();
            }
        }
    }

    public void createHitboxes() {

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (!this.hitboxes.isEmpty()) {
            return;
        }

        createHitbox(serverLevel, -0.45, 0.50, 0.00);
        createHitbox(serverLevel, -0.325, 0.625, 0.175);
        createHitbox(serverLevel, -0.20, 0.75, 0.30);
        createHitbox(serverLevel, -0.075, 0.875, 0.475);
        createHitbox(serverLevel, 0.05, 1.00, 0.55);
        createHitbox(serverLevel, 0.175, 1.125, 0.55);
        createHitbox(serverLevel, 0.30, 1.25, 0.55);
        createHitbox(serverLevel, 0.425, 1.375, 0.50);
        createHitbox(serverLevel, 0.55, 1.50, 0.35);
        createHitbox(serverLevel, 0.675, 1.625, 0.175);
        createHitbox(serverLevel, 0.80, 1.75, 0.00);
    }

    private void createHitbox(
            ServerLevel serverLevel,
            double x,
            double y,
            double z
    ) {

        DivineSlashHitboxEntity hitbox =
                new DivineSlashHitboxEntity(
                        ModEntities.DIVINE_SLASH_HITBOX,
                        serverLevel
                );

        hitbox.setDivineSlash(this);

        hitbox.setPos(
                this.localToWorld(x, y, z)
        );

        this.hitboxes.add(hitbox);

        serverLevel.addFreshEntity(hitbox);
    }

    private void updateHitboxPositions() {

        if (this.hitboxes.size() >= 1) {
            this.hitboxes.get(0).setPos(
                    this.localToWorld(-0.45, 0.50, 0.0)
            );
        }

        if (this.hitboxes.size() >= 2) {
            this.hitboxes.get(1).setPos(
                    this.localToWorld(-0.325, 0.625, 0.175)
            );
        }

        if (this.hitboxes.size() >= 3) {
            this.hitboxes.get(2).setPos(
                    this.localToWorld(-0.20, 0.75, 0.30)
            );
        }

        if (this.hitboxes.size() >= 4) {
            this.hitboxes.get(3).setPos(
                    this.localToWorld(-0.075, 0.875, 0.475)
            );
        }

        if (this.hitboxes.size() >= 5) {
            this.hitboxes.get(4).setPos(
                    this.localToWorld(0.05, 1.00, 0.55)
            );
        }

        if (this.hitboxes.size() >= 6) {
            this.hitboxes.get(5).setPos(
                    this.localToWorld(0.175, 1.125, 0.55)
            );
        }

        if (this.hitboxes.size() >= 7) {
            this.hitboxes.get(6).setPos(
                    this.localToWorld(0.30, 1.25, 0.55)
            );
        }

        if (this.hitboxes.size() >= 8) {
            this.hitboxes.get(7).setPos(
                    this.localToWorld(0.425, 1.375, 0.50)
            );
        }

        if (this.hitboxes.size() >= 9) {
            this.hitboxes.get(8).setPos(
                    this.localToWorld(0.55, 1.50, 0.35)
            );
        }

        if (this.hitboxes.size() >= 10) {
            this.hitboxes.get(9).setPos(
                    this.localToWorld(0.675, 1.625, 0.175)
            );
        }

        if (this.hitboxes.size() >= 11) {
            this.hitboxes.get(10).setPos(
                    this.localToWorld(0.80, 1.75, 0.0)
            );
        }
    }

    private void removeHitboxes() {

        for (DivineSlashHitboxEntity hitbox : this.hitboxes) {

            if (!hitbox.isRemoved()) {
                hitbox.discard();
            }
        }

        this.hitboxes.clear();
    }

    public Vec3 getDivineSlashDirection() {

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

    public Vec3 localToWorld(
            double x,
            double y,
            double z
    ) {

        return localToWorldAtPosition(
                this.position(),
                x,
                y,
                z
        );
    }

    public boolean isPointInsideDivineSlash(
            Vec3 worldPoint
    ) {

        Vec3 forward =
                getDivineSlashDirection();

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

        if (Math.abs(localZ) > 0.18) {
            return false;
        }

        double outerRadius = 1.5;

        double outerDistance =
                Math.sqrt(
                        localX * localX +
                                localY * localY
                );

        if (outerDistance > outerRadius) {
            return false;
        }

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

    public Vec3 getCollisionPoint(
            double distance
    ) {

        return this.position().add(
                this.getDivineSlashDirection()
                        .scale(distance)
        );
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers
    ) {
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