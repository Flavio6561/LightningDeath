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
        boolean toggleMod;
        boolean includePlayer;
    }

    public static void loadConfig() {
        Gson gson = new Gson();
        File configFile = configPath.toFile();
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                config = gson.fromJson(reader, Config.class);
                if (config == null)
                    restoreDefaultConfig();
            } catch (JsonSyntaxException | IOException exception) {
                restoreDefaultConfig();
            }
        } else
            restoreDefaultConfig();
        applyConfig();
    }

    private static void restoreDefaultConfig() {
        config = new Config();
        config.toggleMod = LightningDeathClient.isToggleMod();
        config.includePlayer = LightningDeathClient.isIncludePlayer();
        saveConfig();
    }

    private static void applyConfig() {
        LightningDeathClient.setToggleMod(config.toggleMod);
        LightningDeathClient.setIncludePlayer(config.includePlayer);
    }

    public static void saveConfig() {
        Gson gson = new Gson();
        File configFile = configPath.toFile();
        Config currentConfig = new Config();
        currentConfig.toggleMod = LightningDeathClient.isToggleMod();
        currentConfig.includePlayer = LightningDeathClient.isIncludePlayer();
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(currentConfig, writer);
        } catch (IOException exception) {
            restoreDefaultConfig();
        }
    }
}