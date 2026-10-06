package ellipec.divinerelics;

import ellipec.divinerelics.Networking.ModPackets;
import ellipec.divinerelics.Networking.ServerboundPackets;
import ellipec.divinerelics.creativemodtab.ModCreativeModTabs;
import ellipec.divinerelics.entity.ModEntities;
import ellipec.divinerelics.item.ModItems;
import ellipec.divinerelics.loot.ModLootTableModifiers;
import ellipec.divinerelics.powers.passive.DragonRuinPassives;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ellipec.divinerelics.powers.passive.TempestEdgePassives;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class DivineRelics implements ModInitializer {
	public static final String MOD_ID = "divinerelics";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModCreativeModTabs.registerModCreativeModTabs();
		ModItems.registerModItems();
		LootTableEvents.MODIFY.register(ModLootTableModifiers::modifyLootTables);
		ModPackets.registerC2SPackets();
		ServerboundPackets.register();
		DragonRuinPassives.register();
		TempestEdgePassives.register();
		ModEntities.register();


		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				TempestEdgePassives.applyPassives(player);
			}
		});
	}
}
