package ellipec.divinerelics.Networking.packet;

import ellipec.divinerelics.DivineRelics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record GustPayloadC2S() implements CustomPacketPayload {

    public static final Type<GustPayloadC2S> TYPE =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "gust"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, GustPayloadC2S> STREAM_CODEC =
            StreamCodec.unit(new GustPayloadC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}