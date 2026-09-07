package ellipec.divinerelics.datagen;

import ellipec.divinerelics.item.ModArmorMaterials;
import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.SIGIL_OF_AEGIS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.AEGIS_REMNANT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.HEART_OF_AEGIS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.VOLCANIC_CORE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ESSENCE_OF_WIND, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FROSTBITE_ARROW, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.BERSERKERS_RELIC, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DRAGON_SCALE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.HEART_OF_THE_SKY, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FRAGMENT_OF_VALOR, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.YETI_CLOAK, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.YETI_FUR, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.YETI_HEART, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FROZEN_FANG, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.ESSENCE_OF_THUNDER, ModelTemplates.FLAT_ITEM);
        // LEGENDARY ITEMS BELOW
        itemModelGenerators.generateFlatItem(ModItems.EMBERFANG, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.TEMPEST_EDGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FROSTMOURNE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.JARNGREIPR, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DRAGONS_RUIN, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.OLYMPUS_WRATH, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateTrimmableItem(ModItems.CROWN_OF_THE_ANCIENTS, ModArmorMaterials.DIVINE_KEY,
                ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModelGenerators.generateTrimmableItem(ModItems.SOUL_OF_AEGIS, ModArmorMaterials.DIVINE_KEY,
                ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModelGenerators.generateTrimmableItem(ModItems.AEGIS_RESOLVE, ModArmorMaterials.DIVINE_KEY,
                ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModelGenerators.generateTrimmableItem(ModItems.AEGIS_VALOR, ModArmorMaterials.DIVINE_KEY,
                ItemModelGenerators.TRIM_PREFIX_BOOTS, false);


    }
}
