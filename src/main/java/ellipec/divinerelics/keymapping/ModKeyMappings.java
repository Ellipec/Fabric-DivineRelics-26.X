package ellipec.divinerelics.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import ellipec.divinerelics.DivineRelics;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final KeyMapping ELLIPEC_KEYMAPPING = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.divinerelics.ellipec.key",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_C, KeyMapping.Category.MISC));

    public static void register(){
        DivineRelics.LOGGER.info("Registering ModKeyMappings for " + DivineRelics.MOD_ID);
    }
}
