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
	protected static boolean toggleMod = true;
	protected static boolean includePlayer = true;

	private static final Logger LOGGER = LoggerFactory.getLogger(LightningDeathClient.class);
	private static final List<PlayerEntity> ignoredPlayers = new ArrayList<>();

	@Override
	public void onInitializeClient() {
		ConfigManager.loadConfig();
		CommandRegister.registerCommands();
		WorldRenderEvents.END.register(context -> manageLightningDeaths(MinecraftClient.getInstance()));
		LOGGER.info("LightningDeath initialized with success");
	}

	private void manageLightningDeaths(MinecraftClient client) {
		ClientWorld world = client.world;
		if (world == null || !toggleMod)
			return;

		List<PlayerEntity> toRemove = new ArrayList<>();
		List<PlayerEntity> toAdd = new ArrayList<>();

		for (PlayerEntity player : world.getPlayers()) {
			if (!player.isDead())
				toRemove.add(player);
			else if (!ignoredPlayers.contains(player) && (!player.isMainPlayer() || includePlayer)) {
				toAdd.add(player);
				Entity lightningBolt = EntityType.LIGHTNING_BOLT.create(world, null);
				if (lightningBolt != null) {
					lightningBolt.updatePosition(player.getX(), player.getY(), player.getZ());
					world.addEntity(lightningBolt);
				}
			}
		}
		ignoredPlayers.removeAll(toRemove);
		ignoredPlayers.addAll(toAdd);
	}

	public static void sendMessageToPlayer(String chat_message) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null)
			client.player.sendMessage(Text.literal(chat_message), false);
	}

	public static int toggleMod(CommandContext<FabricClientCommandSource> ignoredFabricClientCommandSourceCommandContext) {
		toggleMod = !toggleMod;
		ConfigManager.saveConfig();
		sendMessageToPlayer("Mod is now " + (toggleMod ? "enabled" : "disabled"));
		return 1;
	}

	public static int toggleIncludePlayer(CommandContext<FabricClientCommandSource> ignoredFabricClientCommandSourceCommandContext) {
		includePlayer = !includePlayer;
		ConfigManager.saveConfig();
		sendMessageToPlayer("Lightning bolts will " + (includePlayer ? "" : "NOT ") + "spawn on playing character");
		return 1;
	}
}