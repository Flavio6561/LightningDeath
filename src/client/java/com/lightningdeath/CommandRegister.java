package com.lightningdeath;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.*;

public class CommandRegister {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("ld")
                        .then(ClientCommandManager.literal("LightningCount")
                                .then(ClientCommandManager.argument("0... 20", IntegerArgumentType.integer())
                                        .executes(CommandRegister::setLightningCount)))
                        .then(ClientCommandManager.literal("IncludePlayer")
                                .then(ClientCommandManager.argument("true, false", BoolArgumentType.bool())
                                        .executes(CommandRegister::setIncludePlayer)))
        ));
    }

    private static int setIncludePlayer(CommandContext<FabricClientCommandSource> context) {
        boolean includePlayer = BoolArgumentType.getBool(context, "true, false");
        LightningDeathClient.sendMessageToPlayer("IncludePlayer has been set to: " + includePlayer);
        LightningDeathClient.setIncludePlayer(includePlayer);
        return 1;
    }

    private static int setLightningCount(CommandContext<FabricClientCommandSource> context) {
        int lightningCount = IntegerArgumentType.getInteger(context, "0... 20");
        if (lightningCount < 0 || lightningCount > 20) {
            LightningDeathClient.sendMessageToPlayer("Insert a value between 0 and 20");
        } else if (lightningCount == 0) {
            LightningDeathClient.sendMessageToPlayer("Mod has been disabled");
            LightningDeathClient.setLightningCount(lightningCount);
        } else {
            LightningDeathClient.sendMessageToPlayer("LightningCount has been set to: " + lightningCount);
            LightningDeathClient.setLightningCount(lightningCount);
        }
        return 1;
    }
}