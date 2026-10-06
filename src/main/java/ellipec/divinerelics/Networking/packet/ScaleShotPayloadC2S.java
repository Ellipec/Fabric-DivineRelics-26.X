package ellipec.divinerelics.Networking.packet;

import ellipec.divinerelics.DivineRelics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ScaleShotPayloadC2S() implements CustomPacketPayload {

    public static final Type<ScaleShotPayloadC2S> TYPE =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "scale_shot"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, ScaleShotPayloadC2S> STREAM_CODEC =
            StreamCodec.unit(new ScaleShotPayloadC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}