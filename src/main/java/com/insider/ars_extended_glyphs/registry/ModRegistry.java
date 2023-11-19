package com.insider.ars_extended_glyphs.registry;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.insider.ars_extended_glyphs.item.CTab;
import com.insider.ars_extended_glyphs.item.RefinedMagestone;
import com.insider.ars_extended_glyphs.item.Tablet;
import com.insider.ars_extended_glyphs.item.UselessTablet;
import com.insider.ars_extended_glyphs.loot.AddItemModifier;
import com.insider.ars_extended_glyphs.ritual.RitualDifficultyIncrease;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.insider.ars_extended_glyphs.Main.MODID;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
    public static final AbstractRitual[] RITUALS = {
            new RitualDifficultyIncrease()
    };

    public static void registerRegistries(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        LOOT_MODIFIERS.register(bus);

        for (AbstractRitual rit : RITUALS){
            ArsNouveauAPI.getInstance().registerRitual(rit);
        }
    }

    private static final Item.Properties BasicProperty = new Item.Properties().tab(CTab.instance);
    public static final RegistryObject<Item> MAGESTONE = ITEMS.register("magestone",
            () -> new Item(BasicProperty));
    public static final RegistryObject<Item> REFINED_MAGESTONE = ITEMS.register("refined_magestone",
            RefinedMagestone::new);

    // Ext.
    public static final Rarity UNIQUE_RARITY = Rarity.create("aeg_unique", ChatFormatting.DARK_AQUA);

    // Tablets

    public static final RegistryObject<Item> BLANK_TABLET = ITEMS.register("blank_tablet", UselessTablet::new);
    public static final RegistryObject<Item> AIR_TABLET = ITEMS.register("air_tablet",  () -> new Tablet(SpellSchools.ELEMENTAL_AIR));
    public static final RegistryObject<Item> WATER_TABLET = ITEMS.register("water_tablet", () -> new Tablet(SpellSchools.ELEMENTAL_WATER));
    public static final RegistryObject<Item> FIRE_TABLET = ITEMS.register("fire_tablet", () -> new Tablet(SpellSchools.ELEMENTAL_FIRE));
    public static final RegistryObject<Item> EARTH_TABLET = ITEMS.register("earth_tablet", () -> new Tablet(SpellSchools.ELEMENTAL_EARTH));
    public static final RegistryObject<Item> CONJURATION_TABLET = ITEMS.register("conjuration_tablet", () -> new Tablet(SpellSchools.CONJURATION));
    public static final RegistryObject<Item> MANIPULATION_TABLET = ITEMS.register("manipulation_tablet", () -> new Tablet(SpellSchools.MANIPULATION));
    public static final RegistryObject<Item> ABJURATION_TABLET = ITEMS.register("abjuration_tablet", () -> new Tablet(SpellSchools.ABJURATION));

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIERS.register("include_tablet", AddItemModifier.CODEC);
}
