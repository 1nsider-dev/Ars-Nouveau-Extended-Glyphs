package com.insider.ars_extended_glyphs.sound;

import com.insider.ars_extended_glyphs.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Main.MODID);

    public static RegistryObject<SoundEvent> BROKEN_RECORD = registerSound("music_disc_record");
    private static RegistryObject<SoundEvent> registerSound(String name) {
        ResourceLocation id = new ResourceLocation(Main.MODID, name);
        return SOUND_EVENTS.register(name, () -> new SoundEvent(id));
    }
    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
