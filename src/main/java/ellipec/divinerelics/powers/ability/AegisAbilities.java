package ellipec.divinerelics.powers.ability;

import ellipec.divinerelics.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AegisAbilities {

    public static final int CLEANSING_OF_THE_UNHOLY_COOLDOWN = 600;

    private static final Map<UUID, Long> cleansingCooldowns = new HashMap<>();

    public static void cleansingOfTheUnholy(ServerPlayer player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        // Require the complete Aegis armour set.
        if (!helmet.is(ModItems.HELM_OF_THE_ANCIENTS)
                || !chestplate.is(ModItems.SOUL_OF_AEGIS)
                || !leggings.is(ModItems.AEGIS_RESOLVE)
                || !boots.is(ModItems.AEGIS_VALOR)) {
            return;
        }

        // Prevent the ability from being used while it is on cooldown.
        long currentTime = player.level().getGameTime();

        if (cleansingCooldowns.getOrDefault(player.getUUID(), 0L)
                > currentTime) {
            return;
        }

        // Remove all current status effects.
        player.removeAllEffects();

        // Apply Resistance II and Regeneration II for 5 seconds.
        player.addEffect(
                new MobEffectInstance(MobEffects.RESISTANCE, 100, 1)
        );

        player.addEffect(
                new MobEffectInstance(MobEffects.REGENERATION, 100, 1)
        );

        // Start the 30-second cooldown.
        cleansingCooldowns.put(
                player.getUUID(),
                currentTime + CLEANSING_OF_THE_UNHOLY_COOLDOWN
        );
    }
}