package com.insider.ars_extended_pages.item;

import com.hollingsworth.arsnouveau.common.items.curios.AbstractManaCurio;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RefinedMagestone extends AbstractManaCurio {
    @Override
    public int getMaxManaBoost(ItemStack i) {
        return 5 * i.getCount();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 16;
    }

    @Override
    public Collection<CreativeModeTab> getCreativeTabs() {
        Collection<CreativeModeTab> list = new ArrayList<CreativeModeTab>();
        list.add(CTab.instance);
        return list;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
        tooltip2.add(Component.translatable("tooltip.refined_magestone"));
        tooltip2.add(Component.translatable("tooltip2.refined_magestone"));
    }
}
