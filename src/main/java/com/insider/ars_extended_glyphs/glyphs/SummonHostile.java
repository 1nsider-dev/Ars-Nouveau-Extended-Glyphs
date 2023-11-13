package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class SummonHostile extends AbstractEffect {

    public static SummonHostile INSTANCE = new SummonHostile(prefix("glyph_summonhostile"), "Summon Hostile");

    public SummonHostile(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 150;
    }

    private final EntityType<?>[] entities = {
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.ENDERMAN,
            EntityType.BAT,
            EntityType.SLIME,
            EntityType.WITCH,
    };
    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolve(rayTraceResult, world, shooter, spellStats, spellContext, resolver);

        if (shooter instanceof Player){
            Vec3 vector3d = safelyGetHitPos(rayTraceResult);
            BlockPos pos = new BlockPos(vector3d);

            for (int i = 0; i < (3+spellStats.getAmpMultiplier()); i++) {
                BlockPos blockpos = pos.offset(-2 + shooter.getRandom().nextInt(5), 1, -2 + shooter.getRandom().nextInt(5));


                EntityType<?> type = spellStats.getBuffCount(AugmentExtreme.INSTANCE)>0 ? EntityType.CREEPER : entities[shooter.getRandom().nextInt(entities.length)];
                type.spawn((ServerLevel) world, shooter.getItemInHand(InteractionHand.MAIN_HAND), (Player)shooter, blockpos, MobSpawnType.MOB_SUMMONED, false, false);
            }
            world.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.NEUTRAL, 1.0f, 0.8f);
        }
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE, AugmentExtreme.INSTANCE);
    }

    @Override
    public int getAugmentLimit(ResourceLocation augmentTag) {
        return (augmentTag==AugmentAmplify.INSTANCE.getRegistryName() || augmentTag==AugmentDampen.INSTANCE.getRegistryName()) ? 2 : 1;
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
