package com.insider.ars_extended_glyphs.registry;

import com.insider.ars_extended_glyphs.item.CTab;
import com.insider.ars_extended_glyphs.item.RefinedMagestone;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.insider.ars_extended_glyphs.Main.MODID;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);


    public static void registerRegistries(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
    }

    private static final Item.Properties BasicProperty = new Item.Properties().tab(CTab.instance);
    public static final RegistryObject<Item> MAGESTONE = ITEMS.register("magestone",
            () -> new Item(BasicProperty));
    public static final RegistryObject<Item> REFINED_MAGESTONE = ITEMS.register("refined_magestone",
            RefinedMagestone::new);
    //public static final RegistryObject<Item> EXAMPLE = ITEMS.register("star_hat", () -> new ExampleCosmetic(new Item.Properties().tab(ArsNouveau.itemGroup)));
}
