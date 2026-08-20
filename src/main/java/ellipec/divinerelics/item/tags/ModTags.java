package ellipec.divinerelics.item.tags;

import ellipec.divinerelics.DivineRelics;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
private static TagKey<Item> createTag(String name) {
    return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name));
}
    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");
        public static final TagKey<Item> DIVINE_REPAIR = createTag("divine_repair");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name));
    }}}
