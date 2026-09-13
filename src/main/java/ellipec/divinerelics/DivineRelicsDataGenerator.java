package ellipec.divinerelics;

import ellipec.divinerelics.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DivineRelicsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModEquipmentAssetProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModRecipeProvider::new);
	}
}
