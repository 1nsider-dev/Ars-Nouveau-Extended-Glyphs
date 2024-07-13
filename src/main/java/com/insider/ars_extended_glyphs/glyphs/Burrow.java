package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketWarpPosition;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class Burrow extends AbstractEffect {

    public static Burrow INSTANCE = new Burrow(prefix("glyph_burrow"), "Burrow");

    public Burrow(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 300;
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolveEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver);

        if (rayTraceResult.getEntity() instanceof LivingEntity entity) {
            if (entity.isRemoved() || entity.getHealth() <= 0)
                return;
            warpEntity(entity, new Vec3(entity.getX(), entity.getY() - 2 - spellStats.getBuffCount(AugmentAmplify.INSTANCE) + spellStats.getBuffCount(AugmentDampen.INSTANCE), entity.getZ()));
        }
    }
    public static void warpEntity(Entity entity, Vec3 warpPos) {
        if (entity == null) return;
        Level world = entity.level;
        if (entity instanceof LivingEntity living){
            Event event = ForgeEventFactory.onEnderTeleport(living, warpPos.x, warpPos.y, warpPos.x);
            if (event.isCanceled()) return;
        }
        ((ServerLevel) world).sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(),
                4, (world.random.nextDouble() - 0.5D) * 2.0D, -world.random.nextDouble(), (world.random.nextDouble() - 0.5D) * 2.0D, 0.1f);

        entity.teleportTo(warpPos.x, warpPos.y, warpPos.z);
        Networking.sendToNearby(world, entity, new PacketWarpPosition(entity.getId(), entity.getX(), entity.getY(), entity.getZ(), entity.getXRot(), entity.getYRot()));
        world.playSound(null, entity.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE);
    }

    @Override
    public int getAugmentLimit(ResourceLocation augmentTag) {
        if (augmentTag == AugmentAmplify.INSTANCE.getRegistryName()){
            return 1;
        } else if (augmentTag == AugmentDampen.INSTANCE.getRegistryName()) {
            return 3;
        }
        return super.getAugmentLimit(augmentTag);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }
    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ELEMENTAL_EARTH);
    }
}
