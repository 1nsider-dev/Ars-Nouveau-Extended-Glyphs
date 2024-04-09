package com.insider.ars_extended_glyphs;

import com.hollingsworth.arsnouveau.api.event.SpellCostCalcEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.insider.ars_extended_glyphs.item.Tablet;
import com.insider.ars_extended_glyphs.item.TabletFragment;
import com.insider.ars_extended_glyphs.registry.ModRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.stream.IntStream;


@Mod.EventBusSubscriber(modid = Main.MODID)
public class Events {
    private static boolean hasCurio(Entity entity, Level level, Item curio){
        if (!level.isClientSide && entity instanceof Player) {
            Optional<IItemHandlerModifiable> curios = CuriosUtil.getAllWornItems((LivingEntity) entity).resolve();
            if (curios.isPresent()) {
                IItemHandlerModifiable items = curios.get();
                return IntStream.range(0, items.getSlots()).anyMatch(i -> items.getStackInSlot(i).getItem()==curio);
            }
        }
        return false;
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void discountGlyph(SpellCostCalcEvent ev){
        LivingEntity entity = ev.context.getUnwrappedCaster();
        Level level = entity.level();
        if (!level.isClientSide && entity instanceof Player) {
            Optional<IItemHandlerModifiable> curios = CuriosUtil.getAllWornItems((LivingEntity) entity).resolve();
            if (curios.isPresent()) {
                IItemHandlerModifiable items = curios.get();
                IntStream.range(0, items.getSlots()).forEach(i -> {
                    if (items.getStackInSlot(i).getItem() instanceof Tablet tab && ev.context.getSpell().recipe.stream().anyMatch(tab.getSchool()::isPartOfSchool)){
                        ev.currentCost -= 50;
                    }
                });
            }
        }
    }
    @SubscribeEvent()
    public static void fallDamage(LivingFallEvent ev){
        if (ev.getEntity() instanceof Player player && !player.getCommandSenderWorld().isClientSide()) {
            if (hasCurio(player, player.level(), ModRegistry.AIR_TABLET_FRAG.get())) {
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
            DamageSources srcTbl = player.damageSources();
            if (hasCurio(player, player.level(), ModRegistry.FIRE_TABLET_FRAG.get()) &&
                    (ev.getSource() == srcTbl.onFire() || ev.getSource() == srcTbl.inFire() || ev.getSource() == srcTbl.lava())){
                int pwr = (int) ev.getAmount()*25;
                CapabilityRegistry.getMana(player).ifPresent(mana -> {
                    if (mana.getCurrentMana() > pwr) {
                        mana.removeMana(pwr);
                        ev.setCanceled(true);
                    }
                });
            } else if (hasCurio(player, player.level(), ModRegistry.EARTH_TABLET_FRAG.get()) &&
                    (ev.getSource() == srcTbl.inWall() || ev.getSource() == srcTbl.flyIntoWall() || ev.getSource() == srcTbl.hotFloor())){
                int pwr = (int) ev.getAmount()*15;
                CapabilityRegistry.getMana(player).ifPresent(mana -> {
                    if (mana.getCurrentMana() > pwr) {
                        mana.removeMana(pwr);
                        ev.setCanceled(true);
                    }
                });
            } else if (hasCurio(player, player.level(), ModRegistry.WATER_TABLET_FRAG.get()) &&
                    (ev.getSource() == srcTbl.drown() || ev.getSource() == srcTbl.dragonBreath())){
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
