package ellipec.divinerelics.loot;

import ellipec.divinerelics.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModLootTableModifiers {
    public static void modifyLootTables(ResourceKey<LootTable> key, FabricLootTableBuilder builder,
                                        LootTableSource source, HolderLookup.Provider provider) {

        // ANCIENT CITY - 5% chance
        if (BuiltInLootTables.ANCIENT_CITY.equals(key)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.05f))
                    .add(LootItem.lootTableItem(ModItems.SIGIL_OF_AEGIS))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)).build());

            builder.pool(poolBuilder.build());
        }

        // BASTION OTHER - 1.5% chance
        if (BuiltInLootTables.BASTION_OTHER.equals(key)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.015f))
                    .add(LootItem.lootTableItem(ModItems.AEGIS_REMNANT))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)).build());

            builder.pool(poolBuilder.build());
        }

        // BASTION TREASURE - 5% chance
        if (BuiltInLootTables.BASTION_TREASURE.equals(key)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.05f))
                    .add(LootItem.lootTableItem(ModItems.AEGIS_REMNANT))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)).build());

            builder.pool(poolBuilder.build());
        }

        // BASTION BRIDGE - 3% chance
        if (BuiltInLootTables.BASTION_BRIDGE.equals(key)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.03f))
                    .add(LootItem.lootTableItem(ModItems.AEGIS_REMNANT))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)).build());

            builder.pool(poolBuilder.build());
        }

        // BASTION HOGLIN STABLE - 3% chance
        if (BuiltInLootTables.BASTION_HOGLIN_STABLE.equals(key)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.03f))
                    .add(LootItem.lootTableItem(ModItems.AEGIS_REMNANT))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)).build());

            builder.pool(poolBuilder.build());
        }

        // ENDER DRAGON - 100% chance, 3-5 scales
        if (key.identifier().equals(Identifier.withDefaultNamespace("entities/ender_dragon"))) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(1f))
                    .add(LootItem.lootTableItem(ModItems.DRAGON_SCALE))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2f, 5f)).build());

            builder.pool(poolBuilder.build());
        }

        // WITHER - 30% chance
        if (key.identifier().equals(Identifier.withDefaultNamespace("entities/wither"))) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.3f))
                    .add(LootItem.lootTableItem(ModItems.FRAGMENT_OF_VALOR))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)).build());

            builder.pool(poolBuilder.build());
        }

        // OMINOUS TRIAL CHAMBER UNIQUE - 5% chance
        if (BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE.equals(key)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.05f))
                    .add(LootItem.lootTableItem(ModItems.HEART_OF_AEGIS))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)).build());

            builder.pool(poolBuilder.build());
        }

    }
}