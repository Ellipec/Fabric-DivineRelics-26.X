package ellipec.divinerelics.item;

import ellipec.divinerelics.DivineRelics;
import ellipec.divinerelics.item.custom.HeartofAegis;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static final Item SIGIL_OF_AEGIS = registerItem("sigil-of-aegis", Item::new);
    public static final Item AEGIS_REMNANT = registerItem("aegis-remnant", Item::new);
    public static final Item HEART_OF_AEGIS = registerItem("heart-of-aegis", HeartofAegis::;
    // I know this registerItem part isn't finished, that's because I went to mess around a little with ModDDataComponents and it didnt work ayways so I left it like this.
    public static final Item FRAGMENT_OF_VALOR = registerItem("fragment-of-valor", Item::new);
    public static final Item VOLCANIC_CORE = registerItem("volcanic-core", Item::new);
    public static final Item ESSENCE_OF_WIND = registerItem("essence-of-wind", Item::new);
    public static final Item HEART_OF_THE_SKY = registerItem("heart-of-the-sky", Item::new);
    public static final Item FROSTBITE_ARROW = registerItem("frostbite-arrow", Item::new);
    public static final Item ESSENCE_OF_THUNDER = registerItem("essence-of-thunder", Item::new);
    public static final Item DRAGON_SCALE = registerItem("dragon-scale", Item::new);


    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name)))));
    }

    public static void registerModItems() {
        DivineRelics.LOGGER.info("Registering ModItems for " + DivineRelics.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(SIGIL_OF_AEGIS);
            output.accept(AEGIS_REMNANT);
            output.accept(HEART_OF_AEGIS);
            output.accept(FRAGMENT_OF_VALOR);
            output.accept(VOLCANIC_CORE);
            output.accept(ESSENCE_OF_THUNDER);
            output.accept(ESSENCE_OF_WIND);
            output.accept(HEART_OF_THE_SKY);
            output.accept(DRAGON_SCALE);
            output.accept(FROSTBITE_ARROW);
        });
    }
}
