package ellipec.divinerelics.Networking.packet;

import ellipec.divinerelics.DivineRelics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DragonStepPayloadC2S() implements CustomPacketPayload {

    public static final Type<DragonStepPayloadC2S> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(
                    DivineRelics.MOD_ID,
                    "dragon_step"
            )
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DragonStepPayloadC2S> STREAM_CODEC =
            StreamCodec.unit(new DragonStepPayloadC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}