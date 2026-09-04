package ellipec.divinerelics;

import ellipec.divinerelics.datagen.ModDataComponents;
import ellipec.divinerelics.datagen.ModEquipmentAssetProvider;
import ellipec.divinerelics.datagen.ModItemTagsProvider;
import ellipec.divinerelics.datagen.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DivineRelicsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModEquipmentAssetProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
	}
}
