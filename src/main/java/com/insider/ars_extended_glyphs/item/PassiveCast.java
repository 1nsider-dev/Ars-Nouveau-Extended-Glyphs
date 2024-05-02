package com.insider.ars_extended_glyphs.item;

import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.sound.ConfiguredSpellSound;
import com.hollingsworth.arsnouveau.api.sound.SpellSound;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.IWrappedCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import com.hollingsworth.arsnouveau.common.crafting.recipes.IDyeable;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.insider.ars_extended_glyphs.ArsNouveauRegistry;
import com.insider.ars_extended_glyphs.glyphs.Repair;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
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

            // This is a kinda hacky solution, but I also hate that the spell doesn't get cast when looking at Block Entities - so I don't care.
            if (pLevel.getGameTime() % getTickSpeed(pStack) == 0 && pStack.getDamageValue() < pStack.getMaxDamage()-1){
                InteractionHand handIn = InteractionHand.MAIN_HAND;
                if (pLevel.isClientSide) {
                    spell = caster.modifySpellBeforeCasting(pLevel, ent, handIn, spell);
                }

                IWrappedCaster wrappedCaster = ent instanceof Player pCaster ? new PlayerCaster(pCaster) : new LivingCaster(ent);
                Player player = ent instanceof Player thisPlayer ? thisPlayer : ANFakePlayer.getPlayer((ServerLevel) pLevel);
                SpellResolver resolver = caster.getSpellResolver(new SpellContext(pLevel, spell, ent, wrappedCaster, pStack), pLevel, player, handIn);
                if (resolver.onCastOnEntity(pStack, ent, handIn)) {
                    caster.playSound(ent.getOnPos(), pLevel, ent, caster.getCurrentSound(), SoundSource.PLAYERS);
                    if (ent instanceof ServerPlayer serverPlayer){
                        pStack.hurt(1, pLevel.random, serverPlayer);
                    }
                }
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.UNBREAKING;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack pToRepair, ItemStack pRepair) {
        return (pRepair.getItem() == ItemsRegistry.SOURCE_GEM.get());
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
                if (part != Repair.INSTANCE)
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
