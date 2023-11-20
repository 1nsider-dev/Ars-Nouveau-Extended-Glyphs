package com.insider.ars_extended_glyphs.item;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.insider.ars_extended_glyphs.registry.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Tablet extends Item implements ICapabilityProvider {
    public Tablet(SpellSchool sch) {
        super(new Item.Properties().stacksTo(1).tab(CTab.instance));
        school = sch;
    }
    private final SpellSchool school;
    public SpellSchool getSchool() {
        return school;
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        return ModRegistry.UNIQUE_RARITY;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
        tooltip2.add(Component.translatable("tooltip.aeg.tablet"));
        if (!stack.isEnchanted()) {
            tooltip2.add(Component.translatable("tooltip.aeg.tablet_warning").withStyle(ChatFormatting.RED));
        }
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return null;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (!pStack.isEnchanted()) {
            pStack.enchant(Enchantments.BINDING_CURSE, 1);
        }
    }
}
