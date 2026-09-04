package ellipec.divinerelics.datagen;

import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ItemTags.SWORDS).add(ModItems.EMBERFANG);
        valueLookupBuilder(ItemTags.SWORDS).add(ModItems.TEMPEST_EDGE);
        valueLookupBuilder(ItemTags.SWORDS).add(ModItems.FROSTMOURNE);
        valueLookupBuilder(ItemTags.SWORDS).add(ModItems.OLYMPUS_WRATH);
        valueLookupBuilder(ItemTags.SWORDS).add(ModItems.DRAGONS_RUIN);

        valueLookupBuilder(ItemTags.AXES).add(ModItems.JARNGREIPR);

        valueLookupBuilder(ItemTags.HEAD_ARMOR).add(ModItems.CROWN_OF_THE_ANCIENTS);
        valueLookupBuilder(ItemTags.CHEST_ARMOR).add(ModItems.SOUL_OF_AEGIS);
        valueLookupBuilder(ItemTags.LEG_ARMOR).add(ModItems.AEGIS_RESOLVE);
        valueLookupBuilder(ItemTags.FOOT_ARMOR).add(ModItems.AEGIS_VALOR);

    }


}

