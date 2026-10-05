package ellipec.divinerelics.Networking.packet;

import ellipec.divinerelics.DivineRelics;
import ellipec.divinerelics.DivineRelicsClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record TestPayloadC2S(String name, int value) implements CustomPacketPayload {
    public static final Type<TestPayloadC2S> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, "test_payload"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TestPayloadC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            TestPayloadC2S::name,

            ByteBufCodecs.VAR_INT,
            TestPayloadC2S::value,

            TestPayloadC2S::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
