package ellipec.divinerelics.Networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record OverheadPayloadC2S() implements CustomPacketPayload {

    public static final Type<OverheadPayloadC2S> TYPE =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            "divinerelics",
                            "overhead"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, OverheadPayloadC2S> STREAM_CODEC =
            StreamCodec.unit(new OverheadPayloadC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}