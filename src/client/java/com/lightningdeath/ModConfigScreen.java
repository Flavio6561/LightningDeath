package com.lightningdeath;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModConfigScreen {
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.lightningdeath.config"));

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.lightningdeath.general"));

        BooleanToggleBuilder ignoreSelfBuilder = builder.entryBuilder()
                .startBooleanToggle(Text.translatable("option.lightningdeath.ignoreSelf"), LightningDeathClient.isIgnoreSelf())
                .setDefaultValue(false)
                .setSaveConsumer(LightningDeathClient::setIgnoreSelf)
                .setTooltip(Text.translatable("tooltip.lightningdeath.ignoreSelf"));
        general.addEntry(ignoreSelfBuilder.build());

        IntSliderBuilder lightningCountBuilder = builder.entryBuilder()
                .startIntSlider(Text.translatable("option.lightningdeath.lightningCount"), LightningDeathClient.getLightningCount(), 0, 20)
                .setDefaultValue(1)
                .setSaveConsumer(LightningDeathClient::setLightningCount)
                .setTooltip(Text.translatable("tooltip.lightningdeath.lightningCount"));
        general.addEntry(lightningCountBuilder.build());

        builder.setSavingRunnable(ConfigManager::saveConfig);
        return builder.build();
    }
}