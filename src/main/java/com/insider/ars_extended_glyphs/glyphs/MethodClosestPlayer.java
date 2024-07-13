package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketANEffect;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class MethodClosestPlayer extends AbstractCastMethod {
    public static MethodClosestPlayer INSTANCE = new MethodClosestPlayer(prefix("glyph_closestplayer"), "Closest Player");

    public MethodClosestPlayer(ResourceLocation tag, String description) {
        super(tag, description);
    }

    private Player getNearestPlayer(LivingEntity requester, EntityGetter getter, double pDistance, @javax.annotation.Nullable Predicate<Entity> pPredicate) {
        double pX = requester.getX();
        double pY = requester.getY();
        double pZ = requester.getZ();

        double d0 = -1.0D;
        Player player = null;

        for(Player player1 : getter.players()) {
            if (player1 != requester && (pPredicate == null || pPredicate.test(player1))) {
                double d1 = player1.distanceToSqr(pX, pY, pZ);
                if ((pDistance < 0.0D || d1 < pDistance * pDistance) && (d0 == -1.0D || d1 < d0)) {
                    d0 = d1;
                    player = player1;
                }
            }
        }

        return player;
    }
    @Override
    public CastResolveType onCast(@Nullable ItemStack stack, LivingEntity playerEntity, Level world, SpellStats spellStats, SpellContext context, SpellResolver resolver) {
        Player nearestPlayer = getNearestPlayer(playerEntity, world, 16*(spellStats.getAoeMultiplier()+1), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        if (nearestPlayer!=null) {
            resolver.onResolveEffect(playerEntity.getCommandSenderWorld(), new EntityHitResult(nearestPlayer));
            Networking.sendToNearby(playerEntity.level(), playerEntity, new PacketANEffect(PacketANEffect.EffectType.TIMED_HELIX, playerEntity.blockPosition()));
            return CastResolveType.SUCCESS;
        }else{
            return CastResolveType.FAILURE;
        }
    }

    @Override
    public CastResolveType onCastOnBlock(UseOnContext context, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return onCast(null, context.getPlayer(), context.getLevel(), spellStats, spellContext, resolver);
    }

    @Override
    public CastResolveType onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return onCast(null, caster, caster.level(), spellStats, spellContext, resolver);
    }

    @Override
    public CastResolveType onCastOnEntity(@Nullable ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return onCast(stack, caster, caster.level(), spellStats, spellContext, resolver);
    }

    @Override
    public int getDefaultManaCost() {
        return 100;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAOE.INSTANCE);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    public SpellTier getConfigTier() {
        return SpellTier.TWO;
    }
}
