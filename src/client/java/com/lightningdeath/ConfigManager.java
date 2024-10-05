package com.lightningdeath;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class ConfigManager {
    private static Config config;
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("LightningDeath.json");

    private static class Config {
        boolean includePlayer;
        int lightningCount;
    }

    public static void loadConfig() {
        Gson gson = new Gson();
        File configFile = configPath.toFile();
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                config = gson.fromJson(reader, Config.class);
                if (config == null) {
                    restoreDefaultConfig();
                }
            } catch (JsonSyntaxException | IOException exception) {
                restoreDefaultConfig();
            }
        } else {
            restoreDefaultConfig();
        }
        applyConfig();
    }

    private static void restoreDefaultConfig() {
        config = new Config();
        config.includePlayer = LightningDeathClient.isIncludePlayer();
        config.lightningCount = LightningDeathClient.getLightningCount();
        saveConfig();
    }

    private static void applyConfig() {
        LightningDeathClient.setIncludePlayer(config.includePlayer);
        LightningDeathClient.setLightningCount(config.lightningCount);
    }

    public static void saveConfig() {
        Gson gson = new Gson();
        File configFile = configPath.toFile();
        Config currentConfig = new Config();
        currentConfig.includePlayer = LightningDeathClient.isIncludePlayer();
        currentConfig.lightningCount = LightningDeathClient.getLightningCount();

        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(currentConfig, writer);
        } catch (IOException exception) {
            restoreDefaultConfig();
        }
    }
}