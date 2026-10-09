package ellipec.divinerelics.entity;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;

public class DivineSlashModel extends GeoModel<DivineSlashEntity> {

    private final Identifier model =
            Identifier.fromNamespaceAndPath("divinerelics", "divine_slash");

    private final Identifier texture =
            Identifier.fromNamespaceAndPath(
                    "divinerelics",
                    "textures/entity/divine_slash.png"
            );

    private final Identifier animation =
            Identifier.fromNamespaceAndPath("divinerelics", "divine_slash");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(DivineSlashEntity animatable) {
        return this.animation;
    }
}