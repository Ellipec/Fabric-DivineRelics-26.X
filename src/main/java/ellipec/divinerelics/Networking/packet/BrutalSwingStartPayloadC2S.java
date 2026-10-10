
package ellipec.divinerelics.Networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record BrutalSwingStartPayloadC2S() implements CustomPacketPayload {

    public static final Type<BrutalSwingStartPayloadC2S> TYPE =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            "divinerelics",
                            "brutal_swing_start"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, BrutalSwingStartPayloadC2S> STREAM_CODEC =
            StreamCodec.unit(new BrutalSwingStartPayloadC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
