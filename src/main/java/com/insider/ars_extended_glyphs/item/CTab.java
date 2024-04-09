package com.insider.ars_extended_glyphs.item;

import com.insider.ars_extended_glyphs.Main;
import com.insider.ars_extended_glyphs.registry.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.insider.ars_extended_glyphs.registry.ModRegistry.*;

public class CTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            Main.MODID);

    public static RegistryObject<CreativeModeTab> ARS_EXT_TAB = CREATIVE_MODE_TABS.register("ars_ext_tab", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(MAGESTONE.get()))
                    .title(Component.translatable("creativemodetab.ars_ext_tab")).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}