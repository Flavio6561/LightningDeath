package com.lightningdeath;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        LightningDeathClient.setModMenuEnabled(true);
        return ModConfigScreen::getConfigScreen;
    }
}