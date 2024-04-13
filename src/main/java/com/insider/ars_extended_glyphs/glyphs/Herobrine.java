package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.KickCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class Herobrine extends AbstractEffect {

    public static Herobrine INSTANCE = new Herobrine(prefix("glyph_herobrine"), "Herobrine");
    private boolean doStuff = true;
    public Herobrine(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 250;
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolve(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
        if (!world.isClientSide() && doStuff && (shooter instanceof ServerPlayer ent)) {
            if (ent.isRemoved() || ent.getHealth() <= 0)
                return;

            ent.connection.disconnect(Component.translatable("aeg.message.herobrine"));
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

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        builder.comment("Should this glyph kick players? (aka. should it do anything)").define("herobrine_glyph", doStuff);
    }
}
