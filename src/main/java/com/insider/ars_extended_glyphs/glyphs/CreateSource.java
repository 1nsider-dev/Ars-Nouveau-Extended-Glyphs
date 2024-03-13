package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.source.ISourceTile;
import com.hollingsworth.arsnouveau.api.source.ISpecialSourceProvider;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.insider.ars_extended_glyphs.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class CreateSource extends AbstractEffect {

    public static CreateSource INSTANCE = new CreateSource(prefix("glyph_createsource"), "Conjure Source");

    public CreateSource(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 90;
    }
    private void resolve(BlockPos worldPos, Level world, SpellStats spellStats){
        worldPos = new BlockPos(worldPos.getX(),worldPos.getY()+1,worldPos.getZ());
        List<ISpecialSourceProvider> providers = SourceUtil.canGiveSource(worldPos, world, 6);
        if(!providers.isEmpty()){
            providers.get(0).getSource().addSource((int) ((20+(spellStats.getAmpMultiplier()*10))* GENERIC_INT.get()));
            ParticleUtil.spawnFollowProjectile(world, worldPos, providers.get(0).getCurrentPos());
            world.playSound(null, worldPos, SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.PLAYERS, 0.5f, 1.0f);
        }
    }
    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolveEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver);

        if (rayTraceResult.getEntity() instanceof LivingEntity entity) {
            if (entity.isRemoved() || entity.getHealth() <= 0)
                return;

            resolve(entity.blockPosition(), world, spellStats);
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolveBlock(rayTraceResult, world, shooter, spellStats, spellContext, resolver);

        resolve(rayTraceResult.getBlockPos(), world, spellStats);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE);
    }
    @Override
    public SpellTier defaultTier() {
        return SpellTier.ONE;
    }
    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addGenericInt(builder, 10, "Mana to Source conversion multiplier. (default 10)", "base_source_mult");
    }
}
