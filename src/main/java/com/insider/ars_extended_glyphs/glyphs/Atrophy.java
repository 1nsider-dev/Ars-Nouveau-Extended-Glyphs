package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class Atrophy extends AbstractEffect {

    public static Atrophy INSTANCE = new Atrophy(prefix("glyph_atrophy"), "Atrophy");

    public Atrophy(ResourceLocation tag, String description) {
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

            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, GENERIC_INT.get()+((int)spellStats.getDurationMultiplier()*200),
                    (int) Math.max(0,spellStats.getAmpMultiplier())), shooter);
            world.playSound(null, entity.blockPosition(), SoundEvents.WITHER_SKELETON_HURT, SoundSource.NEUTRAL, 1.0f, 1.0f);
        }
    }


    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentExtendTime.INSTANCE, AugmentAmplify.INSTANCE);
    }
    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addGenericInt(builder, 400, "Duration of weakness", "base_weakness");
    }
}
