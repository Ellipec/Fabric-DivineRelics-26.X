package ellipec.divinerelics;

import ellipec.divinerelics.Networking.packet.DragonStepPayloadC2S;
import ellipec.divinerelics.Networking.packet.GaleDashPayloadC2S;
import ellipec.divinerelics.Networking.packet.ScaleShotPayloadC2S;
import ellipec.divinerelics.entity.DragonScaleRenderer;
import ellipec.divinerelics.entity.ModEntities;
import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.keymapping.ModKeyMappings;
import ellipec.divinerelics.powers.ability.DragonRuinAbilities;
import ellipec.divinerelics.powers.ability.TempestEdgeAbilities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class DivineRelicsClient implements ClientModInitializer {

    private static int galeDashCooldown = 0;
    private static int dragonStepCooldown = 0;
    private static int scaleShotCooldown = 0;

    private static final List<String> cooldownOrder = new ArrayList<>();

    @Override
    public void onInitializeClient() {

        ModKeyMappings.register();

        ClientTickEvents.END_CLIENT_TICK.register(
                DivineRelicsClient::onEndTick
        );

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(
                        DivineRelics.MOD_ID,
                        "gale_dash_cooldown"
                ),
                DivineRelicsClient::renderGaleDashCooldown
        );

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(
                        DivineRelics.MOD_ID,
                        "dragon_step_cooldown"
                ),
                DivineRelicsClient::renderDragonStepCooldown
        );

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(
                        DivineRelics.MOD_ID,
                        "scale_shot_cooldown"
                ),
                DivineRelicsClient::renderScaleShotCooldown
        );

        EntityRenderers.register(
                ModEntities.DRAGON_SCALE,
                DragonScaleRenderer::new
        );
    }

    public static void onEndTick(Minecraft client) {

        while (ModKeyMappings.PRIMARY_ABILITY.consumeClick()) {

            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.TEMPEST_EDGE)
                    && galeDashCooldown <= 0) {

                ClientPlayNetworking.send(
                        new GaleDashPayloadC2S()
                );

                galeDashCooldown =
                        TempestEdgeAbilities.GALE_DASH_COOLDOWN;

                cooldownOrder.remove("gale_dash");
                cooldownOrder.add("gale_dash");
            }

            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.DRAGONS_RUIN)
                    && dragonStepCooldown <= 0) {

                ClientPlayNetworking.send(
                        new DragonStepPayloadC2S()
                );

                dragonStepCooldown =
                        DragonRuinAbilities.DRAGONSTEP_COOLDOWN;

                cooldownOrder.remove("dragon_step");
                cooldownOrder.add("dragon_step");
            }
        }

        while (ModKeyMappings.SECONDARY_ABILITY.consumeClick()) {

            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.DRAGONS_RUIN)
                    && scaleShotCooldown <= 0) {

                ClientPlayNetworking.send(
                        new ScaleShotPayloadC2S()
                );

                scaleShotCooldown =
                        DragonRuinAbilities.SCALE_SHOT_COOLDOWN;

                cooldownOrder.remove("scale_shot");
                cooldownOrder.add("scale_shot");
            }
        }

        if (galeDashCooldown > 0) {
            galeDashCooldown--;
        }

        if (dragonStepCooldown > 0) {
            dragonStepCooldown--;
        }

        if (scaleShotCooldown > 0) {
            scaleShotCooldown--;
        }

        if (galeDashCooldown <= 0) {
            cooldownOrder.remove("gale_dash");
        }

        if (dragonStepCooldown <= 0) {
            cooldownOrder.remove("dragon_step");
        }

        if (scaleShotCooldown <= 0) {
            cooldownOrder.remove("scale_shot");
        }
    }

    private static void renderGaleDashCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {

        if (galeDashCooldown <= 0) {
            return;
        }

        int position =
                cooldownOrder.indexOf("gale_dash");

        if (position == -1) {
            return;
        }

        renderCooldownBar(
                graphics,
                galeDashCooldown,
                TempestEdgeAbilities.GALE_DASH_COOLDOWN,
                position,
                0xFF55FFFF,
                Items.WIND_CHARGE
        );
    }

    private static void renderDragonStepCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {

        if (dragonStepCooldown <= 0) {
            return;
        }

        int position =
                cooldownOrder.indexOf("dragon_step");

        if (position == -1) {
            return;
        }

        renderCooldownBar(
                graphics,
                dragonStepCooldown,
                DragonRuinAbilities.DRAGONSTEP_COOLDOWN,
                position,
                0xFF7100D7,
                Items.ENDER_PEARL
        );
    }

    private static void renderScaleShotCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {

        if (scaleShotCooldown <= 0) {
            return;
        }

        int position =
                cooldownOrder.indexOf("scale_shot");

        if (position == -1) {
            return;
        }

        renderCooldownBar(
                graphics,
                scaleShotCooldown,
                DragonRuinAbilities.SCALE_SHOT_COOLDOWN,
                position,
                0xFF230032,
                ModItems.DRAGON_SCALE
        );
    }

    private static void renderCooldownBar(
            GuiGraphicsExtractor graphics,
            int cooldown,
            int maxCooldown,
            int position,
            int colour,
            Item icon
    ) {

        int screenWidth =
                Minecraft.getInstance()
                        .getWindow()
                        .getGuiScaledWidth();

        int screenHeight =
                Minecraft.getInstance()
                        .getWindow()
                        .getGuiScaledHeight();

        int barWidth = 80;
        int barHeight = 5;

        // Icon is rendered at approximately 9x9 pixels.
        float iconScale = 0.5625f;

        int iconSize = 9;
        int iconGap = 4;

        int totalWidth =
                iconSize
                        + iconGap
                        + barWidth;

        int startX =
                (screenWidth - totalWidth) / 2;

        int x =
                startX + iconSize + iconGap;

        // Each cooldown is separated by 11 pixels.
        // The whole group starts 2 pixels higher.
        int y =
                screenHeight
                        - 47
                        - (position * 11);

        // Draw the ability icon at approximately 9x9.
        graphics.pose().pushMatrix();

        // Move to the centre of the icon.
        graphics.pose().translate(
                startX + 4.5f,
                y + 2.5f
        );

        // Scale the normal 16x16 item down to 9x9.
        graphics.pose().scale(
                iconScale,
                iconScale
        );

        // Move back so the item stays centred.
        graphics.pose().translate(
                -8.0f,
                -8.0f
        );

        graphics.item(
                new ItemStack(icon),
                0,
                0
        );

        graphics.pose().popMatrix();

        // Background.
        graphics.fill(
                x,
                y,
                x + barWidth,
                y + barHeight,
                0xAA000000
        );

        // Cooldown fill.
        int fillWidth =
                (barWidth - 2)
                        * cooldown
                        / maxCooldown;

        graphics.fill(
                x + 1,
                y + 1,
                x + 1 + fillWidth,
                y + barHeight - 1,
                colour
        );
    }
}