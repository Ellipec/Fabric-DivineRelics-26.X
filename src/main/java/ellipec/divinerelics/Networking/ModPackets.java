package ellipec.divinerelics.Networking;

import ellipec.divinerelics.Networking.packet.DragonStepPayloadC2S;
import ellipec.divinerelics.Networking.packet.GaleDashPayloadC2S;
import ellipec.divinerelics.Networking.packet.ScaleShotPayloadC2S;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModPackets {

    public static void registerC2SPackets() {

        PayloadTypeRegistry.serverboundPlay().register(
                GaleDashPayloadC2S.TYPE,
                GaleDashPayloadC2S.STREAM_CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                DragonStepPayloadC2S.TYPE,
                DragonStepPayloadC2S.STREAM_CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                ScaleShotPayloadC2S.TYPE,
                ScaleShotPayloadC2S.STREAM_CODEC
        );
    }
}