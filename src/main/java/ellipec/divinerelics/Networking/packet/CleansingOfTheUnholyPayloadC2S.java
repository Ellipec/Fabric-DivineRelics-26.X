
package ellipec.divinerelics.Networking.packet;

import ellipec.divinerelics.DivineRelics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CleansingOfTheUnholyPayloadC2S() implements CustomPacketPayload {

    public static final Type<CleansingOfTheUnholyPayloadC2S> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, "cleansing_of_the_unholy"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CleansingOfTheUnholyPayloadC2S> STREAM_CODEC =
            StreamCodec.unit(new CleansingOfTheUnholyPayloadC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
