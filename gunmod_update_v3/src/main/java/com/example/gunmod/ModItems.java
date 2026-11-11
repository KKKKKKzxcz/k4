package com.example.gunmod;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static final Item BULLET = register("bullet", Item::new, new Item.Settings());
    public static final Item SHELL = register("shell", Item::new, new Item.Settings());

    // Luger P08 ปืนพกกึ่งอัตโนมัติ
    public static final Item PISTOL = register("pistol",
            s -> new GunItem(s, 7f, 10, 48.0, 1, 0, 1, BULLET, 1.4f, ModSounds.PISTOL),
            new Item.Settings().maxDamage(250).maxCount(1));

    // Colt M1911 ปืนพกอเมริกัน ดาเมจสูงกว่า
    public static final Item M1911 = register("m1911",
            s -> new GunItem(s, 9f, 12, 48.0, 1, 0, 1, BULLET, 1.2f, ModSounds.PISTOL),
            new Item.Settings().maxDamage(250).maxCount(1));

    // Webley Mk VI ปืนลูกโม่อังกฤษ
    public static final Item WEBLEY = register("webley",
            s -> new GunItem(s, 10f, 16, 40.0, 1, 0, 1, BULLET, 1.0f, ModSounds.REVOLVER),
            new Item.Settings().maxDamage(220).maxCount(1));

    // Mauser C96 ยิงรัว 2 นัด
    public static final Item C96 = register("c96",
            s -> new GunItem(s, 6f, 8, 48.0, 2, 0.015, 2, BULLET, 1.3f, ModSounds.PISTOL_BURST),
            new Item.Settings().maxDamage(300).maxCount(1));

    // Lee-Enfield SMLE ไรเฟิลลูกเลื่อน
    public static final Item RIFLE = register("rifle",
            s -> new GunItem(s, 14f, 35, 96.0, 1, 0, 1, BULLET, 0.9f, ModSounds.RIFLE),
            new Item.Settings().maxDamage(400).maxCount(1));

    // Gewehr 98 ไรเฟิลเยอรมัน
    public static final Item GEWEHR98 = register("gewehr98",
            s -> new GunItem(s, 15f, 38, 96.0, 1, 0, 1, BULLET, 0.9f, ModSounds.RIFLE),
            new Item.Settings().maxDamage(400).maxCount(1));

    // Springfield M1903 ไรเฟิลอเมริกัน
    public static final Item M1903 = register("m1903",
            s -> new GunItem(s, 15f, 34, 100.0, 1, 0, 1, BULLET, 0.9f, ModSounds.RIFLE),
            new Item.Settings().maxDamage(400).maxCount(1));

    // Mosin-Nagant ไรเฟิลรัสเซีย ดาเมจสูงสุด ยิงช้าสุด
    public static final Item MOSIN = register("mosin",
            s -> new GunItem(s, 16f, 40, 100.0, 1, 0, 1, BULLET, 0.85f, ModSounds.RIFLE),
            new Item.Settings().maxDamage(400).maxCount(1));

    // MP 18 ปืนกลมือ ยิงรัว 3 นัด
    public static final Item MP18 = register("mp18",
            s -> new GunItem(s, 5f, 14, 40.0, 3, 0.03, 3, BULLET, 1.2f, ModSounds.SMG),
            new Item.Settings().maxDamage(350).maxCount(1));

    // Chauchat ปืนกลเบา ยิงรัว 4 นัด
    public static final Item CHAUCHAT = register("chauchat",
            s -> new GunItem(s, 5f, 20, 56.0, 4, 0.05, 4, BULLET, 0.9f, ModSounds.MG4),
            new Item.Settings().maxDamage(350).maxCount(1));

    // Lewis Gun ปืนกลเบา ยิงรัว 6 นัด
    public static final Item LEWIS = register("lewis",
            s -> new GunItem(s, 5f, 26, 64.0, 6, 0.04, 6, BULLET, 0.8f, ModSounds.MG6),
            new Item.Settings().maxDamage(500).maxCount(1));

    // Maxim MG08 ปืนกลหนัก ยิงรัว 8 นัด
    public static final Item MAXIM = register("maxim",
            s -> new GunItem(s, 5f, 30, 64.0, 8, 0.035, 8, BULLET, 0.7f, ModSounds.MG8),
            new Item.Settings().maxDamage(800).maxCount(1));

    // Winchester M1897 ลูกซองสนามเพลาะ ลูกปราย 7 เม็ด
    public static final Item TRENCH = register("trench",
            s -> new GunItem(s, 3.5f, 32, 20.0, 7, 0.09, 1, SHELL, 0.6f, ModSounds.SHOTGUN),
            new Item.Settings().maxDamage(300).maxCount(1));

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(GunMod.MOD_ID, name));
        return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(e -> {
            e.add(PISTOL);
            e.add(M1911);
            e.add(WEBLEY);
            e.add(C96);
            e.add(RIFLE);
            e.add(GEWEHR98);
            e.add(M1903);
            e.add(MOSIN);
            e.add(MP18);
            e.add(CHAUCHAT);
            e.add(LEWIS);
            e.add(MAXIM);
            e.add(TRENCH);
            e.add(BULLET);
            e.add(SHELL);
        });
    }
}
