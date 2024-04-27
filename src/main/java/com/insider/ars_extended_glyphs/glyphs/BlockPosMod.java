package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class BlockPosMod extends AbstractEffect {
    public static BlockPosMod INSTANCE = new BlockPosMod(prefix("glyph_above"), "Above");

    public BlockPosMod(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        spellContext.setCanceled(true);
        SpellContext newContext = resolver.spellContext.clone().withSpell(spellContext.getRemainingSpell());
        BlockPos pos = rayTraceResult.getBlockPos();
        resolver.getNewResolver(newContext.clone()).onResolveEffect(world, new BlockHitResult(new Vec3(pos.getX(), pos.getY()+1, pos.getZ()), Direction.UP, new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()), false));
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        spellContext.setCanceled(true);
        SpellContext newContext = resolver.spellContext.clone().withSpell(spellContext.getRemainingSpell());
        BlockPos pos = rayTraceResult.getEntity().getOnPos();
        resolver.getNewResolver(newContext.clone()).onResolveEffect(world, new BlockHitResult(new Vec3(pos.getX(), pos.getY()+1, pos.getZ()), Direction.UP, new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()), false));
    }

    @Override
    protected int getDefaultManaCost() {
        return 0;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf();
    }
}
