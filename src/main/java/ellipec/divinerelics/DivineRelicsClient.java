package ellipec.divinerelics;

import ellipec.divinerelics.Networking.packet.DragonStepPayloadC2S;
import ellipec.divinerelics.Networking.packet.GaleDashPayloadC2S;
import ellipec.divinerelics.Networking.packet.GustPayloadC2S;
import ellipec.divinerelics.Networking.packet.ScaleShotPayloadC2S;
import ellipec.divinerelics.entity.DragonScaleRenderer;
import ellipec.divinerelics.entity.GustHitboxRenderer;
import ellipec.divinerelics.entity.GustRenderer;
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
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DivineRelicsClient implements ClientModInitializer {

    private static int galeDashCooldown = 0;
    private static int gustCooldown = 0;
    private static int dragonStepCooldown = 0;
    private static int scaleShotCooldown = 0;

    private static final List<String> cooldownOrder = new ArrayList<>();

    private static final Identifier GUST_ICON =
            Identifier.fromNamespaceAndPath(
                    DivineRelics.MOD_ID,
                    "textures/entity/gust_slash.png"
            );

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
                        "gust_cooldown"
                ),
                DivineRelicsClient::renderGustCooldown
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

        EntityRenderers.register(
                ModEntities.GUST,
                GustRenderer::new
        );

        EntityRenderers.register(
                ModEntities.GUST_HITBOX,
                GustHitboxRenderer::new
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

            // Tempest Edge - Gust
            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.TEMPEST_EDGE)
                    && gustCooldown <= 0) {

                ClientPlayNetworking.send(
                        new GustPayloadC2S()
                );

                gustCooldown =
                        TempestEdgeAbilities.GUST_COOLDOWN;

                cooldownOrder.remove("gust");
                cooldownOrder.add("gust");
            }

            // Dragon's Ruin - Scale Shot
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

        if (gustCooldown > 0) {
            gustCooldown--;
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

        if (gustCooldown <= 0) {
            cooldownOrder.remove("gust");
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
                0xFFD9DFED,
                new ItemStack(net.minecraft.world.item.Items.WIND_CHARGE)
        );
    }

    private static void renderGustCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {

        if (gustCooldown <= 0) {
            return;
        }

        int position =
                cooldownOrder.indexOf("gust");

        if (position == -1) {
            return;
        }

        renderTextureCooldownBar(
                graphics,
                gustCooldown,
                TempestEdgeAbilities.GUST_COOLDOWN,
                position,
                0xFF55FFFF,
                GUST_ICON
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
                new ItemStack(net.minecraft.world.item.Items.ENDER_PEARL)
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
                new ItemStack(ModItems.DRAGON_SCALE)
        );
    }

    private static void renderCooldownBar(
            GuiGraphicsExtractor graphics,
            int cooldown,
            int maxCooldown,
            int position,
            int colour,
            ItemStack icon
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

        int y =
                screenHeight
                        - 47
                        - (position * 11);

        graphics.pose().pushMatrix();

        graphics.pose().translate(
                startX + 4.5f,
                y + 2.5f
        );

        graphics.pose().scale(
                iconScale,
                iconScale
        );

        graphics.pose().translate(
                -8.0f,
                -8.0f
        );

        graphics.item(
                icon,
                0,
                0
        );

        graphics.pose().popMatrix();

        graphics.fill(
                x,
                y,
                x + barWidth,
                y + barHeight,
                0xAA000000
        );

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

    private static void renderTextureCooldownBar(
            GuiGraphicsExtractor graphics,
            int cooldown,
            int maxCooldown,
            int position,
            int colour,
            Identifier texture
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

        int iconWidth = 7;
        int iconHeight = 15;
        int iconGap = 4;

        int totalWidth =
                iconWidth
                        + iconGap
                        + barWidth;

        int startX =
                (screenWidth - totalWidth) / 2;

        int x =
                startX + iconWidth + iconGap;

        int y =
                screenHeight
                        - 47
                        - (position * 11);

        graphics.pose().pushMatrix();

        graphics.pose().translate(
                startX + (iconWidth / 2.0f),
                y + 2.5f
        );

        graphics.pose().rotate(
                (float) Math.toRadians(225f)
        );

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                texture,
                -iconWidth / 2,
                -iconHeight / 2,
                51.0f,
                0.0f,
                iconWidth,
                iconHeight,
                -29,
                64,
                128,
                64,
                0xFFFFFFFF
        );

        graphics.pose().popMatrix();

        graphics.fill(
                x,
                y,
                x + barWidth,
                y + barHeight,
                0xAA000000
        );

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