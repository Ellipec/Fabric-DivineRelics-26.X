package ellipec.divinerelics.entity;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;

public class GustModel extends GeoModel<GustEntity> {

    private final Identifier model =
            Identifier.fromNamespaceAndPath("divinerelics", "gust_slash");

    private final Identifier texture =
            Identifier.fromNamespaceAndPath("divinerelics", "textures/entity/gust_slash.png");

    private final Identifier animation =
            Identifier.fromNamespaceAndPath("divinerelics", "gust_slash");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(GustEntity animatable) {
        return this.animation;
    }
}