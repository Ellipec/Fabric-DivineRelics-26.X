package ellipec.divinerelics.item;

import ellipec.divinerelics.DivineRelics;
import ellipec.divinerelics.creativemodtab.ModCreativeModTabs;
import ellipec.divinerelics.item.custom.HeartofAegis;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Function;

public class ModItems {
    public static final Item SIGIL_OF_AEGIS = registerItem("sigil-of-aegis", Item::new);
    public static final Item AEGIS_REMNANT = registerItem("aegis-remnant", Item::new);
    public static final Item HEART_OF_AEGIS = registerItem("heart-of-aegis", Item::new);
    // I know this registerItem part isn't finished, that's because I went to mess around a little with ModDDataComponents and it didnt work ayways so I left it like this.
    public static final Item FRAGMENT_OF_VALOR = registerItem("fragment-of-valor", properties -> new Item(properties.humanoidArmor(ModArmorMaterials.FRAGMENT_DIVINE_ARMOUR_MATERIAL, ArmorType.BOOTS)));
    public static final Item VOLCANIC_CORE = registerItem("volcanic-core", Item::new);
    public static final Item ESSENCE_OF_WIND = registerItem("essence-of-wind", Item::new);
    public static final Item HEART_OF_THE_SKY = registerItem("heart-of-the-sky", Item::new);
    public static final Item ESSENCE_OF_THUNDER = registerItem("essence-of-thunder", Item::new);
    public static final Item DRAGON_SCALE = registerItem("dragon-scale", Item::new);
    // YETI ITEMS BELOW
    public static final Item YETI_CLOAK = registerItem("yeti-cloak", Item::new);
    public static final Item FROSTBITE_ARROW = registerItem("frostbite-arrow", Item::new);
    public static final Item YETI_HEART = registerItem("yeti-heart", Item::new);
    public static final Item FROZEN_FANG = registerItem("frozen-fang", Item::new);
    public static final Item YETI_FUR = registerItem("yeti-fur", Item::new);
    // Legendary Items Below
    public static final Item CROWN_OF_THE_ANCIENTS = registerItem("crown-of-the-ancients", properties -> new Item((properties.humanoidArmor(ModArmorMaterials.DIVINE_ARMOR_MATERIAL, ArmorType.HELMET).fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE))));
    public static final Item SOUL_OF_AEGIS = registerItem("soul-of-aegis", properties -> new Item((properties.humanoidArmor(ModArmorMaterials.DIVINE_ARMOR_MATERIAL, ArmorType.CHESTPLATE).fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE))));
    public static final Item AEGIS_RESOLVE = registerItem("aegis-resolve", properties -> new Item((properties.humanoidArmor(ModArmorMaterials.DIVINE_ARMOR_MATERIAL, ArmorType.LEGGINGS).fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE))));
    public static final Item AEGIS_VALOR = registerItem("aegis-valor", properties -> new Item((properties.humanoidArmor(ModArmorMaterials.DIVINE_ARMOR_MATERIAL, ArmorType.BOOTS).fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE))));

    public static final Item EMBERFANG = registerItem("emberfang", properties -> new Item(properties.sword(ModToolMaterials.DIVINE, 4f, -2.4f) .fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE)));
    public static final Item TEMPEST_EDGE = registerItem("tempest-edge", properties -> new Item(properties.sword(ModToolMaterials.DIVINE, 4f, -2.4f) .fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE)));
    public static final Item FROSTMOURNE = registerItem("frostmourne", properties -> new Item(properties.sword(ModToolMaterials.DIVINE, 4f, -2.4f) .fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE)));
    public static final Item OLYMPUS_WRATH = registerItem("olympus-wrath", properties -> new Item(properties.sword(ModToolMaterials.DIVINE, 4f, -2.4f) .fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE)));
    public static final Item DRAGONS_RUIN = registerItem("dragons-ruin", properties -> new Item(properties.sword(ModToolMaterials.DIVINE, 4f, -2.4f) .fireResistant().component(DataComponents.UNBREAKABLE, Unit.INSTANCE)));


    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name)))));
    }

    public static void registerModItems() {
        DivineRelics.LOGGER.info("Registering ModItems for " + DivineRelics.MOD_ID);

        };
}
