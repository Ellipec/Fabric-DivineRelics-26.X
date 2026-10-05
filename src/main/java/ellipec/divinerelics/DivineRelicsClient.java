package ellipec.divinerelics;

import com.microsoft.aad.msal4j.IClientAssertion;
import ellipec.divinerelics.keymapping.ModKeyMappings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class DivineRelicsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeyMappings.register();

        ClientTickEvents.END_CLIENT_TICK.register(DivineRelicsClient::onEndTick);
    }

    public static void onEndTick(Minecraft client) {
        while(ModKeyMappings.ELLIPEC_KEYMAPPING.consumeClick()) {
            client.player.sendSystemMessage(Component.literal("Test"));
        }
    }
}
