package ellipec.divinerelics.entity;

import ellipec.divinerelics.DivineRelics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    private static final ResourceKey<EntityType<?>> DRAGON_SCALE_KEY =
            ResourceKey.create(
                    Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "dragon_scale"
                    )
            );

    public static final EntityType<DragonScaleEntity> DRAGON_SCALE =
            EntityType.Builder.<DragonScaleEntity>of(
                            DragonScaleEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0.25f, 0.25f)
                    .build(DRAGON_SCALE_KEY);


    private static final ResourceKey<EntityType<?>> GUST_KEY =
            ResourceKey.create(
                    Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "gust"
                    )
            );

    public static final EntityType<GustEntity> GUST =
            EntityType.Builder.<GustEntity>of(
                            GustEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0.0001f, 0.0001f)
                    .build(GUST_KEY);


    private static final ResourceKey<EntityType<?>> GUST_HITBOX_KEY =
            ResourceKey.create(
                    Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "gust_hitbox"
                    )
            );

    public static final EntityType<GustHitboxEntity> GUST_HITBOX =
            EntityType.Builder.<GustHitboxEntity>of(
                            GustHitboxEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0.25f, 0.25f)
                    .build(GUST_HITBOX_KEY);


    public static void register() {

        Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                DRAGON_SCALE_KEY,
                DRAGON_SCALE
        );

        Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                GUST_KEY,
                GUST
        );

        Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                GUST_HITBOX_KEY,
                GUST_HITBOX
        );
    }
}