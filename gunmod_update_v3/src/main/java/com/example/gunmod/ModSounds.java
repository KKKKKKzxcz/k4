package com.example.gunmod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent PISTOL = register("gun_pistol");
    public static final SoundEvent REVOLVER = register("gun_revolver");
    public static final SoundEvent PISTOL_BURST = register("gun_pistol_burst");
    public static final SoundEvent RIFLE = register("gun_rifle");
    public static final SoundEvent SMG = register("gun_smg");
    public static final SoundEvent MG4 = register("gun_mg4");
    public static final SoundEvent MG6 = register("gun_mg6");
    public static final SoundEvent MG8 = register("gun_mg8");
    public static final SoundEvent SHOTGUN = register("gun_shotgun");
    public static final SoundEvent EMPTY = register("gun_empty");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.of(GunMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
    }
}
