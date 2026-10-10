package ellipec.divinerelics.Networking;

import ellipec.divinerelics.Networking.packet.*;
import ellipec.divinerelics.powers.ability.DragonRuinAbilities;
import ellipec.divinerelics.powers.ability.TempestEdgeAbilities;
import ellipec.divinerelics.powers.ability.JarngreiprAbilities;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerboundPackets {

    public static void register() {

        ServerPlayNetworking.registerGlobalReceiver(
                GaleDashPayloadC2S.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        TempestEdgeAbilities.galeDash(context.player());
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                DragonStepPayloadC2S.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        DragonRuinAbilities.dragonStep(context.player());
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                ScaleShotPayloadC2S.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        DragonRuinAbilities.scaleShot(context.player());
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                DivineSlashPayloadC2S.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        TempestEdgeAbilities.divineSlash(context.player());
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                OverheadPayloadC2S.TYPE,
                (payload, context) -> {
                    context.server().execute(() -> {
                        JarngreiprAbilities.overhead(context.player());
                    });
                }
        );

        // Start charging Brutal Swing
        ServerPlayNetworking.registerGlobalReceiver(
                BrutalSwingStartPayloadC2S.TYPE,
                (payload, context) -> context.server().execute(() ->
                        JarngreiprAbilities.startBrutalSwingCharge(
                                context.player()
                        )
                )
        );

        // Release Brutal Swing
        ServerPlayNetworking.registerGlobalReceiver(
                BrutalSwingReleasePayloadC2S.TYPE,
                (payload, context) -> context.server().execute(() ->
                        JarngreiprAbilities.releaseBrutalSwing(
                                context.player()
                        )
                )
        );
    }
}