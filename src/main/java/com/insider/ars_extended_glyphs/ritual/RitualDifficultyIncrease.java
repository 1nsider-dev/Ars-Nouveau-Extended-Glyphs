package com.insider.ars_extended_glyphs.ritual;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.insider.ars_extended_glyphs.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class RitualDifficultyIncrease extends AbstractRitual {
    @Override
    protected void tick() {
        if (getWorld() instanceof ServerLevel world){
            world.playSound(null, tile.getBlockPos(), SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.NEUTRAL, 1.0f, 1.0f);
            if (world.getGameTime() % 10 == 0) {
                incrementProgress();
                if (getProgress() >= 5) {
                    Difficulty d = world.getDifficulty();
                    if (didConsumeItem(Items.FEATHER)) {
                        if (d==Difficulty.EASY){
                            BlockPos pos = tile.getBlockPos();
                            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(world);
                            lightningbolt.setDamage(1000);
                            lightningbolt.moveTo(Vec3.atBottomCenterOf(world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 12, false).getOnPos()));
                            world.addFreshEntity(lightningbolt);
                            setFinished();
                            return;
                        }
                        switch (d) {
                            case NORMAL -> world.getServer().setDifficulty(Difficulty.EASY, false);
                            case HARD -> world.getServer().setDifficulty(Difficulty.NORMAL, false);
                        }
                        world.playSound(null, tile.getBlockPos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.NEUTRAL, 1.0f, 1.0f);
                    } else {
                        switch (d) {
                            case EASY -> world.getServer().setDifficulty(Difficulty.NORMAL, false);
                            case NORMAL -> world.getServer().setDifficulty(Difficulty.HARD, false);
                        }
                        world.playSound(null, tile.getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.NEUTRAL, 1.0f, 1.0f);
                    }
                    ParticleUtil.spawnPoof(world, tile.getBlockPos());
                    setFinished();
                }
            }
        }
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(Main.MODID, "ritual_difficulty_increase");
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        return stack.getItem() == Items.FEATHER;
    }
}
