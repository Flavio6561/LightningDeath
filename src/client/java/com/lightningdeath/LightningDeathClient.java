package com.lightningdeath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class LightningDeathClient implements ClientModInitializer {
	private static final ArrayList<PlayerEntity> ignoredPlayers = new ArrayList<>();

	@Override
	public void onInitializeClient() {
		WorldRenderEvents.END.register(LightningDeathClient::lightningDeathMain);
	}

	private static void lightningDeathMain(WorldRenderContext context) {
		ArrayList<PlayerEntity> toAdd = new ArrayList<>();
		ArrayList<PlayerEntity> toRemove = new ArrayList<>();

		for (PlayerEntity player : context.world().getPlayers()) {
			if (!player.isDead())
				toRemove.add(player);
			else if (!ignoredPlayers.contains(player)) {
				toAdd.add(player);
				Entity lightningBolt = EntityType.LIGHTNING_BOLT.create(context.world());
				if (lightningBolt != null) {
					lightningBolt.updatePosition(player.getX(), player.getY(), player.getZ());
					context.world().addEntity(context.world().getRegularEntityCount() + 1, lightningBolt);
				}
			}
		}
        ignoredPlayers.addAll(toAdd);
		ignoredPlayers.removeAll(toRemove);
	}
}