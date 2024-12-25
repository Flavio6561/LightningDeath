package com.lightningdeath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.*;

import java.util.*;

public class LightningDeathClient implements ClientModInitializer {
	private static boolean includePlayer = true;
	private static int lightningCount = 1;
	private static boolean isModMenuEnabled = false;
	private int remainingBolts = 0;

	private static final Logger LOGGER = LoggerFactory.getLogger(LightningDeathClient.class);
	private final Map<PlayerEntity, BlockPosition> playerDeathPositions = new HashMap<>();

	@Override
	public void onInitializeClient() {
		ConfigManager.loadConfig();
		ClientTickEvents.END_CLIENT_TICK.register(this::handlePlayerDeaths);
		LOGGER.info("LightningDeath initialized with success");
	}

	private void handlePlayerDeaths(MinecraftClient client) {
		if (client.isFinishedLoading() && !isModMenuEnabled)
			CommandRegister.registerCommands();
		if (client.world == null || lightningCount == 0)
			return;

		ClientWorld world = client.world;
		List<PlayerEntity> playersToRemove = new ArrayList<>();

		for (PlayerEntity player : world.getPlayers()) {
			if (player.isDead()) {
				BlockPosition deathPos = new BlockPosition(player.getX(), player.getY(), player.getZ());
				if (!includePlayer && player == client.player)
					playerDeathPositions.put(player, deathPos);
				if (remainingBolts != 0)
					spawnBolts(world, deathPos);
				if (!playerDeathPositions.containsKey(player)) {
					playerDeathPositions.put(player, deathPos);
					remainingBolts = lightningCount;
					spawnBolts(world, deathPos);
				}
			} else
				playersToRemove.add(player);
		}
		removeAlivePlayersFromTracking(playersToRemove);
	}

	private void removeAlivePlayersFromTracking(List<PlayerEntity> playersToRemove) {
		for (PlayerEntity player : playersToRemove)
			playerDeathPositions.remove(player);
	}

	private void spawnBolts(ClientWorld world, BlockPosition deathPos) {
		remainingBolts--;
		displayLightningBolt(world, deathPos);
	}

	private void displayLightningBolt(ClientWorld world, BlockPosition deathPos) {
		Entity lightningBolt = EntityType.LIGHTNING_BOLT.create(world, null);
		if (lightningBolt == null)
			return;
		lightningBolt.updatePosition(deathPos.x(), deathPos.y(), deathPos.z());
		world.addEntity(lightningBolt);
	}

	public static void sendMessageToPlayer(String chat_message) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null)
			client.player.sendMessage(Text.literal(chat_message), false);
	}

	public static void setIncludePlayer(boolean includePlayer) {
		LightningDeathClient.includePlayer = includePlayer;
		ConfigManager.saveConfig();
	}

	public static void setLightningCount(int lightningCount) {
		LightningDeathClient.lightningCount = lightningCount;
		ConfigManager.saveConfig();
	}

	public static void setModMenuEnabled() {
		isModMenuEnabled = true;
	}

	public static boolean isIncludePlayer() {
		return includePlayer;
	}

	public static int getLightningCount() {
		return lightningCount;
	}
}