package ellipec.divinerelics.Networking;

import ellipec.divinerelics.Networking.packet.GaleDashPayloadC2S;
import ellipec.divinerelics.ability.TempestEdgeAbilities;
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
    }
}