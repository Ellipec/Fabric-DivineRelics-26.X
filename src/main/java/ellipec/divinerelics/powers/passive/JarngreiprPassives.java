package ellipec.divinerelics.powers.passive;

import ellipec.divinerelics.Networking.packet.JarngreiprMomentumPayloadS2C;
import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class JarngreiprPassives {

    private static final Map<UUID, Map<UUID, MomentumData>> momentum =
            new HashMap<>();

    private static final Map<UUID, Float> lastHealth =
            new HashMap<>();

    private static final int MAX_MOMENTUM = 5;
    private static final long MOMENTUM_DECAY_TIME = 60;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(
                JarngreiprPassives::tick
        );
    }

    public static void recordHit(
            ServerPlayer player,
            LivingEntity target
    ) {

        if (!player.getMainHandItem().is(ModItems.JARNGREIPR)) {
            resetTarget(player, target);
            return;
        }

        UUID playerId = player.getUUID();
        UUID targetId = target.getUUID();

        Map<UUID, MomentumData> targets =
                momentum.computeIfAbsent(
                        playerId,
                        id -> new HashMap<>()
                );

        MomentumData data =
                targets.computeIfAbsent(
                        targetId,
                        id -> new MomentumData()
                );

        data.hits =
                Math.min(
                        data.hits + 1,
                        MAX_MOMENTUM + 1
                );

        data.lastHit =
                player.level().getGameTime();

        sendMomentum(
                player,
                target,
                data.hits
        );
    }

    public static void resetTimer(
            ServerPlayer player,
            LivingEntity target
    ) {

        Map<UUID, MomentumData> targets =
                momentum.get(player.getUUID());

        if (targets == null) {
            return;
        }

        MomentumData data =
                targets.get(target.getUUID());

        if (data == null) {
            return;
        }

        data.lastHit =
                player.level().getGameTime();
    }

    public static int getMomentum(
            ServerPlayer player,
            LivingEntity target
    ) {

        Map<UUID, MomentumData> targets =
                momentum.get(player.getUUID());

        if (targets == null) {
            return 0;
        }

        MomentumData data =
                targets.get(target.getUUID());

        if (data == null) {
            return 0;
        }

        return data.hits;
    }

    public static float getDamageMultiplier(
            ServerPlayer player,
            LivingEntity target
    ) {

        return switch (getMomentum(player, target)) {
            case 1 -> 1.10f;
            case 2 -> 1.20f;
            case 3 -> 1.30f;
            case 4 -> 1.40f;
            case 5, 6 -> 1.50f;
            default -> 1.0f;
        };
    }

    public static void resetTarget(
            ServerPlayer player,
            LivingEntity target
    ) {

        Map<UUID, MomentumData> targets =
                momentum.get(player.getUUID());

        if (targets == null) {
            return;
        }

        targets.remove(target.getUUID());

        sendMomentum(
                player,
                target,
                0
        );

        if (targets.isEmpty()) {
            momentum.remove(player.getUUID());
        }
    }

    private static void resetPlayerMomentum(
            ServerPlayer player
    ) {

        Map<UUID, MomentumData> targets =
                momentum.remove(player.getUUID());

        if (targets == null) {
            return;
        }

        for (UUID targetId : targets.keySet()) {

            LivingEntity target =
                    findTarget(
                            player,
                            targetId
                    );

            if (target != null) {
                sendMomentum(
                        player,
                        target,
                        0
                );
            }
        }
    }

    private static void tick(
            net.minecraft.server.MinecraftServer server
    ) {

        Iterator<Map.Entry<UUID, Float>> healthIterator =
                lastHealth.entrySet().iterator();

        while (healthIterator.hasNext()) {

            Map.Entry<UUID, Float> entry =
                    healthIterator.next();

            ServerPlayer player =
                    server.getPlayerList()
                            .getPlayer(entry.getKey());

            if (player == null) {
                healthIterator.remove();
                continue;
            }

            float currentHealth =
                    player.getHealth();

            float previousHealth =
                    entry.getValue();

            if (currentHealth < previousHealth) {
                resetPlayerMomentum(player);
            }

            entry.setValue(currentHealth);
        }

        for (ServerPlayer player :
                server.getPlayerList().getPlayers()) {

            lastHealth.putIfAbsent(
                    player.getUUID(),
                    player.getHealth()
            );
        }

        Iterator<Map.Entry<UUID, Map<UUID, MomentumData>>> playerIterator =
                momentum.entrySet().iterator();

        while (playerIterator.hasNext()) {

            Map.Entry<UUID, Map<UUID, MomentumData>> playerEntry =
                    playerIterator.next();

            ServerPlayer player =
                    server.getPlayerList()
                            .getPlayer(playerEntry.getKey());

            if (player == null) {
                playerIterator.remove();
                continue;
            }

            long currentTime =
                    player.level().getGameTime();

            Map<UUID, MomentumData> targets =
                    playerEntry.getValue();

            Iterator<Map.Entry<UUID, MomentumData>> targetIterator =
                    targets.entrySet().iterator();

            while (targetIterator.hasNext()) {

                Map.Entry<UUID, MomentumData> targetEntry =
                        targetIterator.next();

                MomentumData data =
                        targetEntry.getValue();

                long elapsed =
                        currentTime - data.lastHit;

                if (elapsed < MOMENTUM_DECAY_TIME) {
                    continue;
                }

                int stagesLost =
                        (int) (
                                elapsed /
                                        MOMENTUM_DECAY_TIME
                        );

                data.hits =
                        Math.max(
                                data.hits - stagesLost,
                                0
                        );

                data.lastHit +=
                        stagesLost *
                                MOMENTUM_DECAY_TIME;

                LivingEntity target =
                        findTarget(
                                player,
                                targetEntry.getKey()
                        );

                if (data.hits <= 0) {

                    if (target != null) {
                        sendMomentum(
                                player,
                                target,
                                0
                        );
                    }

                    targetIterator.remove();
                    continue;
                }

                if (target != null) {
                    sendMomentum(
                            player,
                            target,
                            data.hits
                    );
                }
            }

            if (targets.isEmpty()) {
                playerIterator.remove();
            }
        }
    }

    private static LivingEntity findTarget(
            ServerPlayer player,
            UUID targetId
    ) {

        for (LivingEntity entity :
                player.level()
                        .getEntitiesOfClass(
                                LivingEntity.class,
                                player.getBoundingBox()
                                        .inflate(128)
                        )) {

            if (entity.getUUID().equals(targetId)) {
                return entity;
            }
        }

        return null;
    }

    private static void sendMomentum(
            ServerPlayer player,
            LivingEntity target,
            int momentumValue
    ) {

        ServerPlayNetworking.send(
                player,
                new JarngreiprMomentumPayloadS2C(
                        target.getId(),
                        momentumValue
                )
        );
    }

    private static class MomentumData {

        private int hits = 0;
        private long lastHit = Long.MIN_VALUE;
    }
}