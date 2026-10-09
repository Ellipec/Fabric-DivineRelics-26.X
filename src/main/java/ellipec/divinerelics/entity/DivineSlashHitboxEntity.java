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

import java.util.List;

public class DivineSlashHitboxEntity extends Entity {

    private DivineSlashEntity divineSlash;

    public DivineSlashHitboxEntity(
            EntityType<? extends DivineSlashHitboxEntity> entityType,
            Level level
    ) {
        super(entityType, level);

        this.setNoGravity(true);
    }

    public void setDivineSlash(DivineSlashEntity divineSlash) {
        this.divineSlash = divineSlash;
    }

    public DivineSlashEntity getDivineSlash() {
        return this.divineSlash;
    }

    @Override
    public void tick() {
        super.tick();

        this.setDeltaMovement(0, 0, 0);

        if (!this.level().isClientSide()) {
            checkForEntityHit();
        }
    }

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

            if (divineSlash != null
                    && entity == divineSlash.getOwner()) {
                continue;
            }

            if (divineSlash != null) {
                divineSlash.triggerEntityHit();
            }

            return;
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