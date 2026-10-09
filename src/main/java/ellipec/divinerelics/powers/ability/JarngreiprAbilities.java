package ellipec.divinerelics.powers.ability;

import com.geckolib.animatable.GeoItem;
import ellipec.divinerelics.item.JarngreiprItem;
import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.powers.passive.JarngreiprPassives;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class JarngreiprAbilities {

    public static final int OVERHEAD_COOLDOWN = 300;

    private static final int OVERHEAD_IMPACT_DELAY = 9;

    private static final Map<UUID, Long> overheadCooldowns =
            new HashMap<>();

    private static final Map<UUID, Long> pendingOverheads =
            new HashMap<>();

    public static void overhead(ServerPlayer player) {

        if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
            return;
        }

        long currentTime =
                player.level().getGameTime();

        if (overheadCooldowns.getOrDefault(
                player.getUUID(),
                0L
        ) > currentTime) {
            return;
        }

        if (player.level() instanceof ServerLevel serverLevel) {

            JarngreiprItem item =
                    (JarngreiprItem) player.getMainHandItem().getItem();

            item.triggerAnim(
                    player,
                    GeoItem.getOrAssignId(
                            player.getMainHandItem(),
                            serverLevel
                    ),
                    "overhead_controller",
                    "overhead"
            );
        }

        pendingOverheads.put(
                player.getUUID(),
                currentTime + OVERHEAD_IMPACT_DELAY
        );

        overheadCooldowns.put(
                player.getUUID(),
                currentTime + OVERHEAD_COOLDOWN
        );
    }

    public static void tick(MinecraftServer server) {

        if (pendingOverheads.isEmpty()) {
            return;
        }

        long currentTime =
                server.overworld().getGameTime();

        Iterator<Map.Entry<UUID, Long>> iterator =
                pendingOverheads.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<UUID, Long> entry =
                    iterator.next();

            UUID playerUUID =
                    entry.getKey();

            long impactTime =
                    entry.getValue();

            if (currentTime < impactTime) {
                continue;
            }

            ServerPlayer player =
                    server.getPlayerList().getPlayer(playerUUID);

            iterator.remove();

            if (player == null) {
                continue;
            }

            if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
                continue;
            }

            performOverheadImpact(player);
        }
    }

    private static void performOverheadImpact(
            ServerPlayer player
    ) {

        Vec3 center =
                player.position().add(
                        0.0,
                        1.0,
                        0.0
                );

        // Create the ground impact particles

        if (player.level() instanceof ServerLevel serverLevel) {

            BlockState groundState =
                    serverLevel.getBlockState(
                            player.blockPosition().below()
                    );

            if (!groundState.isAir()) {

                BlockParticleOption debris =
                        new BlockParticleOption(
                                ParticleTypes.BLOCK,
                                groundState
                        );

                var random =
                        serverLevel.getRandom();

                for (int i = 0; i < 50; i++) {

                    double x =
                            player.getX()
                                    + (random.nextDouble() - 0.5) * 3.0;

                    double z =
                            player.getZ()
                                    + (random.nextDouble() - 0.5) * 3.0;

                    double y =
                            player.getY() + 0.05;

                    double outwardX =
                            x - player.getX();

                    double outwardZ =
                            z - player.getZ();

                    double length =
                            Math.sqrt(
                                    outwardX * outwardX
                                            + outwardZ * outwardZ
                            );

                    if (length > 0.0) {

                        outwardX /= length;
                        outwardZ /= length;
                    }

                    double speed =
                            0.15
                                    + random.nextDouble() * 0.25;

                    double velocityX =
                            outwardX * speed;

                    double velocityZ =
                            outwardZ * speed;

                    double velocityY =
                            0.25
                                    + random.nextDouble() * 0.45;

                    serverLevel.sendParticles(
                            debris,
                            x,
                            y,
                            z,
                            2,
                            velocityX,
                            velocityY,
                            velocityZ,
                            1.0
                    );
                }
            }
        }

        // Hitbox: 3 blocks wide, tall, and deep

        AABB hitbox =
                new AABB(
                        center.x - 1.5,
                        center.y - 1.5,
                        center.z - 1.5,
                        center.x + 1.5,
                        center.y + 1.5,
                        center.z + 1.5
                );

        for (LivingEntity target :
                player.level().getEntitiesOfClass(
                        LivingEntity.class,
                        hitbox,
                        entity -> entity != player
                )) {

            float damageMultiplier =
                    JarngreiprPassives.getDamageMultiplier(
                            player,
                            target
                    );

            float damage =
                    22.0f * damageMultiplier;

            // Deal the damage

            target.hurt(
                    player.damageSources().playerAttack(player),
                    damage
            );

            JarngreiprPassives.recordHit(
                    player,
                    target
            );

            Vec3 knockback =
                    target.position()
                            .subtract(player.position());

            if (knockback.lengthSqr() > 0.0) {

                knockback =
                        knockback.normalize();

                target.push(
                        knockback.x * 1.5,
                        0.5,
                        knockback.z * 1.5
                );

                target.hurtMarked = true;
            }
        }
    }
}