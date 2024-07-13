package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class Absorption extends AbstractEffect {

    private final float maxAbsorp = 10;
    public static Absorption INSTANCE = new Absorption(prefix("glyph_absorption"), "Absorption");

    public Absorption(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 250;
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolveEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver);

        if (rayTraceResult.getEntity() instanceof LivingEntity entity) {
            if (entity.isRemoved() || entity.getHealth() <= 0)
                return;

            float healVal = (float) (GENERIC_DOUBLE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier());
            if(entity instanceof Player player){
                float currAP = player.getAbsorptionAmount();
                float toSetAP = currAP+healVal;
                if (toSetAP > maxAbsorp) {
                    toSetAP = Math.max(currAP, maxAbsorp);
                }
                player.setAbsorptionAmount(toSetAP);
            }

        }
    }


    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE);
    }
    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ABJURATION);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addGenericDouble(builder, 3.0, "Base add amount", "base_absorption");
        addAmpConfig(builder, 1.5);
    }
}
