package ellipec.divinerelics;

import ellipec.divinerelics.Networking.packet.GaleDashPayloadC2S;
import ellipec.divinerelics.ability.TempestEdgeAbilities;
import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.keymapping.ModKeyMappings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class DivineRelicsClient implements ClientModInitializer {

    private static int galeDashCooldown = 0;

    @Override
    public void onInitializeClient() {
        ModKeyMappings.register();

        ClientTickEvents.END_CLIENT_TICK.register(DivineRelicsClient::onEndTick);

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(
                        DivineRelics.MOD_ID,
                        "gale_dash_cooldown"
                ),
                DivineRelicsClient::renderGaleDashCooldown
        );
    }

    public static void onEndTick(Minecraft client) {
        while (ModKeyMappings.PRIMARY_ABILITY.consumeClick()) {

            if (galeDashCooldown <= 0
                    && client.player != null
                    && client.player.getMainHandItem().is(ModItems.TEMPEST_EDGE)) {
                ClientPlayNetworking.send(new GaleDashPayloadC2S());

                galeDashCooldown = TempestEdgeAbilities.GALE_DASH_COOLDOWN;
            }
        }

        if (galeDashCooldown > 0) {
            galeDashCooldown--;
        }
    }

    private static void renderGaleDashCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        if (galeDashCooldown <= 0) {
            return;
        }

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int barWidth = 80;
        int barHeight = 5;

        int x = (screenWidth - barWidth) / 2;
        int y = screenHeight - 45;

        // Background
        graphics.fill(
                x,
                y,
                x + barWidth,
                y + barHeight,
                0xAA000000
        );

        // Cooldown bar
        int fillWidth = (barWidth - 2) * galeDashCooldown / TempestEdgeAbilities.GALE_DASH_COOLDOWN;

        graphics.fill(
                x + 1,
                y + 1,
                x + 1 + fillWidth,
                y + barHeight - 1,
                0xFFFFFFFF
        );
    }
}