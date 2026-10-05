package ellipec.divinerelics.ability;

import ellipec.divinerelics.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TempestEdgeAbilities {

    public static final int GALE_DASH_COOLDOWN = 200;

    private static final Map<UUID, Long> galeDashCooldowns = new HashMap<>();

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
}