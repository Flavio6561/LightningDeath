package com.lightningdeath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.slf4j.*;

import java.util.*;

public class LightningDeathClient implements ClientModInitializer {
	private static boolean includePlayer = true;
	private static int lightningCount = 1;
	private static boolean isModMenuEnabled = false;
	private int remainingBolts = 0;

	private static final Logger LOGGER = LoggerFactory.getLogger(LightningDeathClient.class);
	private final Map<PlayerEntity, BlockPos> playerDeathPositions = new HashMap<>();

	@Override
	public void onInitializeClient() {
		ConfigManager.loadConfig();
		ClientTickEvents.END_CLIENT_TICK.register(this::handlePlayerDeaths);
		LOGGER.info("LightningDeath initialized with success");
	}

	private void handlePlayerDeaths(MinecraftClient client) {
		if (client.isFinishedLoading() && !isModMenuEnabled) {
			CommandRegister.registerCommands();
		}
		if (client.world == null) {
			return;
		}
		if (lightningCount == 0) {
			return;
		}

		ClientWorld world = client.world;
		List<PlayerEntity> playersToRemove = new ArrayList<>();

		for (PlayerEntity player : world.getPlayers()) {
			if (player.isDead()) {
				if (!includePlayer && player == client.player) {
					BlockPos deathPos = player.getBlockPos();
					playerDeathPositions.put(player, deathPos);
				}
				if (remainingBolts != 0) {
					spawnBolts(world, player.getBlockPos());
				}
				if (!playerDeathPositions.containsKey(player)) {
					BlockPos deathPos = player.getBlockPos();
					playerDeathPositions.put(player, deathPos);
					remainingBolts = lightningCount;
					spawnBolts(world, player.getBlockPos());
					logLightningBoltStatus(player);
				}
			} else {
				playersToRemove.add(player);
			}
		}
		removeAlivePlayersFromTracking(playersToRemove);
	}

	private void removeAlivePlayersFromTracking(List<PlayerEntity> playersToRemove) {
		for (PlayerEntity player : playersToRemove) {
			playerDeathPositions.remove(player);
		}
	}

	private void spawnBolts (ClientWorld world, BlockPos deathPos) {
		remainingBolts--;
		displayLightningBolt(world, deathPos);
	}

	private void displayLightningBolt(ClientWorld world, BlockPos deathPos) {
		Entity lightningBolt = EntityType.LIGHTNING_BOLT.create(world);
		if (lightningBolt == null) {
			return;
		}
		lightningBolt.updatePosition(deathPos.getX(), deathPos.getY(), deathPos.getZ());
		world.addEntity(lightningBolt);
	}

	private void logLightningBoltStatus(PlayerEntity player) {
		if (lightningCount == 1) {
			LOGGER.info("{}'s lightning bolt displayed successfully at coordinates: {}", player.getName().getString(), player.getBlockPos().toShortString());
		} else {
			LOGGER.info("{}'s {} lightning bolts displayed successfully at coordinates: {}", player.getName().getString(), lightningCount, player.getBlockPos().toShortString());
		}
	}

	public static void sendMessageToPlayer(String chat_message) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null) {
			client.player.sendMessage(Text.literal(chat_message), false);
		}
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