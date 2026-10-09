package ellipec.divinerelics.powers.ability;

import com.geckolib.animatable.GeoItem;
import ellipec.divinerelics.item.JarngreiprItem;
import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.powers.passive.JarngreiprPassives;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
                        0.

                );

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