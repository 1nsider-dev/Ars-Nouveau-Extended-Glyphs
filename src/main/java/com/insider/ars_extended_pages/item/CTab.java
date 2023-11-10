package com.insider.ars_extended_pages.item;

import com.insider.ars_extended_pages.ExtendedPages;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.insider.ars_extended_pages.registry.ModRegistry.*;

public class CTab extends CreativeModeTab {
    public static final CTab instance = new CTab(CreativeModeTab.TABS.length, ExtendedPages.MODID);
    private CTab(int index, String label) {
        super(index, label);
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(MAGESTONE.get());
    }
}
