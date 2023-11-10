package com.insider.ars_extended_pages.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;

import static com.insider.ars_extended_pages.ExtendedPages.prefix;

public class SummonWildlife extends AbstractEffect {

    public static SummonWildlife INSTANCE = new SummonWildlife(prefix("glyph_summonwildlife"), "Summon Wildlife");

    public SummonWildlife(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 150;
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
    };
    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolve(rayTraceResult, world, shooter, spellStats, spellContext, resolver);

        if (shooter instanceof Player){
            Vec3 vector3d = safelyGetHitPos(rayTraceResult);
            BlockPos pos = new BlockPos(vector3d);
            for (int i = 0; i < (3+spellStats.getAmpMultiplier()); i++) {
                BlockPos blockpos = pos.offset(-2 + shooter.getRandom().nextInt(5), 2, -2 + shooter.getRandom().nextInt(5));

                EntityType<?> type = entities[shooter.getRandom().nextInt(entities.length)];
                type.spawn((ServerLevel) world, shooter.getItemInHand(InteractionHand.MAIN_HAND), (Player)shooter, blockpos, MobSpawnType.MOB_SUMMONED, false, false);
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
        return setOf(SpellSchools.CONJURATION);
    }
}
