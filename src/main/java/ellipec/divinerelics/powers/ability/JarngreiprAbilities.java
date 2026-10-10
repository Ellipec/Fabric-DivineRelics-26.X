package ellipec.divinerelics.powers.ability;

import com.geckolib.animatable.GeoItem;
import ellipec.divinerelics.item.JarngreiprItem;
import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.powers.passive.JarngreiprPassives;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class JarngreiprAbilities {

    public static final int OVERHEAD_COOLDOWN = 300;
    public static final int BRUTAL_SWING_COOLDOWN = 0;
    public static final int BRUTAL_SWING_MAX_CHARGE = 60;

    private static final int OVERHEAD_IMPACT_DELAY = 9;

    private static final Identifier BRUTAL_SWING_SLOW_ID =
            Identifier.fromNamespaceAndPath(
                    "divinerelics",
                    "brutal_swing_slow"
            );

    private static final Map<UUID, Long> overheadCooldowns =
            new HashMap<>();

    private static final Map<UUID, Long> pendingOverheads =
            new HashMap<>();

    private static final Map<UUID, Long> brutalSwingChargeStarts =
            new HashMap<>();

    private static final Map<UUID, Long> brutalSwingCooldowns =
            new HashMap<>();

    public static void overhead(ServerPlayer player) {

        if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
            return;
        }

        long currentTime = player.level().getGameTime();

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

    public static void startBrutalSwingCharge(ServerPlayer player) {

        if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
            return;
        }

        long currentTime =
                player.level().getServer().overworld().getGameTime();

        if (brutalSwingCooldowns.getOrDefault(
                player.getUUID(),
                0L
        ) > currentTime) {
            return;
        }

        if (brutalSwingChargeStarts.putIfAbsent(
                player.getUUID(),
                currentTime
        ) == null) {

            AttributeModifier movementPenalty =
                    new AttributeModifier(
                            BRUTAL_SWING_SLOW_ID,
                            -0.7,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    );

            player.getAttribute(Attributes.MOVEMENT_SPEED)
                    .addOrUpdateTransientModifier(movementPenalty);
        }
    }

    public static void releaseBrutalSwing(ServerPlayer player) {

        UUID playerUUID = player.getUUID();

        Long chargeStart =
                brutalSwingChargeStarts.remove(playerUUID);

        player.getAttribute(Attributes.MOVEMENT_SPEED)
                .removeModifier(BRUTAL_SWING_SLOW_ID);

        if (chargeStart == null) {
            return;
        }

        if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
            return;
        }

        long currentTime =
                player.level().getServer().overworld().getGameTime();

        int chargeTicks = (int) Math.min(
                BRUTAL_SWING_MAX_CHARGE,
                Math.max(0L, currentTime - chargeStart)
        );

        if (brutalSwingCooldowns.getOrDefault(
                playerUUID,
                0L
        ) > currentTime) {
            return;
        }

        brutalSwingCooldowns.put(
                playerUUID,
                currentTime + BRUTAL_SWING_COOLDOWN
        );

        performBrutalSwing(player, chargeTicks);
    }

    public static void tick(MinecraftServer server) {

        long currentTime = server.overworld().getGameTime();

        Iterator<Map.Entry<UUID, Long>> overheadIterator =
                pendingOverheads.entrySet().iterator();

        while (overheadIterator.hasNext()) {

            Map.Entry<UUID, Long> entry =
                    overheadIterator.next();

            if (currentTime < entry.getValue()) {
                continue;
            }

            ServerPlayer player =
                    server.getPlayerList().getPlayer(entry.getKey());

            overheadIterator.remove();

            if (player == null) {
                continue;
            }

            if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
                continue;
            }

            performOverheadImpact(player);
        }

        // Cancel charging if the player switches weapons.
        Iterator<Map.Entry<UUID, Long>> chargeIterator =
                brutalSwingChargeStarts.entrySet().iterator();

        while (chargeIterator.hasNext()) {

            Map.Entry<UUID, Long> entry =
                    chargeIterator.next();

            ServerPlayer player =
                    server.getPlayerList().getPlayer(entry.getKey());

            if (player == null) {
                chargeIterator.remove();
                continue;
            }

            if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {

                player.getAttribute(Attributes.MOVEMENT_SPEED)
                        .removeModifier(BRUTAL_SWING_SLOW_ID);

                chargeIterator.remove();
            }
        }
    }

    private static void performOverheadImpact(ServerPlayer player) {

        Vec3 center = player.position().add(
                0.0,
                1.0,
                0.0
        );

        // Create the ground impact particles.
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

                var random = serverLevel.getRandom();

                for (int i = 0; i < 50; i++) {

                    double x =
                            player.getX()
                                    + (random.nextDouble() - 0.5) * 3.0;

                    double z =
                            player.getZ()
                                    + (random.nextDouble() - 0.5) * 3.0;

                    double y = player.getY() + 0.05;

                    double outwardX = x - player.getX();
                    double outwardZ = z - player.getZ();

                    double length = Math.sqrt(
                            outwardX * outwardX
                                    + outwardZ * outwardZ
                    );

                    if (length > 0.0) {
                        outwardX /= length;
                        outwardZ /= length;
                    }

                    double speed =
                            0.15 + random.nextDouble() * 0.25;

                    double velocityX = outwardX * speed;
                    double velocityZ = outwardZ * speed;

                    double velocityY =
                            0.25 + random.nextDouble() * 0.45;

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

        // Original Overhead hitbox.
        AABB hitbox = new AABB(
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

            float damage = 22.0f * damageMultiplier;

            target.hurt(
                    player.damageSources().playerAttack(player),
                    damage
            );

            JarngreiprPassives.recordHit(player, target);

            Vec3 knockback =
                    target.position().subtract(player.position());

            if (knockback.lengthSqr() > 0.0) {

                knockback = knockback.normalize();

                target.push(
                        knockback.x * 1.5,
                        0.5,
                        knockback.z * 1.5
                );

                target.hurtMarked = true;
            }
        }
    }

    private static void performBrutalSwing(
            ServerPlayer player,
            int chargeTicks
    ) {

        float chargeProgress = Math.min(
                1.0f,
                chargeTicks / (float) BRUTAL_SWING_MAX_CHARGE
        );

        Vec3 forward = player.getLookAngle().multiply(
                1.0,
                0.0,
                1.0
        );

        if (forward.lengthSqr() < 0.0001) {
            return;
        }

        forward = forward.normalize();

        Vec3 right = new Vec3(
                -forward.z,
                0.0,
                forward.x
        );

        Vec3 center = player.position()
                .add(forward.scale(2.75))
                .add(0.0, 1.0, 0.0);

        double forwardReach = 3.25;
        double sideReach = 2.75;

        double xRadius =
                Math.abs(forward.x) * forwardReach
                        + Math.abs(right.x) * sideReach;

        double zRadius =
                Math.abs(forward.z) * forwardReach
                        + Math.abs(right.z) * sideReach;

        AABB searchBox = new AABB(
                center.x - xRadius,
                player.getY() - 0.5,
                center.z - zRadius,
                center.x + xRadius,
                player.getY() + 2.5,
                center.z + zRadius
        );

        float baseDamage = 8.0f + (12.0f * chargeProgress);
        double knockbackStrength = 1.0 + (0.75 * chargeProgress);

        for (LivingEntity target :
                player.level().getEntitiesOfClass(
                        LivingEntity.class,
                        searchBox,
                        entity -> entity != player
                )) {

            Vec3 relativePosition =
                    target.position().subtract(player.position());

            double forwardDistance =
                    relativePosition.x * forward.x
                            + relativePosition.z * forward.z;

            double sideDistance =
                    relativePosition.x * right.x
                            + relativePosition.z * right.z;

            if (forwardDistance < 0.0
                    || forwardDistance > 6.0
                    || Math.abs(sideDistance) > sideReach) {
                continue;
            }

            // Calculate Momentum before recording this hit.
            float damageMultiplier =
                    JarngreiprPassives.getDamageMultiplier(
                            player,
                            target
                    );

            float damage = baseDamage * damageMultiplier;

            target.hurt(
                    player.damageSources().playerAttack(player),
                    damage
            );

            JarngreiprPassives.recordHit(player, target);

            Vec3 knockback = new Vec3(
                    forward.x * knockbackStrength,
                    0.15,
                    forward.z * knockbackStrength
            );

            target.push(
                    knockback.x,
                    knockback.y,
                    knockback.z
            );

            target.hurtMarked = true;
        }
    }
}