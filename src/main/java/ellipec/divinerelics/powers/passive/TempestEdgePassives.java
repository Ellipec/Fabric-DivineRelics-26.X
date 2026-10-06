package ellipec.divinerelics.powers.passive;

import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class TempestEdgePassives {

    public static void register() {

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(
                (entity, source, amount) -> {

                    if (!(entity instanceof ServerPlayer player)) {
                        return true;
                    }

                    if (!player.getInventory().contains(
                            ModItems.TEMPEST_EDGE.getDefaultInstance())) {
                        return true;
                    }

                    if (source.is(DamageTypeTags.IS_FALL)) {
                        return false;
                    }

                    return true;
                }
        );
    }

    public static void applyPassives(ServerPlayer player) {

        if (player.getInventory().contains(
                ModItems.TEMPEST_EDGE.getDefaultInstance())) {

            // Speed II
            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.SPEED,
                            40,
                            1,
                            false,
                            false,
                            false
                    )
            );
        }
    }
}