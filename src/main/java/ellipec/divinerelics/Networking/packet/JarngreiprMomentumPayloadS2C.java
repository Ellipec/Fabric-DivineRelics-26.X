package ellipec.divinerelics.Networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record JarngreiprMomentumPayloadS2C(
        int entityId,
        int momentum
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<JarngreiprMomentumPayloadS2C> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(
                            "divinerelics",
                            "jarngreipr_momentum"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, JarngreiprMomentumPayloadS2C> CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeVarInt(payload.entityId());
                        buf.writeVarInt(payload.momentum());
                    },
                    buf -> new JarngreiprMomentumPayloadS2C(
                            buf.readVarInt(),
                            buf.readVarInt()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}