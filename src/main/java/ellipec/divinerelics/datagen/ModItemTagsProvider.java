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
        tag(ItemTags.SWORDS).add(ModItems.getRK(ModItems.EMBERFANG));
        tag(ItemTags.SWORDS).add(ModItems.getRK(ModItems.TEMPEST_EDGE));
        tag(ItemTags.SWORDS).add(ModItems.getRK(ModItems.FROSTMOURNE));
        tag(ItemTags.SWORDS).add(ModItems.getRK(ModItems.OLYMPUS_WRATH));
        tag(ItemTags.SWORDS).add(ModItems.getRK(ModItems.DRAGONS_RUIN));

        tag(ItemTags.AXES).add(ModItems.getRK(ModItems.JARNGREIPR));

        tag(ItemTags.HEAD_ARMOR).add(ModItems.getRK(ModItems.CROWN_OF_THE_ANCIENTS));
        tag(ItemTags.CHEST_ARMOR).add(ModItems.getRK(ModItems.SOUL_OF_AEGIS));
        tag(ItemTags.LEG_ARMOR).add(ModItems.getRK(ModItems.AEGIS_RESOLVE));
        tag(ItemTags.FOOT_ARMOR).add(ModItems.getRK(ModItems.AEGIS_VALOR));

    }


}

