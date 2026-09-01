package ellipec.divinerelics.item;

import ellipec.divinerelics.DivineRelics;
import ellipec.divinerelics.item.tags.ModTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.EquipmentAsset;

public class ModArmorMaterials {
    public static final ResourceKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static final ResourceKey<EquipmentAsset> DIVINE_KEY = ResourceKey.create(REGISTRY_KEY, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, "divine"));

    public static final ArmorMaterial DIVINE_ARMOR_MATERIAL = new ArmorMaterial(750,
            ArmorMaterials.makeDefense(4,7,9,4,20),
            20, SoundEvents.ARMOR_EQUIP_GOLD, 4F,0.2F, ModTags.Items.DIVINE_REPAIR, DIVINE_KEY);

    public static final ArmorMaterial FRAGMENT_DIVINE_ARMOUR_MATERIAL = new ArmorMaterial(1, ArmorMaterials.makeDefense(1,0,0,0,0),1, SoundEvents.ARMOR_EQUIP_CHAIN, 0,0, ModTags.Items.DIVINE_REPAIR, DIVINE_KEY);
}
