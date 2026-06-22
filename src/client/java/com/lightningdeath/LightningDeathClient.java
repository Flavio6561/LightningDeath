package com.lightningdeath;

import com.lightningdeath.mixin.client.ClientLevelAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.TransientEntitySectionManager;

import java.util.ArrayList;

public class LightningDeathClient implements ClientModInitializer {
    private static final ArrayList<Player> ignoredPlayers = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_LEVEL_TICK.register(LightningDeathClient::lightningDeathMain);
    }

    private static void lightningDeathMain(ClientLevel clientLevel) {
        ArrayList<Player> toAdd = new ArrayList<>();
        ArrayList<Player> toRemove = new ArrayList<>();

        for (Player player : clientLevel.players()) {
            if (!player.isDeadOrDying())
                toRemove.add(player);
            else if (!ignoredPlayers.contains(player)) {
                toAdd.add(player);
                Entity lightningBolt = EntityTypes.LIGHTNING_BOLT.create(clientLevel, (EntitySpawnReason) null);
                if (lightningBolt != null) {
                    TransientEntitySectionManager<Entity> storage = ((ClientLevelAccessor) clientLevel).getEntityStorage();
                    int id = storage.count() + 1;

                    LevelEntityGetter<Entity> entityGetter = storage.getEntityGetter();
                    while (entityGetter.get(id) != null)
                        id++;

                    lightningBolt.setId(id);
                    lightningBolt.absSnapTo(player.getX(), player.getY(), player.getZ());
                    storage.addEntity(lightningBolt);
                }
            }
        }
        ignoredPlayers.addAll(toAdd);
        ignoredPlayers.removeAll(toRemove);
    }
}