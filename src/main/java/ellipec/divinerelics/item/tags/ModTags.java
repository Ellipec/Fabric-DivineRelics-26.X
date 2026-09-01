package ellipec.divinerelics.item.tags;


import ellipec.divinerelics.DivineRelics;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_DIVINE_TOOL = createTag("needs_divine_tool");
        public static final TagKey<Block> INCORRECT_FOR_DIVINE_TOOL = createTag("incorrect_for_divine_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> DIVINE_REPAIR = createTag("divine_repair");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DivineRelics.MOD_ID, name));
        }
    }

}
