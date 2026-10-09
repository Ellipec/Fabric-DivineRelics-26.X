package ellipec.divinerelics.mixin;

import ellipec.divinerelics.crafting.LegendaryCraftingData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class LegendaryCraftingPreventMixin {

    @Inject(
            method = "mayPickup",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventLegendaryCraft(
            Player player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!((Object) this instanceof ResultSlot)) {
            return;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        ItemStack stack =
                ((Slot) (Object) this).getItem();

        String itemId =
                getLegendaryId(stack);

        if (itemId == null) {
            return;
        }

        LegendaryCraftingData data =
                LegendaryCraftingData.get(
                        serverPlayer.level().getServer()
                );

        if (data.hasBeenCrafted(itemId)) {
            cir.setReturnValue(false);
        }
    }

    private static String getLegendaryId(ItemStack stack) {
        if (stack.is(ellipec.divinerelics.item.ModItems.EMBERFANG)) {
            return "emberfang";
        }

        if (stack.is(ellipec.divinerelics.item.ModItems.TEMPEST_EDGE)) {
            return "tempest_edge";
        }

        if (stack.is(ellipec.divinerelics.item.ModItems.FROSTMOURNE)) {
            return "frostmourne";
        }

        if (stack.is(ellipec.divinerelics.item.ModItems.JARNGREIPR)) {
            return "jarngreipr.json";
        }

        if (stack.is(ellipec.divinerelics.item.ModItems.DRAGONS_RUIN)) {
            return "dragons_ruin";
        }

        if (stack.is(ellipec.divinerelics.item.ModItems.HELM_OF_THE_ANCIENTS)) {
            return "helm_of_the_ancients";
        }

        if (stack.is(ellipec.divinerelics.item.ModItems.SOUL_OF_AEGIS)) {
            return "soul_of_aegis";
        }

        if (stack.is(ellipec.divinerelics.item.ModItems.AEGIS_RESOLVE)) {
            return "aegis_resolve";
        }

        if (stack.is(ellipec.divinerelics.item.ModItems.AEGIS_VALOR)) {
            return "aegis_valor";
        }

        return null;
    }
}