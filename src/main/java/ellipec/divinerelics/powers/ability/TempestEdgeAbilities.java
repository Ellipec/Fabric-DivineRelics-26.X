package ellipec.divinerelics.powers.ability;

import ellipec.divinerelics.entity.DivineSlashEntity;
import ellipec.divinerelics.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TempestEdgeAbilities {

    public static final int GALE_DASH_COOLDOWN = 200;
    public static final int DIVINE_SLASH_COOLDOWN = 300;

    private static final Map<UUID, Long> galeDashCooldowns = new HashMap<>();
    private static final Map<UUID, Long> divineSlashCooldowns = new HashMap<>();

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

    public static void divineSlash(ServerPlayer player) {
        if (!player.getMainHandItem().is(ModItems.TEMPEST_EDGE)) {
            return;
        }

        long currentTime = player.level().getGameTime();

        if (divineSlashCooldowns.getOrDefault(player.getUUID(), 0L) > currentTime) {
            return;
        }

        Vec3 look = player.getLookAngle();

        DivineSlashEntity divineSlash = new DivineSlashEntity(
                ellipec.divinerelics.entity.ModEntities.DIVINE_SLASH,
                player.level()
        );

        divineSlash.setOwner(player);

        float yaw = (float) (
                Math.atan2(-look.x, look.z) * (180.0 / Math.PI)
        );

        float pitch = (float) (
                Math.atan2(
                        -look.y,
                        Math.sqrt(look.x * look.x + look.z * look.z)
                ) * (180.0 / Math.PI)
        );

        divineSlash.setYRot(yaw);
        divineSlash.setXRot(pitch);

        divineSlash.setPos(
                player.getX() + look.x * 1.5,
                player.getY() + 1.0 + look.y * 1.5,
                player.getZ() + look.z * 1.5
        );

        divineSlash.setDeltaMovement(
                look.x * 1.25,
                look.y * 1.25,
                look.z * 1.25
        );

        divineSlash.createHitboxes();

        player.level().addFreshEntity(divineSlash);

        divineSlashCooldowns.put(
                player.getUUID(),
                currentTime + DIVINE_SLASH_COOLDOWN
        );
    }
}