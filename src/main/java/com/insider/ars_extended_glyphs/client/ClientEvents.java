package com.insider.ars_extended_glyphs.client;

import com.insider.ars_extended_glyphs.Main;
import com.insider.ars_extended_glyphs.registry.ModRegistry;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack,index) -> {
            if (index == 0){
                int clr = stack.getOrCreateTag().getInt("color");
                if (clr==0) {clr=0xf587ff;}
                return clr;
            } else {
                return -1;
            }
        }, ModRegistry.PASSIVE_CAST);
    }
}
