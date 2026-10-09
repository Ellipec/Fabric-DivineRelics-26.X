package ellipec.divinerelics.Networking.packet;

import ellipec.divinerelics.DivineRelics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DivineSlashPayloadC2S() implements CustomPacketPayload {

    public static final Type<DivineSlashPayloadC2S> TYPE =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "divine_slash"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, DivineSlashPayloadC2S> STREAM_CODEC =
            StreamCodec.unit(new DivineSlashPayloadC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}