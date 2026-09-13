package ellipec.divinerelics.datagen;

import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.item.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {

                shaped(RecipeCategory.MISC, ModItems.EMBERFANG)
                        .pattern("LBL")
                        .pattern("LVL")
                        .pattern("LSL")
                        .define('L', Items.LAVA_BUCKET)
                        .define('B', Items.BLAZE_ROD)
                        .define('V', ModItems.VOLCANIC_CORE)
                        .define('S', Items.NETHERITE_SWORD)
                        .unlockedBy(getHasName(Items.NETHERITE_SWORD), has(Items.NETHERITE_SWORD))
                        .group("legendary-weapon")
                        .save(output, "emberfang_sword");
            }
        };
    }

    @Override
    public String getName() {
        return "Divine Relics Recipes";
    }
}