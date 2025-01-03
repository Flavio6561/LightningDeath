package com.lightningdeath;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.*;
import java.util.ArrayList;
import java.util.List;

public class LightningDeathClient implements ClientModInitializer {
	private static boolean toggleMod = true;
	private static boolean includePlayer = true;

	private static final Logger LOGGER = LoggerFactory.getLogger(LightningDeathClient.class);
	private static final List<PlayerEntity> ignoredPlayers = new ArrayList<>();

	@Override
	public void onInitializeClient() {
		ConfigManager.loadConfig();
		CommandRegister.registerCommands();
		WorldRenderEvents.END.register(context -> handlePlayerDeaths(MinecraftClient.getInstance()));
		LOGGER.info("LightningDeath initialized with success");
	}

	private void handlePlayerDeaths(MinecraftClient client) {
		ClientWorld world = client.world;
		if (world == null || !toggleMod)
			return;
		for (PlayerEntity player : world.getPlayers()) {
			if (!player.isDead())
				ignoredPlayers.remove(player);
			else if (!ignoredPlayers.contains(player)) {
				if (player.isMainPlayer() && !includePlayer)
					return;
				ignoredPlayers.add(player);
				Entity lightningBolt = EntityType.LIGHTNING_BOLT.create(world, null);
				if (lightningBolt == null)
					return;
				lightningBolt.updatePosition(player.getX(), player.getY(), player.getZ());
				world.addEntity(lightningBolt);
			}
		}
	}

	public static void sendMessageToPlayer(String chat_message) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null)
			client.player.sendMessage(Text.literal(chat_message), false);
	}

	public static void setToggleMod(boolean toggleMod) {
		LightningDeathClient.toggleMod = toggleMod;
		ConfigManager.saveConfig();
	}

	public static void setIncludePlayer(boolean includePlayer) {
		LightningDeathClient.includePlayer = includePlayer;
		ConfigManager.saveConfig();
	}

	public static boolean isToggleMod() {
		return toggleMod;
	}

	public static boolean isIncludePlayer() {
		return includePlayer;
	}

	public static int toggleMod(CommandContext<FabricClientCommandSource> fabricClientCommandSourceCommandContext) {
		if (toggleMod) {
			setToggleMod(false);
			sendMessageToPlayer("Mod is now disabled");
		} else {
			setToggleMod(true);
			sendMessageToPlayer("Mod is now enabled");
		}
		return 1;
	}

	public static int toggleIncludePlayer(CommandContext<FabricClientCommandSource> fabricClientCommandSourceCommandContext) {
		if (includePlayer) {
			setIncludePlayer(false);
			sendMessageToPlayer("Now lightning bolts will NOT spawn on your character");
		} else {
			setIncludePlayer(true);
			sendMessageToPlayer("Now lightning bolts WILL spawn on your character");
		}
		return 1;
	}
}