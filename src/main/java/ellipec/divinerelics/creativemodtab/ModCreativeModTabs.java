package ellipec.divinerelics.creativemodtab;

import ellipec.divinerelics.DivineRelics;
import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModTabs {
    public static final CreativeModeTab DIVINE_RELICS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
    Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, "divine_relics"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.AEGIS_REMNANT))
                    .title(Component.translatable("creativemodetab.divinerelics.divine_relics"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.AEGIS_REMNANT);
                        output.accept(ModItems.HEART_OF_AEGIS);
                        output.accept(ModItems.SIGIL_OF_AEGIS);
                        output.accept(ModItems.VOLCANIC_CORE);
                        output.accept(ModItems.ESSENCE_OF_THUNDER);
                        output.accept(ModItems.ESSENCE_OF_WIND);
                        output.accept(ModItems.HEART_OF_THE_SKY);
                        output.accept(ModItems.DRAGON_SCALE);
                        output.accept(ModItems.FROSTBITE_ARROW);
                    }).build());


    public static void registerModCreativeModTabs() {
        DivineRelics.LOGGER.info("Registering Mod Creative Mod Tabsfor " + DivineRelics.MOD_ID);
    }
}
