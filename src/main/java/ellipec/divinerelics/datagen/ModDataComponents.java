package ellipec.divinerelics.datagen;

import ellipec.divinerelics.DivineRelics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DataComponentType<BlockPos> DeathProtection = register("deathprotection",
            builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));

private static <T>DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
    return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name),
            builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerDataComponents() {
        DivineRelics.LOGGER.info("Registering data component types for " + DivineRelics.MOD_ID);

    }
}
