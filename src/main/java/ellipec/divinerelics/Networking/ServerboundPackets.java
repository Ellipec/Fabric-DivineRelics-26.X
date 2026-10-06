package ellipec.divinerelics.Networking;

import ellipec.divinerelics.Networking.packet.DragonStepPayloadC2S;
import ellipec.divinerelics.Networking.packet.GaleDashPayloadC2S;
import ellipec.divinerelics.Networking.packet.ScaleShotPayloadC2S;
import ellipec.divinerelics.powers.ability.DragonRuinAbilities;
import ellipec.divinerelics.powers.ability.TempestEdgeAbilities;
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
    }
}