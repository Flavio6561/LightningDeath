package com.lightningdeath;

import com.terraformersmc.modmenu.api.*;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        LightningDeathClient.setModMenuEnabled();
        return ModConfigScreen::getConfigScreen;
    }
}