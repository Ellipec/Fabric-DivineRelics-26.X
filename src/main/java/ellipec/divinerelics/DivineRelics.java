package ellipec.divinerelics;

import ellipec.divinerelics.creativemodtab.ModCreativeModTabs;
import ellipec.divinerelics.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DivineRelics implements ModInitializer {
	public static final String MOD_ID = "divinerelics";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModCreativeModTabs.registerModCreativeModTabs();

		ModItems.registerModItems();


	}
}
