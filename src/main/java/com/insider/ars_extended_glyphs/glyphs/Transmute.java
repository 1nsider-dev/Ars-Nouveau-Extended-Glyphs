package com.insider.ars_extended_glyphs.glyphs;

import com.hollingsworth.arsnouveau.api.item.inv.ExtractedStack;
import com.hollingsworth.arsnouveau.api.item.inv.InventoryManager;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentFortune;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.insider.ars_extended_glyphs.Main.prefix;

public class Transmute extends AbstractEffect {

    public static Transmute INSTANCE = new Transmute(prefix("glyph_transmute"), "Transmute");

    public Transmute(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return 100;
    }

    private final Item[] potItems = {
            Items.DIAMOND,
            Items.COAL,
            Items.IRON_INGOT,
            Items.IRON_NUGGET,
            Items.GOLD_INGOT,
            Items.GOLD_NUGGET,
            Items.COPPER_INGOT,
            Items.GLOWSTONE_DUST,
            Items.BONE,
            Items.DIRT,
            Items.GRASS_BLOCK,
            Items.GUNPOWDER
    };
    private final Item[] extremeItems = {
            Items.DIAMOND,
            Items.IRON_INGOT,
            Items.GOLD_INGOT,
            Items.EMERALD,
            Items.COPPER_INGOT
    };
    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolve(rayTraceResult, world, shooter, spellStats, spellContext, resolver);

        if (shooter instanceof Player p) {
            InventoryManager manager = spellContext.getCaster().getInvManager();
            ExtractedStack ext = manager.extractItem(i -> {
                return i.is(ItemsRegistry.SOURCE_GEM.asItem());
            }, 1);
            ItemStack stack = ext.getStack();

            if (stack.getCount() > 0) {
                stack.shrink(1);

                var rng = p.getRandom();
                var itemCount = rng.nextInt(3 + spellStats.getBuffCount(AugmentFortune.INSTANCE)) + 1;
                ItemStack item;

                if (spellStats.getBuffCount(AugmentExtreme.INSTANCE)>0) {
                    item = new ItemStack(extremeItems[rng.nextInt(extremeItems.length)], itemCount);
                }else{
                    item = new ItemStack(potItems[rng.nextInt(potItems.length)], itemCount);
                }

                p.getInventory().add(item);
                world.playSound(null, p.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0f, 1.0f);

            }else{
                spellContext.setCanceled(true);
            }
        }
    }


    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentFortune.INSTANCE, AugmentExtreme.INSTANCE);
    }

    @Override
    public int getAugmentLimit(ResourceLocation augmentTag) {
        return (augmentTag==AugmentFortune.INSTANCE.getRegistryName()) ? 2 : 1;
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
