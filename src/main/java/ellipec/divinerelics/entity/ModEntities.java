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


    private static final ResourceKey<EntityType<?>> DIVINE_SLASH_KEY =
            ResourceKey.create(
                    Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "divine_slash"
                    )
            );

    public static final EntityType<DivineSlashEntity> DIVINE_SLASH =
            EntityType.Builder.<DivineSlashEntity>of(
                            DivineSlashEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0.0001f, 0.0001f)
                    .build(DIVINE_SLASH_KEY);


    private static final ResourceKey<EntityType<?>> DIVINE_SLASH_HITBOX_KEY =
            ResourceKey.create(
                    Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "divine_slash_hitbox"
                    )
            );

    public static final EntityType<DivineSlashHitboxEntity> DIVINE_SLASH_HITBOX =
            EntityType.Builder.<DivineSlashHitboxEntity>of(
                            DivineSlashHitboxEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0.25f, 0.25f)
                    .build(DIVINE_SLASH_HITBOX_KEY);


    public static void register() {

        Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                DRAGON_SCALE_KEY,
                DRAGON_SCALE
        );

        Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                DIVINE_SLASH_KEY,
                DIVINE_SLASH
        );

        Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                DIVINE_SLASH_HITBOX_KEY,
                DIVINE_SLASH_HITBOX
        );
    }
}