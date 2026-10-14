
package ellipec.divinerelics;

import ellipec.divinerelics.Networking.packet.*;
import ellipec.divinerelics.client.JarngreiprClientAnimation;
import ellipec.divinerelics.entity.DragonScaleRenderer;
import ellipec.divinerelics.entity.DivineSlashHitboxRenderer;
import ellipec.divinerelics.entity.DivineSlashRenderer;
import ellipec.divinerelics.entity.ModEntities;
import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.keymapping.ModKeyMappings;
import ellipec.divinerelics.powers.ability.AegisAbilities;
import ellipec.divinerelics.powers.ability.DragonRuinAbilities;
import ellipec.divinerelics.powers.ability.JarngreiprAbilities;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DivineRelicsClient implements ClientModInitializer {

    // TEMPEST EDGE ABILITIES
    private static int galeDashCooldown = 0;
    private static int divineSlashCooldown = 0;

    // DRAGON'S RUIN ABILITIES
    private static int dragonStepCooldown = 0;
    private static int scaleShotCooldown = 0;

    // JARNGREIPR ABILITIES
    private static int overheadCooldown = 0;

    private static boolean brutalSwingKeyWasDown = false;
    private static int brutalSwingChargeTicks = 0;
    private static boolean brutalSwingCharging = false;

    // AEGIS ABILITIES
    private static int cleansingCooldown = 0;

    private static final List<String> cooldownOrder = new ArrayList<>();

    private static final Map<Integer, Integer> jarngreiprMomentum =
            new HashMap<>();

    private static final Identifier DIVINE_SLASH_ICON =
            Identifier.fromNamespaceAndPath(
                    DivineRelics.MOD_ID,
                    "textures/entity/divine_slash.png"
            );

    public static boolean isBrutalSwingCharging() {
        return brutalSwingCharging;
    }

    @Override
    public void onInitializeClient() {

        ModKeyMappings.register();

        ClientTickEvents.END_CLIENT_TICK.register(
                DivineRelicsClient::onEndTick
        );

        ClientPlayNetworking.registerGlobalReceiver(
                JarngreiprMomentumPayloadS2C.TYPE,
                (payload, context) -> {
                    context.client().execute(() -> {
                        setMomentum(
                                payload.entityId(),
                                payload.momentum()
                        );
                    });
                }
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
                        "divine_slash_cooldown"
                ),
                DivineRelicsClient::renderDivineSlashCooldown
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

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(
                        DivineRelics.MOD_ID,
                        "overhead_cooldown"
                ),
                DivineRelicsClient::renderOverheadCooldown
        );

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(
                        DivineRelics.MOD_ID,
                        "cleansing_of_the_unholy_cooldown"
                ),
                DivineRelicsClient::renderCleansingCooldown
        );

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(
                        DivineRelics.MOD_ID,
                        "brutal_swing_charge"
                ),
                DivineRelicsClient::renderBrutalSwingCharge
        );

        EntityRenderers.register(
                ModEntities.DRAGON_SCALE,
                DragonScaleRenderer::new
        );

        EntityRenderers.register(
                ModEntities.DIVINE_SLASH,
                DivineSlashRenderer::new
        );

        EntityRenderers.register(
                ModEntities.DIVINE_SLASH_HITBOX,
                DivineSlashHitboxRenderer::new
        );
    }

    public static void setMomentum(int entityId, int momentum) {

        if (momentum <= 0) {
            jarngreiprMomentum.remove(entityId);
            return;
        }

        jarngreiprMomentum.put(entityId, momentum);
    }

    public static int getMomentum(int entityId) {
        return jarngreiprMomentum.getOrDefault(entityId, 0);
    }

    public static void onEndTick(Minecraft client) {

        JarngreiprClientAnimation.tick();
        handleBrutalSwingInput(client);

        // PRIMARY WEAPON ABILITIES
        while (ModKeyMappings.PRIMARY_ABILITY.consumeClick()) {

            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.TEMPEST_EDGE)
                    && galeDashCooldown <= 0) {

                ClientPlayNetworking.send(new GaleDashPayloadC2S());

                galeDashCooldown =
                        TempestEdgeAbilities.GALE_DASH_COOLDOWN;

                cooldownOrder.remove("gale_dash");
                cooldownOrder.add("gale_dash");
            }

            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.DRAGONS_RUIN)
                    && dragonStepCooldown <= 0) {

                ClientPlayNetworking.send(new DragonStepPayloadC2S());

                dragonStepCooldown =
                        DragonRuinAbilities.DRAGONSTEP_COOLDOWN;

                cooldownOrder.remove("dragon_step");
                cooldownOrder.add("dragon_step");
            }

            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.JARNGREIPR)
                    && overheadCooldown <= 0) {

                JarngreiprClientAnimation.start();

                ClientPlayNetworking.send(new OverheadPayloadC2S());

                overheadCooldown =
                        JarngreiprAbilities.OVERHEAD_COOLDOWN;

                cooldownOrder.remove("overhead");
                cooldownOrder.add("overhead");
            }
        }

        // SECONDARY WEAPON ABILITIES
        while (ModKeyMappings.SECONDARY_ABILITY.consumeClick()) {

            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.TEMPEST_EDGE)
                    && divineSlashCooldown <= 0) {

                ClientPlayNetworking.send(new DivineSlashPayloadC2S());

                divineSlashCooldown =
                        TempestEdgeAbilities.DIVINE_SLASH_COOLDOWN;

                cooldownOrder.remove("divine_slash");
                cooldownOrder.add("divine_slash");
            }

            if (client.player != null
                    && client.player.getMainHandItem().is(ModItems.DRAGONS_RUIN)
                    && scaleShotCooldown <= 0) {

                ClientPlayNetworking.send(new ScaleShotPayloadC2S());

                scaleShotCooldown =
                        DragonRuinAbilities.SCALE_SHOT_COOLDOWN;

                cooldownOrder.remove("scale_shot");
                cooldownOrder.add("scale_shot");
            }
        }

        // ARMOUR ABILITIES
        while (ModKeyMappings.ARMOUR_PRIMARY_ABILITY.consumeClick()) {

            // AEGIS: CLEANSING OF THE UNHOLY
            if (client.player != null
                    && cleansingCooldown <= 0
                    && client.player.getItemBySlot(EquipmentSlot.HEAD)
                    .is(ModItems.HELM_OF_THE_ANCIENTS)
                    && client.player.getItemBySlot(EquipmentSlot.CHEST)
                    .is(ModItems.SOUL_OF_AEGIS)
                    && client.player.getItemBySlot(EquipmentSlot.LEGS)
                    .is(ModItems.AEGIS_RESOLVE)
                    && client.player.getItemBySlot(EquipmentSlot.FEET)
                    .is(ModItems.AEGIS_VALOR)) {

                ClientPlayNetworking.send(
                        new CleansingOfTheUnholyPayloadC2S()
                );

                cleansingCooldown =
                        AegisAbilities.CLEANSING_OF_THE_UNHOLY_COOLDOWN;

                cooldownOrder.remove("cleansing_of_the_unholy");
                cooldownOrder.add("cleansing_of_the_unholy");
            }
        }

        // COOLDOWN COUNTDOWNS
        if (galeDashCooldown > 0) galeDashCooldown--;
        if (divineSlashCooldown > 0) divineSlashCooldown--;
        if (dragonStepCooldown > 0) dragonStepCooldown--;
        if (scaleShotCooldown > 0) scaleShotCooldown--;
        if (overheadCooldown > 0) overheadCooldown--;
        if (cleansingCooldown > 0) cleansingCooldown--;

        // REMOVE FINISHED COOLDOWNS FROM THE HUD ORDER
        if (galeDashCooldown <= 0) cooldownOrder.remove("gale_dash");
        if (divineSlashCooldown <= 0) cooldownOrder.remove("divine_slash");
        if (dragonStepCooldown <= 0) cooldownOrder.remove("dragon_step");
        if (scaleShotCooldown <= 0) cooldownOrder.remove("scale_shot");
        if (overheadCooldown <= 0) cooldownOrder.remove("overhead");

        if (cleansingCooldown <= 0) {
            cooldownOrder.remove("cleansing_of_the_unholy");
        }
    }

    private static void handleBrutalSwingInput(Minecraft client) {

        boolean holdingWeapon =
                client.player != null
                        && client.player.getMainHandItem()
                        .is(ModItems.JARNGREIPR);

        boolean keyDown =
                holdingWeapon
                        && ModKeyMappings.SECONDARY_ABILITY.isDown();

        if (brutalSwingCharging && client.options != null) {
            client.options.keyJump.setDown(false);
        }

        if (keyDown && !brutalSwingKeyWasDown) {

            ClientPlayNetworking.send(new BrutalSwingStartPayloadC2S());

            brutalSwingCharging = true;
            brutalSwingChargeTicks = 0;

            JarngreiprClientAnimation.startCharging();

            client.options.keyJump.setDown(false);
        }

        if (keyDown && brutalSwingCharging) {

            brutalSwingChargeTicks = Math.min(
                    JarngreiprAbilities.BRUTAL_SWING_MAX_CHARGE,
                    brutalSwingChargeTicks + 1
            );

            float chargeProgress =
                    brutalSwingChargeTicks
                            / (float) JarngreiprAbilities.BRUTAL_SWING_MAX_CHARGE;

            JarngreiprClientAnimation.updateCharge(chargeProgress);

            client.options.keyJump.setDown(false);
        }

        if (!keyDown && brutalSwingKeyWasDown) {

            if (brutalSwingCharging) {

                ClientPlayNetworking.send(
                        new BrutalSwingReleasePayloadC2S()
                );

                JarngreiprClientAnimation.startSwing();

                brutalSwingCharging = false;
                brutalSwingChargeTicks = 0;
            }
        }

        brutalSwingKeyWasDown = keyDown;
    }

    // TEMPEST EDGE: GALE DASH COOLDOWN
    private static void renderGaleDashCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        if (galeDashCooldown <= 0) return;

        int position = cooldownOrder.indexOf("gale_dash");
        if (position == -1) return;

        renderCooldownBar(
                graphics,
                galeDashCooldown,
                TempestEdgeAbilities.GALE_DASH_COOLDOWN,
                position,
                0xFFD9DFED,
                new ItemStack(net.minecraft.world.item.Items.WIND_CHARGE)
        );
    }

    // TEMPEST EDGE: DIVINE SLASH COOLDOWN
    private static void renderDivineSlashCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        if (divineSlashCooldown <= 0) return;

        int position = cooldownOrder.indexOf("divine_slash");
        if (position == -1) return;

        renderTextureCooldownBar(
                graphics,
                divineSlashCooldown,
                TempestEdgeAbilities.DIVINE_SLASH_COOLDOWN,
                position,
                0xFF55FFFF,
                DIVINE_SLASH_ICON
        );
    }

    // DRAGON'S RUIN: DRAGON STEP COOLDOWN
    private static void renderDragonStepCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        if (dragonStepCooldown <= 0) return;

        int position = cooldownOrder.indexOf("dragon_step");
        if (position == -1) return;

        renderCooldownBar(
                graphics,
                dragonStepCooldown,
                DragonRuinAbilities.DRAGONSTEP_COOLDOWN,
                position,
                0xFF7100D7,
                new ItemStack(net.minecraft.world.item.Items.ENDER_PEARL)
        );
    }

    // DRAGON'S RUIN: SCALE SHOT COOLDOWN
    private static void renderScaleShotCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        if (scaleShotCooldown <= 0) return;

        int position = cooldownOrder.indexOf("scale_shot");
        if (position == -1) return;

        renderCooldownBar(
                graphics,
                scaleShotCooldown,
                DragonRuinAbilities.SCALE_SHOT_COOLDOWN,
                position,
                0xFF230032,
                new ItemStack(ModItems.DRAGON_SCALE)
        );
    }

    // JARNGREIPR: OVERHEAD COOLDOWN
    private static void renderOverheadCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        if (overheadCooldown <= 0) return;

        int position = cooldownOrder.indexOf("overhead");
        if (position == -1) return;

        renderCooldownBar(
                graphics,
                overheadCooldown,
                JarngreiprAbilities.OVERHEAD_COOLDOWN,
                position,
                0xFFFF6A00,
                new ItemStack(ModItems.JARNGREIPR)
        );
    }

    // AEGIS: CLEANSING OF THE UNHOLY COOLDOWN
    private static void renderCleansingCooldown(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {
        if (cleansingCooldown <= 0) return;

        int position = cooldownOrder.indexOf("cleansing_of_the_unholy");
        if (position == -1) return;

        renderCooldownBar(
                graphics,
                cleansingCooldown,
                AegisAbilities.CLEANSING_OF_THE_UNHOLY_COOLDOWN,
                position,
                0xFFB09A19,
                new ItemStack(ModItems.HEART_OF_AEGIS)
        );
    }

    // JARNGREIPR: BRUTAL SWING CHARGE BAR
    private static void renderBrutalSwingCharge(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {

        if (!brutalSwingCharging) {
            return;
        }

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
        int iconSize = 9;
        int iconGap = 4;

        int totalWidth = iconSize + iconGap + barWidth;
        int startX = (screenWidth - totalWidth) / 2;
        int x = startX + iconSize + iconGap;

        int position = cooldownOrder.size();
        int y = screenHeight - 47 - (position * 11);

        graphics.pose().pushMatrix();

        graphics.pose().translate(startX + 4.5f, y + 2.5f);
        graphics.pose().scale(0.5625f, 0.5625f);
        graphics.pose().translate(-8.0f, -8.0f);

        graphics.item(new ItemStack(ModItems.JARNGREIPR), 0, 0);

        graphics.pose().popMatrix();

        graphics.fill(
                x,
                y,
                x + barWidth,
                y + barHeight,
                0xAA000000
        );

        float progress = Math.min(
                1.0f,
                brutalSwingChargeTicks
                        / (float) JarngreiprAbilities.BRUTAL_SWING_MAX_CHARGE
        );

        int fillWidth = Math.round((barWidth - 2) * progress);

        int colour = progress >= 1.0f
                ? 0xFFFFD700
                : 0xFF9B59FF;

        graphics.fill(
                x + 1,
                y + 1,
                x + 1 + fillWidth,
                y + barHeight - 1,
                colour
        );
    }

    // SHARED COOLDOWN BAR RENDERER
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

        int totalWidth = iconSize + iconGap + barWidth;
        int startX = (screenWidth - totalWidth) / 2;
        int x = startX + iconSize + iconGap;
        int y = screenHeight - 47 - (position * 11);

        graphics.pose().pushMatrix();

        graphics.pose().translate(startX + 4.5f, y + 2.5f);
        graphics.pose().scale(iconScale, iconScale);
        graphics.pose().translate(-8.0f, -8.0f);

        graphics.item(icon, 0, 0);

        graphics.pose().popMatrix();

        graphics.fill(
                x,
                y,
                x + barWidth,
                y + barHeight,
                0xAA000000
        );

        int fillWidth = (barWidth - 2) * cooldown / maxCooldown;

        graphics.fill(
                x + 1,
                y + 1,
                x + 1 + fillWidth,
                y + barHeight - 1,
                colour
        );
    }

    // SHARED TEXTURE COOLDOWN BAR RENDERER
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

        int totalWidth = iconWidth + iconGap + barWidth;
        int startX = (screenWidth - totalWidth) / 2;
        int x = startX + iconWidth + iconGap;
        int y = screenHeight - 47 - (position * 11);

        graphics.pose().pushMatrix();

        graphics.pose().translate(
                startX + (iconWidth / 2.0f),
                y + 2.5f
        );

        graphics.pose().rotate((float) Math.toRadians(225f));

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

        int fillWidth = (barWidth - 2) * cooldown / maxCooldown;

        graphics.fill(
                x + 1,
                y + 1,
                x + 1 + fillWidth,
                y + barHeight - 1,
                colour
        );
    }
}
