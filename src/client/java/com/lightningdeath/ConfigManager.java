package com.lightningdeath;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigManager {
    private static Config config;
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("lightningDeath.6561");

    private static class Config {
        boolean ignoreSelf;
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
        config.ignoreSelf = LightningDeathClient.isIgnoreSelf();
        config.lightningCount = LightningDeathClient.getLightningCount();
        saveConfig();
    }

    private static void applyConfig() {
        LightningDeathClient.setIgnoreSelf(config.ignoreSelf);
        LightningDeathClient.setLightningCount(config.lightningCount);
    }

    public static void saveConfig() {
        Gson gson = new Gson();
        File configFile = configPath.toFile();
        Config currentConfig = new Config();
        currentConfig.ignoreSelf = LightningDeathClient.isIgnoreSelf();
        currentConfig.lightningCount = LightningDeathClient.getLightningCount();

        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(currentConfig, writer);
        } catch (IOException exception) {
            restoreDefaultConfig();
        }
    }
}