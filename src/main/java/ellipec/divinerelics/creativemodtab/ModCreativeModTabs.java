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
                        output.accept(ModItems.SIGIL_OF_AEGIS);
                        output.accept(ModItems.HEART_OF_AEGIS);
                        output.accept(ModItems.AEGIS_REMNANT);
                        output.accept(ModItems.FRAGMENT_OF_VALOR);
                        output.accept(ModItems.VOLCANIC_CORE);
                        output.accept(ModItems.HEART_OF_THE_SKY);
                        output.accept(ModItems.ESSENCE_OF_WIND);
                        output.accept(ModItems.ESSENCE_OF_THUNDER);
                        output.accept(ModItems.DRAGON_SCALE);
                        // YETI ITEMS BELOW
                        output.accept(ModItems.FROSTBITE_ARROW);
                        output.accept(ModItems.YETI_CLOAK);
                        output.accept(ModItems.YETI_HEART);
                        output.accept(ModItems.FROZEN_FANG);
                        output.accept(ModItems.YETI_FUR);
                        // LEGENDARY ITEMS BELOW
                        output.accept(ModItems.AEGIS_VALOR);
                        output.accept(ModItems.AEGIS_RESOLVE);
                        output.accept(ModItems.SOUL_OF_AEGIS);
                        output.accept(ModItems.CROWN_OF_THE_ANCIENTS);
                        output.accept(ModItems.EMBERFANG);
                        output.accept(ModItems.TEMPEST_EDGE);
                        output.accept(ModItems.FROSTMOURNE);
                        output.accept(ModItems.OLYMPUS_WRATH);
                        output.accept(ModItems.DRAGONS_RUIN);

                    }).build());


    public static void registerModCreativeModTabs() {
        DivineRelics.LOGGER.info("Registering Mod Creative Mod Tabsfor " + DivineRelics.MOD_ID);
    }
}
