package com.insider.ars_extended_glyphs;

import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import com.insider.ars_extended_glyphs.item.Tablet;
import com.insider.ars_extended_glyphs.item.TabletFragment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;


@Mod.EventBusSubscriber(modid = Main.MODID)
public class Events {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void discountGlyph(SpellCastEvent ev){
        if (!ev.getWorld().isClientSide && ev.getEntity() instanceof Player player) {
            SlotResult tablet = CuriosApi.getCuriosHelper().findFirstCurio(player, c -> (c.getItem() instanceof Tablet)).orElse(null);
            if (tablet != null && tablet.stack().getItem() instanceof Tablet tab && ev.spell.recipe.stream().anyMatch(tab.getSchool()::isPartOfSchool)) {
                ev.spell.addDiscount(50);
            }
        }
    }
    @SubscribeEvent()
    public static void fallDamage(LivingFallEvent ev){
        if (ev.getEntity() instanceof Player player && !player.getCommandSenderWorld().isClientSide()) {
            SlotResult tablet = CuriosApi.getCuriosHelper().findFirstCurio(player, c -> (c.getItem() instanceof TabletFragment)).orElse(null);
            if (tablet != null && tablet.stack().getItem() instanceof TabletFragment tab && tab.getSchool() == SpellSchools.ELEMENTAL_AIR) {
                int dist = (int) ev.getDistance()*3;
                CapabilityRegistry.getMana(player).ifPresent(mana -> {
                    if (mana.getCurrentMana() > dist) {
                        mana.removeMana(dist);
                        ev.setDamageMultiplier(0);
                    }
                });
            }
        }
    }
    @SubscribeEvent()
    public static void fireDamage(LivingHurtEvent ev){
        if (ev.getEntity() instanceof Player player && !player.getCommandSenderWorld().isClientSide()){
            SlotResult tablet = CuriosApi.getCuriosHelper().findFirstCurio(player, c -> (c.getItem() instanceof TabletFragment)).orElse(null);
            if (tablet != null && tablet.stack().getItem() instanceof TabletFragment tab) {
                if (tab.getSchool() == SpellSchools.ELEMENTAL_FIRE &&
                        (ev.getSource() == DamageSource.ON_FIRE || ev.getSource() == DamageSource.IN_FIRE || ev.getSource() == DamageSource.LAVA)){
                    int pwr = (int) ev.getAmount()*25;
                    CapabilityRegistry.getMana(player).ifPresent(mana -> {
                        if (mana.getCurrentMana() > pwr) {
                            mana.removeMana(pwr);
                            ev.setCanceled(true);
                        }
                    });
                } else if (tab.getSchool() == SpellSchools.ELEMENTAL_EARTH &&
                        (ev.getSource() == DamageSource.IN_WALL || ev.getSource() == DamageSource.FLY_INTO_WALL || ev.getSource() == DamageSource.HOT_FLOOR)){
                    int pwr = (int) ev.getAmount()*15;
                    CapabilityRegistry.getMana(player).ifPresent(mana -> {
                        if (mana.getCurrentMana() > pwr) {
                            mana.removeMana(pwr);
                            ev.setCanceled(true);
                        }
                    });
                } else if (tab.getSchool() == SpellSchools.ELEMENTAL_WATER &&
                        (ev.getSource() == DamageSource.DROWN || ev.getSource() == DamageSource.DRAGON_BREATH)){
                    int pwr = 20;
                    CapabilityRegistry.getMana(player).ifPresent(mana -> {
                        if (mana.getCurrentMana() > pwr) {
                            mana.removeMana(pwr);
                            ev.setCanceled(true);
                        }
                    });
                }
            }
        }
    }
}
