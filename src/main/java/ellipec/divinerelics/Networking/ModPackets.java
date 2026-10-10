package ellipec.divinerelics.Networking;

import ellipec.divinerelics.Networking.packet.*;
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

        PayloadTypeRegistry.serverboundPlay().register(
                DivineSlashPayloadC2S.TYPE,
                DivineSlashPayloadC2S.STREAM_CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                OverheadPayloadC2S.TYPE,
                OverheadPayloadC2S.STREAM_CODEC
        );

        // Brutal Swing packets
        PayloadTypeRegistry.serverboundPlay().register(
                BrutalSwingStartPayloadC2S.TYPE,
                BrutalSwingStartPayloadC2S.STREAM_CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                BrutalSwingReleasePayloadC2S.TYPE,
                BrutalSwingReleasePayloadC2S.STREAM_CODEC
        );
    }
}