package ellipec.divinerelics.powers.ability;

import ellipec.divinerelics.entity.ModEntities;
import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class DragonRuinAbilities {

    public static final int DRAGONSTEP_COOLDOWN = 200;

    // 15 seconds = 300 ticks.
    public static final int SCALE_SHOT_COOLDOWN = 300;

    private static final Map<UUID, Long> dragonStepCooldowns = new HashMap<>();
    private static final Map<UUID, Long> scaleShotCooldowns = new HashMap<>();

    private static final Map<UUID, PendingScaleShot> pendingScaleShots =
            new HashMap<>();

    static {
        ServerTickEvents.END_SERVER_TICK.register(
                DragonRuinAbilities::tick
        );
    }

    public static void dragonStep(ServerPlayer player) {

        if (!player.getMainHandItem().is(ModItems.DRAGONS_RUIN)) {
            return;
        }

        long currentTime = player.level().getGameTime();

        if (dragonStepCooldowns.getOrDefault(
                player.getUUID(),
                0L
        ) > currentTime) {
            return;
        }

        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle();

        double teleportDistance = 10.0;

        Vec3 destination = player.position().add(
                look.x * teleportDistance,
                look.y * teleportDistance,
                look.z * teleportDistance
        );

        var hitResult = player.level().clip(
                new net.minecraft.world.level.ClipContext(
                        start,
                        start.add(look.scale(teleportDistance)),
                        net.minecraft.world.level.ClipContext.Block.COLLIDER,
                        net.minecraft.world.level.ClipContext.Fluid.NONE,
                        player
                )
        );

        if (hitResult.getType()
                == net.minecraft.world.phys.HitResult.Type.BLOCK) {

            Vec3 hitPosition = hitResult.getLocation();
            Vec3 direction = look.normalize();

            destination = hitPosition.subtract(
                    direction.x * 0.5,
                    direction.y * 0.5,
                    direction.z * 0.5
            );
        }

        while (!player.level().noCollision(
                player.getBoundingBox().move(
                        destination.x - player.getX(),
                        destination.y - player.getY(),
                        destination.z - player.getZ()
                )
        )) {
            destination = destination.add(0, 1, 0);
        }

        player.teleportTo(
                destination.x,
                destination.y,
                destination.z
        );

        dragonStepCooldowns.put(
                player.getUUID(),
                currentTime + DRAGONSTEP_COOLDOWN
        );
    }

    public static void scaleShot(ServerPlayer player) {

        if (!player.getMainHandItem().is(ModItems.DRAGONS_RUIN)) {
            return;
        }

        long currentTime = player.level().getGameTime();

        // Server-side 3 second cooldown.
        if (scaleShotCooldowns.getOrDefault(
                player.getUUID(),
                0L
        ) > currentTime) {
            return;
        }

        // Fire the first scale immediately.
        fireScale(player);

        // Schedule two more scales.
        pendingScaleShots.put(
                player.getUUID(),
                new PendingScaleShot(
                        4,
                        2
                )
        );

        // Start the 3 second cooldown.
        scaleShotCooldowns.put(
                player.getUUID(),
                currentTime + SCALE_SHOT_COOLDOWN
        );
    }

    private static void fireScale(ServerPlayer player) {

        // Get the player's CURRENT facing direction.
        Vec3 direction = player.getLookAngle().normalize();

        var scale = new ellipec.divinerelics.entity.DragonScaleEntity(
                ModEntities.DRAGON_SCALE,
                player.level()
        );

        scale.setPos(
                player.getX(),
                player.getEyeY() - 0.2,
                player.getZ()
        );

        // Remember which player fired the scale.
        scale.setOwner(player);

        scale.shoot(
                direction.x,
                direction.y,
                direction.z,
                2.5f,
                0.0f
        );

        player.level().addFreshEntity(scale);
    }

    private static void tick(MinecraftServer server) {

        Iterator<Map.Entry<UUID, PendingScaleShot>> iterator =
                pendingScaleShots.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<UUID, PendingScaleShot> entry =
                    iterator.next();

            UUID uuid = entry.getKey();
            PendingScaleShot pending = entry.getValue();

            ServerPlayer player =
                    server.getPlayerList().getPlayer(uuid);

            // Remove the scheduled shots if the player is offline.
            if (player == null) {
                iterator.remove();
                continue;
            }

            pending.ticksUntilNext--;

            if (pending.ticksUntilNext <= 0) {

                // This gets the player's CURRENT direction.
                fireScale(player);

                pending.shotsRemaining--;

                if (pending.shotsRemaining <= 0) {

                    iterator.remove();

                } else {

                    // Wait another 4 ticks before firing the final scale.
                    pending.ticksUntilNext = 4;
                }
            }
        }
    }

    private static class PendingScaleShot {

        private int ticksUntilNext;
        private int shotsRemaining;

        private PendingScaleShot(
                int ticksUntilNext,
                int shotsRemaining
        ) {
            this.ticksUntilNext = ticksUntilNext;
            this.shotsRemaining = shotsRemaining;
        }
    }
}