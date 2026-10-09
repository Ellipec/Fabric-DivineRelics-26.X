package ellipec.divinerelics.item;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class JarngreiprItem extends Item implements GeoItem {

    private static final RawAnimation OVERHEAD_ANIMATION =
            RawAnimation.begin()
                    .thenPlay("overhead");

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    public JarngreiprItem(Properties properties) {
        super(properties);

        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(
            Consumer<GeoRenderProvider> consumer
    ) {
        System.out.println(
                "JARNGREIPR createGeoRenderer CALLED"
        );

        consumer.accept(new GeoRenderProvider() {

            private JarngreiprRenderer renderer;

            @Override
            public JarngreiprRenderer getGeoItemRenderer() {

                if (renderer == null) {

                    System.out.println(
                            "JARNGREIPR GECKOLIB RENDERER CREATED"
                    );

                    renderer = new JarngreiprRenderer();
                }

                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers
    ) {
        controllers.add(
                new AnimationController<>(
                        "overhead_controller",
                        state -> PlayState.STOP
                ).triggerableAnim(
                        "overhead",
                        OVERHEAD_ANIMATION
                )
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}