package com.insider.ars_extended_glyphs.item;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.sound.ConfiguredSpellSound;
import com.hollingsworth.arsnouveau.api.sound.SpellSound;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import com.hollingsworth.arsnouveau.common.crafting.recipes.IDyeable;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PassiveCast extends Item implements ICasterTool {
    private int speed = 5;
    public PassiveCast(Properties pProperties, int spellSpeed) {
        super(pProperties);
        speed = spellSpeed;
    }
    private long getTickSpeed(ItemStack pStack){
        CompoundTag tag = pStack.getOrCreateTag();
        long tSpeed = tag.getInt("invTickSpeed");
        return (speed + Math.max(-9, tSpeed) * 20L);
    }
    private boolean getEnabled(ItemStack pStack){
        CompoundTag tag = pStack.getOrCreateTag();
        return tag.getBoolean("canTick");
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (getEnabled(pStack) && (pEntity instanceof LivingEntity ent)) {
            ISpellCaster caster = getSpellCaster(pStack);
            Spell spell = caster.getSpell();
//            CompoundTag tag = pStack.getOrCreateTag();
//            int curTick = tag.getInt("currentTick");
//
//            if (curTick >= getTickSpeed(pStack)){
//                tag.putInt("currentTick", 0);
//                caster.castSpell(pLevel, ent, InteractionHand.MAIN_HAND, Component.empty(), spell);
//            }else{
//                tag.putInt("currentTick", curTick+1);
//            }
            if (pLevel.getGameTime() % getTickSpeed(pStack) == 0){
                caster.castSpell(pLevel, ent, InteractionHand.MAIN_HAND, Component.empty(), spell);
            }
        }
    }

    @Override
    public boolean setSpell(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        ArrayList<AbstractSpellPart> recipe = new ArrayList<>();
        recipe.add(MethodSelf.INSTANCE);
        CompoundTag tag = stack.getOrCreateTag();
        int newSpeed = 0;
        boolean finishedFirst = false;
        for (AbstractSpellPart part: spell.recipe) {
            if (!finishedFirst && part == AugmentExtendTime.INSTANCE) {
                newSpeed++;
            } else if (!finishedFirst && part == AugmentDurationDown.INSTANCE) {
                newSpeed--;
            } else {
                finishedFirst = true;
                recipe.add(part);
            }
        }
        tag.putInt("invTickSpeed", newSpeed);
        tag.putInt("color", player.getRandom().nextInt());
        spell.recipe = recipe;
        spell.sound = caster.getSound(caster.getCurrentSlot());
        spell.color = caster.getColor(caster.getCurrentSlot());
        return ICasterTool.super.setSpell(caster, player, hand, stack, spell);
    }

    @Override
    public boolean isScribedSpellValid(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        return spell.recipe.stream().noneMatch(s -> s instanceof AbstractCastMethod);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Rate: " + (getTickSpeed(pStack)/20) + " second(s)")
                .withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
        pTooltipComponents.add(Component.literal("Status: " + (getEnabled(pStack) ? "Enabled" : "Disabled"))
                .withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.getKey().getValue())) {
            pTooltipComponents.add(Component.translatable("aeg.passive_caster.desc"));
            pTooltipComponents.add(Component.translatable("aeg.passive_caster.desc2"));
        }else{
            pTooltipComponents.add(Component.translatable("tooltip.aeg.await"));
        }
        pTooltipComponents.add(Component.empty());
        getInformation(pStack,pLevel,pTooltipComponents,pIsAdvanced);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        CompoundTag tag = pPlayer.getItemInHand(pUsedHand).getOrCreateTag();
        tag.putBoolean("canTick", !tag.getBoolean("canTick"));
        PortUtil.sendMessageNoSpam(pPlayer, Component.translatable(tag.getBoolean("canTick") ? "aeg.passive_caster_on" : "aeg.passive_caster_off")
                .withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
