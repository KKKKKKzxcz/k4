package com.example.gunmod;

import net.fabricmc.api.ModInitializer;

public class GunMod implements ModInitializer {
    public static final String MOD_ID = "gunmod";

    @Override
    public void onInitialize() {
        ModSounds.init();
        ModItems.init();
    }
}
