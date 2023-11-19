package com.insider.ars_extended_glyphs;

import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.insider.ars_extended_glyphs.item.Tablet;
import net.minecraft.world.entity.player.Player;
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
            if (tablet != null) {
                if (tablet.stack().getItem() instanceof Tablet tab && ev.spell.recipe.stream().anyMatch(tab.getSchool()::isPartOfSchool)) {
                    ev.spell.addDiscount(50);
                }
            }
        }
    }
}
