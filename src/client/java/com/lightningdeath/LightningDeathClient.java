package com.lightningdeath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LightningDeathClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(LightningDeathClient.class);
	private static final List<PlayerEntity> ignoredPlayers = new ArrayList<>();

	@Override
	public void onInitializeClient() {
		WorldRenderEvents.END.register(context -> lightningDeathMain());
		LOGGER.info("LightningDeath initialized");
	}

	private static void lightningDeathMain() {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null)
			return;
        List<PlayerEntity> toAdd = new ArrayList<>();
		List<PlayerEntity> toRemove = new ArrayList<>();

		for (PlayerEntity player : world.getPlayers()) {
			if (!player.isDead())
				toRemove.add(player);
			else if (!ignoredPlayers.contains(player)) {
				toAdd.add(player);
				Entity lightningBolt = EntityType.LIGHTNING_BOLT.create(world, null);
				if (lightningBolt != null)
					lightningBolt.updatePosition(player.getX(), player.getY(), player.getZ());
                world.addEntity(lightningBolt);
			}
		}
        ignoredPlayers.addAll(toAdd);
		ignoredPlayers.removeAll(toRemove);
	}
}