package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class ReturnHome extends AbstractEffect {

    public static ReturnHome INSTANCE = new ReturnHome(prefix("glyph_returnhome"), "Return to Spawn");

    public ReturnHome(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 200;
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolveEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
        if (rayTraceResult.getEntity() instanceof ServerPlayer player) {
            BlockPos homePos = player.getRespawnPosition();
            float homeYaw = player.getRespawnAngle();
            ServerLevel homeDim = world.getServer().getLevel(player.getRespawnDimension());
            if (homeDim!=null && homePos!=null) {
                BlockPos prevPos = player.blockPosition();
                player.teleportTo(homeDim, homePos.getX()+.5, homePos.getY()+.6D, homePos.getZ()+.5, homeYaw, 0f);
                world.playSound(null, prevPos, SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.3F, 1.0F);
            }
        }
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf();
    }
    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }
    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }
}
