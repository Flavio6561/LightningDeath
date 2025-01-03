package com.lightningdeath;

import net.fabricmc.fabric.api.client.command.v2.*;

public class CommandRegister {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("ld")
                        .executes(LightningDeathClient::toggleMod)
                        .then(ClientCommandManager.literal("IncludePlayer")
                                .executes(LightningDeathClient::toggleIncludePlayer)))
        );
    }
}