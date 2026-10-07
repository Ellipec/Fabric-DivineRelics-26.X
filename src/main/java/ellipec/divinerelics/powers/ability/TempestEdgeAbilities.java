package ellipec.divinerelics.powers.ability;

import ellipec.divinerelics.entity.GustEntity;
import ellipec.divinerelics.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TempestEdgeAbilities {

    public static final int GALE_DASH_COOLDOWN = 200;
    public static final int GUST_COOLDOWN = 0;

    private static final Map<UUID, Long> galeDashCooldowns = new HashMap<>();
    private static final Map<UUID, Long> gustCooldowns = new HashMap<>();

    public static void galeDash(ServerPlayer player) {
        if (!player.getMainHandItem().is(ModItems.TEMPEST_EDGE)) {
            return;
        }

        long currentTime = player.level().getGameTime();

        if (galeDashCooldowns.getOrDefault(player.getUUID(), 0L) > currentTime) {
            return;
        }

        Vec3 look = player.getLookAngle();

        double dashStrength = 3.2;

        player.setDeltaMovement(
                look.x * dashStrength,
                look.y * dashStrength * 0.4,
                look.z * dashStrength
        );

        player.hurtMarked = true;

        galeDashCooldowns.put(
                player.getUUID(),
                currentTime + GALE_DASH_COOLDOWN
        );
    }

    public static void gust(ServerPlayer player) {
        if (!player.getMainHandItem().is(ModItems.TEMPEST_EDGE)) {
            return;
        }

        long currentTime = player.level().getGameTime();

        if (gustCooldowns.getOrDefault(player.getUUID(), 0L) > currentTime) {
            return;
        }

        Vec3 look = player.getLookAngle();

        GustEntity gust = new GustEntity(
                ellipec.divinerelics.entity.ModEntities.GUST,
                player.level()
        );

        // Remember who fired the Gust.
        gust.setOwner(player);

        // Make the Gust face the same direction as the player's look direction.
        float yaw = (float) (
                Math.atan2(-look.x, look.z) * (180.0 / Math.PI)
        );

        float pitch = (float) (
                Math.atan2(
                        -look.y,
                        Math.sqrt(look.x * look.x + look.z * look.z)
                ) * (180.0 / Math.PI)
        );

        gust.setYRot(yaw);
        gust.setXRot(pitch);

        gust.setPos(
                player.getX() + look.x * 1.5,
                player.getY() + 1.0 + look.y * 1.5,
                player.getZ() + look.z * 1.5
        );

        gust.setDeltaMovement(
                look.x * 1.25,
                look.y * 1.25,
                look.z * 1.25
        );

        // Create the hitboxes immediately.
        gust.createHitboxes();

        player.level().addFreshEntity(gust);

        gustCooldowns.put(
                player.getUUID(),
                currentTime + GUST_COOLDOWN
        );
    }
}