package ellipec.divinerelics.mixin;

import ellipec.divinerelics.crafting.LegendaryCraftingData;
import ellipec.divinerelics.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public class LegendaryCraftingMixin {

    @Inject(
            method = "onTake",
            at = @At("TAIL")
    )
    private void registerLegendaryCraft(
            Player player,
            ItemStack stack,
            CallbackInfo ci
    ) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        String itemId =
                getLegendaryId(stack);

        if (itemId == null) {
            return;
        }

        ServerLevel level =
                serverPlayer.level();

        LegendaryCraftingData data =
                LegendaryCraftingData.get(
                        level.getServer()
                );

        if (data.hasBeenCrafted(itemId)) {
            return;
        }

        data.markCrafted(itemId);

        announceCraft(
                serverPlayer,
                stack
        );
    }

    private static void announceCraft(
            ServerPlayer player,
            ItemStack stack
    ) {
        ServerLevel level =
                player.level();

        String dimension =
                getDimensionName(level);

        Component message =
                Component.literal("BEWARE! ")
                        .withStyle(style -> style
                                .withColor(ChatFormatting.DARK_RED)
                                .withBold(true))
                        .append(
                                Component.literal(
                                        player.getName().getString()
                                ).withStyle(style -> style
                                        .withColor(ChatFormatting.YELLOW)
                                        .withBold(true))
                        )
                        .append(
                                Component.literal(
                                        " HAS CRAFTED "
                                )
                        )
                        .append(
                                Component.literal(
                                        "THE "
                                                + stack.getHoverName()
                                                .getString()
                                                .toUpperCase()
                                ).withStyle(style -> style
                                        .withColor(ChatFormatting.GOLD)
                                        .withBold(true))
                        )
                        .append(
                                Component.literal(
                                        " AT ["
                                                + player.getBlockX()
                                                + ", "
                                                + player.getBlockY()
                                                + ", "
                                                + player.getBlockZ()
                                                + "] IN THE "
                                                + dimension
                                                + "!"
                                )
                        );

        level.getServer()
                .getPlayerList()
                .broadcastSystemMessage(
                        message,
                        false
                );
    }

    private static String getDimensionName(
            ServerLevel level
    ) {
        if (level.dimension() == ServerLevel.OVERWORLD) {
            return "OVERWORLD";
        }

        if (level.dimension() == ServerLevel.NETHER) {
            return "NETHER";
        }

        if (level.dimension() == ServerLevel.END) {
            return "END";
        }

        return level.dimension()
                .toString()
                .toUpperCase();
    }

    private static String getLegendaryId(
            ItemStack stack
    ) {
        if (stack.is(ModItems.EMBERFANG)) {
            return "emberfang";
        }

        if (stack.is(ModItems.TEMPEST_EDGE)) {
            return "tempest_edge";
        }

        if (stack.is(ModItems.FROSTMOURNE)) {
            return "frostmourne";
        }

        if (stack.is(ModItems.JARNGREIPR)) {
            return "jarngreipr.json";
        }

        if (stack.is(ModItems.DRAGONS_RUIN)) {
            return "dragons_ruin";
        }

        if (stack.is(ModItems.HELM_OF_THE_ANCIENTS)) {
            return "helm_of_the_ancients";
        }

        if (stack.is(ModItems.SOUL_OF_AEGIS)) {
            return "soul_of_aegis";
        }

        if (stack.is(ModItems.AEGIS_RESOLVE)) {
            return "aegis_resolve";
        }

        if (stack.is(ModItems.AEGIS_VALOR)) {
            return "aegis_valor";
        }

        return null;
    }
}