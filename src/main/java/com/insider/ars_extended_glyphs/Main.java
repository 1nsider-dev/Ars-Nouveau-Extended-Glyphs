package com.insider.ars_extended_glyphs;

import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.insider.ars_extended_glyphs.item.CTab;
import com.insider.ars_extended_glyphs.item.Tablet;
import com.insider.ars_extended_glyphs.registry.ModRegistry;
import com.insider.ars_extended_glyphs.sound.SoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(com.insider.ars_extended_glyphs.Main.MODID)
public class Main
{
    public static final String MODID = "ars_extended_glyphs";

    private static final Logger LOGGER = LogManager.getLogger();

    public Main() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        CTab.register(modbus);
        ModRegistry.registerRegistries(modbus);
        SoundRegistry.register(modbus);
        ArsNouveauRegistry.registerGlyphs();
        modbus.addListener(this::setup);
        modbus.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);

        modbus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){
        if (event.getTab() == CTab.ARS_EXT_TAB.get()) {
            event.accept(ModRegistry.MAGESTONE);
            event.accept(ModRegistry.REFINED_MAGESTONE);
            event.accept(ModRegistry.AMULET_OF_GREATER_MANA_POWER);
            event.accept(ModRegistry.RING_OF_GREATER_DISCOUNT);

            event.accept(ModRegistry.BLANK_TABLET);
            event.accept(ModRegistry.AIR_TABLET);
            event.accept(ModRegistry.FIRE_TABLET);
            event.accept(ModRegistry.WATER_TABLET);
            event.accept(ModRegistry.EARTH_TABLET);
            event.accept(ModRegistry.MANIPULATION_TABLET);
            event.accept(ModRegistry.CONJURATION_TABLET);
            event.accept(ModRegistry.ABJURATION_TABLET);

            event.accept(ModRegistry.BLANK_TABLET_FRAG);
            event.accept(ModRegistry.AIR_TABLET_FRAG);
            event.accept(ModRegistry.FIRE_TABLET_FRAG);
            event.accept(ModRegistry.WATER_TABLET_FRAG);
            event.accept(ModRegistry.EARTH_TABLET_FRAG);

            event.accept(ModRegistry.BROKEN_RECORD);
        }
    }

    public static ResourceLocation prefix(String path){
        return new ResourceLocation(MODID, path);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }
}
