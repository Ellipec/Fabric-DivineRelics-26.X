package ellipec.divinerelics.crafting;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.Codec;
import ellipec.divinerelics.DivineRelics;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.minecraft.commands.Commands.literal;

public class LegendaryCraftingData extends SavedData {

    private final Set<String> craftedItems;

    private static final Codec<LegendaryCraftingData> CODEC =
            Codec.STRING.listOf()
                    .xmap(
                            list -> new LegendaryCraftingData(new HashSet<>(list)),
                            data -> List.copyOf(data.craftedItems)
                    );

    public static final SavedDataType<LegendaryCraftingData> TYPE =
            new SavedDataType<>(
                    Identifier.fromNamespaceAndPath(
                            DivineRelics.MOD_ID,
                            "legendary_crafting"
                    ),
                    () -> new LegendaryCraftingData(new HashSet<>()),
                    CODEC,
                    null
            );

    private LegendaryCraftingData(Set<String> craftedItems) {
        this.craftedItems = craftedItems;
    }

    public static LegendaryCraftingData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public boolean hasBeenCrafted(String itemId) {
        return craftedItems.contains(itemId);
    }

    public void markCrafted(String itemId) {
        craftedItems.add(itemId);
        setDirty();
    }

    public void resetAll() {
        craftedItems.clear();
        setDirty();
    }

    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) ->
                        registerCommand(dispatcher)
        );
    }

    private static void registerCommand(
            CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(
                literal("resetlegendarycrafts")
                        .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                        .executes(context -> {
                            LegendaryCraftingData data =
                                    LegendaryCraftingData.get(
                                            context.getSource().getServer()
                                    );

                            data.resetAll();

                            context.getSource().sendSuccess(
                                    () -> Component.literal(
                                            "All legendary crafts have been reset."
                                    ),
                                    true
                            );

                            return 1;
                        })
        );
    }
}