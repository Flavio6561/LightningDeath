package com.lightningdeath;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class LightningDeathClient implements ClientModInitializer {
	private static final ArrayList<PlayerEntity> ignoredPlayers = new ArrayList<>();

	@Override
	public void onInitializeClient() {
        ClientTickEvents.END_WORLD_TICK.register(LightningDeathClient::lightningDeathMain);
	}

    private static void lightningDeathMain(ClientWorld clientWorld) {
        ArrayList<PlayerEntity> toAdd = new ArrayList<>();
        ArrayList<PlayerEntity> toRemove = new ArrayList<>();

        for (PlayerEntity player : ((World) clientWorld).getPlayers()) {
            if (!player.isDead())
                toRemove.add(player);
            else if (!ignoredPlayers.contains(player)) {
                toAdd.add(player);
                Entity lightningBolt = EntityType.LIGHTNING_BOLT.create(clientWorld, null);
                if (lightningBolt != null) {
                    lightningBolt.updatePosition(player.getX(), player.getY(), player.getZ());
                    clientWorld.addEntity(lightningBolt);
                }
            }
        }
        ignoredPlayers.addAll(toAdd);
        ignoredPlayers.removeAll(toRemove);
    }
}