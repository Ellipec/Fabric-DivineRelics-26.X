package ellipec.divinerelics.datagen;

import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
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
                    // LEGENDARY ITEMS
                shaped(RecipeCategory.MISC, ModItems.EMBERFANG)
                        .pattern("LBL")
                        .pattern("PVP")
                        .pattern("LSL")
                        .define('V', ModItems.VOLCANIC_CORE)
                        .define('L', Items.LAVA_BUCKET)
                        .define('B', Items.BLAZE_ROD)
                        .define('S', Items.NETHERITE_SWORD)
                        .define('P', Items.PLAYER_HEAD)
                        .unlockedBy(getHasName(ModItems.VOLCANIC_CORE), has(ModItems.VOLCANIC_CORE))
                        .group("legendary-weapon")
                        .save(output, "emberfang_sword");

                shaped(RecipeCategory.MISC, ModItems.TEMPEST_EDGE)
                        .pattern("PEP")
                        .pattern(" B ")
                        .pattern(" S ")
                        .define('E', ModItems.ESSENCE_OF_WIND)
                        .define('P', Items.PLAYER_HEAD)
                        .define('B', Items.BREEZE_ROD)
                        .define('S', Items.NETHERITE_SWORD)
                        .unlockedBy(getHasName(ModItems.ESSENCE_OF_WIND), has(ModItems.ESSENCE_OF_WIND))
                        .group("legendary-weapon")
                        .save(output, "tempest_edge");

                shaped(RecipeCategory.MISC, ModItems.FROSTMOURNE)
                        .pattern("BFB")
                        .pattern("PYP")
                        .pattern("SNS")
                        .define('F', ModItems.FROZEN_FANG)
                        .define('Y', ModItems.YETI_HEART)
                        .define('B', Items.BLUE_ICE)
                        .define('P', Items.PLAYER_HEAD)
                        .define('S', Items.POWDER_SNOW_BUCKET)
                        .define('N', Items.NETHERITE_SWORD)
                        .unlockedBy(getHasName(ModItems.YETI_HEART), has(ModItems.YETI_HEART))
                        .group("legendary-weapon")
                        .save(output, "frostmourne_staff");

                shaped(RecipeCategory.MISC, ModItems.JARNGREIPR)
                        .pattern("IHI")
                        .pattern("NBN")
                        .pattern("PAP")
                        .define('B', ModItems.BERSERKERS_RELIC)
                        .define('I', Items.IRON_BLOCK)
                        .define('N', Items.NETHERITE_INGOT)
                        .define('P', Items.PLAYER_HEAD)
                        .define('H', Items.HEAVY_CORE)
                        .define('A', Items.NETHERITE_AXE)
                        .unlockedBy(getHasName(ModItems.BERSERKERS_RELIC), has(ModItems.BERSERKERS_RELIC))
                        .group("legendary-weapon")
                        .save(output, "jarngreipr_axe");

                shaped(RecipeCategory.MISC, ModItems.DRAGONS_RUIN)
                        .pattern("SSS")
                        .pattern("SES")
                        .pattern("PNP")
                        .define('S', ModItems.DRAGON_SCALE)
                        .define('E', Items.DRAGON_EGG)
                        .define('P', Items.PLAYER_HEAD)
                        .define('N', Items.NETHER_STAR)
                        .unlockedBy(getHasName(ModItems.DRAGON_SCALE), has(ModItems.DRAGON_SCALE))
                        .group("legendary-weapon")
                        .save(output, "dragons_ruin_scythe");

                shaped(RecipeCategory.MISC, ModItems.CROWN_OF_THE_ANCIENTS)
                        .pattern("GSG")
                        .pattern("GHG")
                        .pattern("GGG")
                        .define('S', ModItems.SIGIL_OF_AEGIS)
                        .define('H', Items.HEAVY_CORE)
                        .define('G', Items.GOLD_BLOCK)
                        .unlockedBy(getHasName(ModItems.SIGIL_OF_AEGIS), has(ModItems.SIGIL_OF_AEGIS))
                        .group("legendary-weapon")
                        .save(output, "crown_of_the_ancients_helmet");

                shaped(RecipeCategory.MISC, ModItems.SOUL_OF_AEGIS)
                        .pattern("IAI")
                        .pattern("IHI")
                        .pattern("GNG")
                        .define('A', ModItems.HEART_OF_AEGIS)
                        .define('I', Items.IRON_BLOCK)
                        .define('G', Items.GOLD_BLOCK)
                        .define('H', Items.HEAVY_CORE)
                        .define('N', Items.NETHERITE_INGOT)
                        .unlockedBy(getHasName(ModItems.HEART_OF_AEGIS), has(ModItems.HEART_OF_AEGIS))
                        .group("legendary-weapon")
                        .save(output, "soul_of_aegis_chestplate");

                shaped(RecipeCategory.MISC, ModItems.AEGIS_RESOLVE)
                        .pattern("IAI")
                        .pattern("GHG")
                        .pattern("N N")
                        .define('A', ModItems.AEGIS_REMNANT)
                        .define('H', Items.HEAVY_CORE)
                        .define('N', Items.NETHERITE_INGOT)
                        .define('I', Items.IRON_BLOCK)
                        .define('G', Items.GOLD_BLOCK)
                        .unlockedBy(getHasName(ModItems.AEGIS_REMNANT), has(ModItems.AEGIS_REMNANT))
                        .group("legendary-weapon")
                        .save(output, "aegis_resolve_leggings");

                shaped(RecipeCategory.MISC, ModItems.AEGIS_VALOR)
                        .pattern(" F ")
                        .pattern("GHG")
                        .pattern("N N")
                        .define('F', ModItems.FRAGMENT_OF_VALOR)
                        .define('G', Items.GOLD_BLOCK)
                        .define('H', Items.HEAVY_CORE)
                        .define('N', Items.NETHERITE_INGOT)
                        .unlockedBy(getHasName(ModItems.FRAGMENT_OF_VALOR), has(ModItems.FRAGMENT_OF_VALOR))
                        .group("legendary-weapon")
                        .save(output, "aegis_valor_boots");
                        //LEGENDARY CRAFTING ITEMS

                shaped(RecipeCategory.MISC, ModItems.VOLCANIC_CORE)
                        .pattern("MBM")
                        .pattern("ONO")
                        .pattern("MBM")
                        .define('M', Items.MAGMA_CREAM)
                        .define('B', Items.BLAZE_ROD)
                        .define('N', Items.NETHER_STAR)
                        .define('O', Items.OBSIDIAN)
                        .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                        .group("legendary-weapon-base")
                        .save(output, "volcanic_core");

                shaped(RecipeCategory.MISC, ModItems.ESSENCE_OF_WIND)
                        .pattern("PBP")
                        .pattern("FHF")
                        .pattern("PBP")
                        .define('H', ModItems.HEART_OF_THE_SKY)
                        .define('P', Items.PHANTOM_MEMBRANE)
                        .define('B', Items.BREEZE_ROD)
                        .define('F', Items.FEATHER)
                        .unlockedBy(getHasName(ModItems.HEART_OF_THE_SKY), has(ModItems.HEART_OF_THE_SKY))
                        .group("legendary-weapon-base")
                        .save(output, "essence_of_wind");

                    // YETI ITEMS
                shaped(RecipeCategory.MISC, ModItems.FROSTBITE_ARROW)
                        .pattern(" F ")
                        .pattern("ASA")
                        .pattern(" A ")
                        .define('F', ModItems.FROZEN_FANG)
                        .define('A', Items.ARROW)
                        .define('S', Items.SNOWBALL)
                        .unlockedBy(getHasName(ModItems.FROZEN_FANG), has(ModItems.FROZEN_FANG))
                        .group("yeti-items")
                        .save(output, "frostbite_arrow");

                shaped(RecipeCategory.MISC, ModItems.YETI_CLOAK)
                        .pattern("YLY")
                        .pattern("LTL")
                        .pattern("YPY")
                        .define('Y', ModItems.YETI_FUR)
                        .define('L', Items.LEATHER)
                        .define('T', Items.LEATHER_CHESTPLATE)
                        .define('P', Items.LEATHER_LEGGINGS)
                        .unlockedBy(getHasName(ModItems.YETI_FUR), has(ModItems.YETI_FUR))
                        .group("yeti-items")
                        .save(output, "yeti_cloak");

            }
        };
    }

    @Override
    public String getName() {
        return "Divine Relics Recipes";
    }
}