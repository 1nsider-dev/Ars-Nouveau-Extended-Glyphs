package com.insider.ars_extended_glyphs.registry;

import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.items.curios.AbstractManaCurio;
import com.hollingsworth.arsnouveau.common.items.curios.DiscountRing;
import com.hollingsworth.arsnouveau.common.util.RegistryWrapper;
import com.insider.ars_extended_glyphs.item.*;
import com.insider.ars_extended_glyphs.loot.AddItemModifier;
import com.insider.ars_extended_glyphs.ritual.RitualDifficultyIncrease;
import com.insider.ars_extended_glyphs.sound.SoundRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

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
    public static final RegistryObject<DiscountRing> RING_OF_GREATER_DISCOUNT = ITEMS.register("ring_of_greatest_discount", () -> new DiscountRing() {
        @Override
        public int getManaDiscount() {
            return 50;
        }

        @Override
        public Collection<CreativeModeTab> getCreativeTabs() {
            return Arrays.asList(CTab.TABS);
        }
    });
    public static final RegistryObject<AbstractManaCurio> AMULET_OF_GREATER_MANA_REGEN = ITEMS.register("amulet_of_greater_mana_regen", () -> new AbstractManaCurio() {
        @Override
        public int getManaRegenBonus(ItemStack i) {
            return 20;
        }
    });
    public static final RegistryObject<Item> BROKEN_RECORD = ITEMS.register("music_disc_record",
            () -> new RecordItem(6, SoundRegistry.BROKEN_RECORD, BasicProperty.rarity(Rarity.RARE).stacksTo(1), 3200));

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
    //~Fragments
    public static final RegistryObject<Item> BLANK_TABLET_FRAG = ITEMS.register("blank_tablet_fragment", UselessTabletFragment::new);
    public static final RegistryObject<Item> AIR_TABLET_FRAG = ITEMS.register("air_tablet_fragment", () -> new TabletFragment(SpellSchools.ELEMENTAL_AIR));
    public static final RegistryObject<Item> FIRE_TABLET_FRAG = ITEMS.register("fire_tablet_fragment", () -> new TabletFragment(SpellSchools.ELEMENTAL_FIRE));
    public static final RegistryObject<Item> WATER_TABLET_FRAG = ITEMS.register("water_tablet_fragment", () -> new TabletFragment(SpellSchools.ELEMENTAL_WATER));
    public static final RegistryObject<Item> EARTH_TABLET_FRAG = ITEMS.register("earth_tablet_fragment", () -> new TabletFragment(SpellSchools.ELEMENTAL_EARTH));

    // Entities

    static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(MODID + ":" + name));
    }

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIERS.register("include_tablet", AddItemModifier.CODEC);
}
