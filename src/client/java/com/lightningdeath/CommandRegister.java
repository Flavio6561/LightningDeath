package com.lightningdeath;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CommandRegister {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("ld")
                        .then(ClientCommandManager.literal("SetLightningCount")
                                .then(ClientCommandManager.argument("value (0,20)", IntegerArgumentType.integer())
                                        .executes(CommandRegister::setLightningCount)))
                        .then(ClientCommandManager.literal("SetIgnoreSelf")
                                .then(ClientCommandManager.argument("bool (true, false)", BoolArgumentType.bool())
                                        .executes(CommandRegister::setIgnoreSelf)))
        ));
    }

    private static int setIgnoreSelf(CommandContext<FabricClientCommandSource> context) {
        boolean ignoreSelf = BoolArgumentType.getBool(context, "bool (true, false)");
        LightningDeathClient.sendMessageToPlayer("IgnoreSelf has been set to: " + ignoreSelf);
        LightningDeathClient.setIgnoreSelf(ignoreSelf);
        return 1;
    }

    private static int setLightningCount(CommandContext<FabricClientCommandSource> context) {
        int lightningCount = IntegerArgumentType.getInteger(context, "value (0,20)");
        if (lightningCount < 0 || lightningCount > 20) {
            LightningDeathClient.sendMessageToPlayer("Your value is not acceptable! Insert a value between 0 and 20");
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