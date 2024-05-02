package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class Repair extends AbstractEffect {

    public static Repair INSTANCE = new Repair(prefix("glyph_repair"), "Repair");

    public Repair(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 250;
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolveEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
        if (rayTraceResult.getEntity() instanceof Player entity) {
            if (entity.isRemoved() || entity.getHealth() <= 0)
                return;
            for (ItemStack item : entity.getInventory().items) {
                if (item.isDamaged()) {
                    item.setDamageValue(Math.max(0, item.getDamageValue()-item.getMaxDamage()/8));
                    world.playSound(null, entity.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.5f, 0.9f);
                    return;
                }
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
        return SpellTier.THREE;
    }
    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION);
    }
}
