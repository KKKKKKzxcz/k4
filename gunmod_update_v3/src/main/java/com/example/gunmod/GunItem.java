package com.example.gunmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class GunItem extends Item {
    private final float damage;      // ดาเมจต่อกระสุน 1 เม็ด (2 = 1 หัวใจ)
    private final int cooldown;      // ดีเลย์ก่อนยิงนัดต่อไป (tick, 20 = 1 วินาที)
    private final double range;      // ระยะยิง (บล็อก)
    private final int rays;          // จำนวนกระสุนต่อการยิง 1 ครั้ง (ปืนกล = ยิงรัว, ลูกซอง = ลูกปราย)
    private final double spread;     // ความกระจายของกระสุน
    private final int ammoPerUse;    // กระสุนที่ใช้ต่อการยิง 1 ครั้ง
    private final Item ammo;         // ชนิดกระสุน
    private final float pitch;       // ระดับเสียงปืน (ต่ำ = เสียงหนัก)
    private final SoundEvent shotSound; // เสียงยิงของปืนกระบอกนี้

    public GunItem(Settings settings, float damage, int cooldown, double range,
                   int rays, double spread, int ammoPerUse, Item ammo, float pitch, SoundEvent shotSound) {
        super(settings);
        this.damage = damage;
        this.cooldown = cooldown;
        this.range = range;
        this.rays = rays;
        this.spread = spread;
        this.ammoPerUse = ammoPerUse;
        this.ammo = ammo;
        this.pitch = pitch;
        this.shotSound = shotSound;
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack gun = player.getStackInHand(hand);
        boolean creative = player.isCreative();

        if (!creative && countAmmo(player) < ammoPerUse) {
            if (!world.isClient) {
                world.playSound(null, player.getBlockPos(), ModSounds.EMPTY,
                        SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
            return ActionResult.FAIL;
        }

        if (world instanceof ServerWorld sw) {
            Vec3d start = player.getEyePos();
            Vec3d baseDir = player.getRotationVec(1.0f);
            Map<Entity, Float> totalDamage = new HashMap<>();

            for (int i = 0; i < rays; i++) {
                Vec3d dir = baseDir;
                if (spread > 0) {
                    dir = baseDir.add(
                            (player.getRandom().nextDouble() - 0.5) * 2 * spread,
                            (player.getRandom().nextDouble() - 0.5) * 2 * spread,
                            (player.getRandom().nextDouble() - 0.5) * 2 * spread).normalize();
                }
                Vec3d end = start.add(dir.multiply(range));

                BlockHitResult blockHit = sw.raycast(new RaycastContext(start, end,
                        RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
                if (blockHit.getType() != HitResult.Type.MISS) {
                    end = blockHit.getPos();
                }

                Box box = player.getBoundingBox().stretch(dir.multiply(range)).expand(1.0);
                EntityHitResult entityHit = ProjectileUtil.raycast(player, start, end, box,
                        e -> !e.isSpectator() && e.canHit(), start.squaredDistanceTo(end));
                if (entityHit != null) {
                    end = entityHit.getPos();
                    totalDamage.merge(entityHit.getEntity(), damage, Float::sum);
                }

                double dist = start.distanceTo(end);
                for (double d = 1.0; d < dist; d += 0.7) {
                    Vec3d p = start.add(dir.multiply(d));
                    sw.spawnParticles(ParticleTypes.CRIT, p.x, p.y, p.z, 1, 0, 0, 0, 0);
                }
            }

            // รวมดาเมจของกระสุนทุกเม็ดที่โดนเป้าหมายเดียวกันให้เป็นการโจมตีครั้งเดียว
            for (Map.Entry<Entity, Float> entry : totalDamage.entrySet()) {
                entry.getKey().damage(sw, sw.getDamageSources().playerAttack(player), entry.getValue());
            }

            // volume > 1 ทำให้คนที่อยู่ไกลได้ยินเสียงปืนด้วย (ประมาณ 16 x volume บล็อก)
            sw.playSound(null, player.getBlockPos(), shotSound,
                    SoundCategory.PLAYERS, 4.0f, 1.0f + (pitch - 1.0f) * 0.25f);

            if (!creative) {
                consumeAmmo(player, ammoPerUse);
                gun.damage(1, player, LivingEntity.getSlotForHand(hand));
            }
        }

        player.getItemCooldownManager().set(gun, cooldown);
        return ActionResult.SUCCESS;
    }

    private int countAmmo(PlayerEntity player) {
        int total = 0;
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.isOf(ammo)) total += s.getCount();
        }
        return total;
    }

    private void consumeAmmo(PlayerEntity player, int amount) {
        for (int i = 0; i < player.getInventory().size() && amount > 0; i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.isOf(ammo)) {
                int take = Math.min(amount, s.getCount());
                s.decrement(take);
                amount -= take;
            }
        }
    }
}
