package com.insider.ars_extended_glyphs.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.insider.ars_extended_glyphs.registry.ModRegistry.*;

public class CTab extends CreativeModeTab {
    public static final CTab instance = new CTab(CreativeModeTab.TABS.length, com.insider.ars_extended_glyphs.Main.MODID);
    private CTab(int index, String label) {
        super(index, label);
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(MAGESTONE.get());
    }
}
