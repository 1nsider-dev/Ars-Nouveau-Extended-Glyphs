package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class SummonWildlife extends AbstractEffect {

    public static SummonWildlife INSTANCE = new SummonWildlife(prefix("glyph_summonwildlife"), "Summon Wildlife");

    public SummonWildlife(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 250;
    }

    private final EntityType<?>[] entities = {
            EntityType.SHEEP,
            EntityType.SHEEP,
            EntityType.COW,
            EntityType.COW,
            EntityType.PIG,
            EntityType.PIG,
            EntityType.BAT,
            EntityType.CHICKEN,
            EntityType.CHICKEN,
    }; // dont mind how ugly this is
    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!canSummon(shooter))
            return;

        if (shooter instanceof Player){
            Vec3 vector3d = safelyGetHitPos(rayTraceResult);
            BlockPos pos = new BlockPos(new Vec3i((int) vector3d.x, (int) vector3d.y, (int) vector3d.z));
            for (int i = 0; i < (3+spellStats.getAmpMultiplier()); i++) {
                BlockPos blockpos = pos.offset(-2 + shooter.getRandom().nextInt(5), 2, -2 + shooter.getRandom().nextInt(5));

                EntityType<?> type = entities[shooter.getRandom().nextInt(entities.length)];
                type.spawn((ServerLevel) world, shooter.getItemInHand(InteractionHand.MAIN_HAND), (Player)shooter, blockpos, MobSpawnType.COMMAND, false, false);
            }
        }
        shooter.addEffect(new MobEffectInstance(ModPotions.SUMMONING_SICKNESS_EFFECT.get(), (int) (600+200*spellStats.getAmpMultiplier())));
    }


    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE);
    }
    @Override
    public int getAugmentLimit(ResourceLocation augmentTag) {
        return 3;
    }
    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }
    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION);
    }
}
