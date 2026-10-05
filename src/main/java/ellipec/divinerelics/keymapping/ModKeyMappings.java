package ellipec.divinerelics.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {

    public static final KeyMapping.Category DIVINE_RELICS_CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(
                            "divinerelics",
                            "abilities"
                    )
            );

    public static final KeyMapping PRIMARY_ABILITY =
            KeyMappingHelper.registerKeyMapping(
                    new KeyMapping(
                            "key.divinerelics.primary_ability",
                            InputConstants.Type.KEYSYM,
                            GLFW.GLFW_KEY_C,
                            DIVINE_RELICS_CATEGORY
                    )
            );

    public static final KeyMapping SECONDARY_ABILITY =
            KeyMappingHelper.registerKeyMapping(
                    new KeyMapping(
                            "key.divinerelics.secondary_ability",
                            InputConstants.Type.KEYSYM,
                            GLFW.GLFW_KEY_V,
                            DIVINE_RELICS_CATEGORY
                    )
            );

    public static final KeyMapping ULTIMATE_ABILITY =
            KeyMappingHelper.registerKeyMapping(
                    new KeyMapping(
                            "key.divinerelics.ultimate_ability",
                            InputConstants.Type.KEYSYM,
                            GLFW.GLFW_KEY_X,
                            DIVINE_RELICS_CATEGORY
                    )
            );

    public static void register() {
    }
}