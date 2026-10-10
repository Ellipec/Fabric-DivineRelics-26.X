package ellipec.divinerelics.mixin;

import ellipec.divinerelics.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CrafterBlock.class)
public class LegendaryCrafterMixin {

    @Inject(
            method = "dispenseFrom",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventLegendaryCrafterCrafting(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            CallbackInfo ci
    ) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof CrafterBlockEntity crafter)) {
            return;
        }

        CraftingInput input = crafter.asCraftInput();

        Optional<RecipeHolder<CraftingRecipe>> recipe =
                CrafterBlock.getPotentialResults(level, input);

        if (recipe.isEmpty()) {
            return;
        }

        ItemStack result = recipe.get().value().assemble(input);

        if (isLegendary(result)) {
            ci.cancel();
        }
    }

    private static boolean isLegendary(ItemStack stack) {
        return stack.is(ModItems.EMBERFANG)
                || stack.is(ModItems.TEMPEST_EDGE)
                || stack.is(ModItems.FROSTMOURNE)
                || stack.is(ModItems.JARNGREIPR)
                || stack.is(ModItems.DRAGONS_RUIN)
                || stack.is(ModItems.HELM_OF_THE_ANCIENTS)
                || stack.is(ModItems.SOUL_OF_AEGIS)
                || stack.is(ModItems.AEGIS_RESOLVE)
                || stack.is(ModItems.AEGIS_VALOR);
    }
}