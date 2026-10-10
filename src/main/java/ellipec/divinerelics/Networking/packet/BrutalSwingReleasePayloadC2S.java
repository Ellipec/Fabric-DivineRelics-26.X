
package ellipec.divinerelics.Networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record BrutalSwingReleasePayloadC2S() implements CustomPacketPayload {

    public static final Type<BrutalSwingReleasePayloadC2S> TYPE =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            "divinerelics",
                            "brutal_swing_release"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, BrutalSwingReleasePayloadC2S> STREAM_CODEC =
            StreamCodec.unit(new BrutalSwingReleasePayloadC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
