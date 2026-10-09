package ellipec.divinerelics.item;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;

public class JarngreiprModel extends GeoModel<JarngreiprItem> {

    private final Identifier model =
            Identifier.fromNamespaceAndPath(
                    "divinerelics",
                    "item/jarngreipr"
            );

    private final Identifier texture =
            Identifier.fromNamespaceAndPath(
                    "divinerelics",
                    "textures/item/jarngreipr_3d.png"
            );

    private final Identifier animation =
            Identifier.fromNamespaceAndPath(
                    "divinerelics",
                    "item/overhead"
            );

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(JarngreiprItem animatable) {
        return this.animation;
    }
}