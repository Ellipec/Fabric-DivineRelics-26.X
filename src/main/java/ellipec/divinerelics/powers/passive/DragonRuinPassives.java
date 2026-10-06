package ellipec.divinerelics.powers.passive;

import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;

public class DragonRuinPassives {

    public static void register() {

        // Heal 10% of damage dealt
        ServerLivingEntityEvents.AFTER_DAMAGE.register(
                (entity, source, baseDamageTaken, damageTaken, blocked) -> {

                    if (!(source.getEntity() instanceof ServerPlayer player)) {
                        return;
                    }

                    if (!player.getMainHandItem().is(ModItems.DRAGONS_RUIN)) {
                        return;
                    }

                    float healing = damageTaken * 0.10f;

                    player.heal(healing);
                }
        );

        // Gain 3 hearts when killing an entity
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(
                (world, player, killedEntity, damageSource) -> {

                    if (!(player instanceof ServerPlayer serverPlayer)) {
                        return;
                    }

                    if (!serverPlayer.getMainHandItem().is(ModItems.DRAGONS_RUIN)) {
                        return;
                    }

                    serverPlayer.heal(6.0f);
                }
        );
    }
}