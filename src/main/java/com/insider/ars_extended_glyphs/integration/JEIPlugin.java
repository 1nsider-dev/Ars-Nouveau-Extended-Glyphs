package com.insider.ars_extended_glyphs.integration;

import com.insider.ars_extended_glyphs.Main;
import com.insider.ars_extended_glyphs.registry.ModRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private List<RegistryObject<Item>> jeiInfoArray = new ArrayList<RegistryObject<Item>>() {
        {
            add(ModRegistry.BLANK_TABLET);
        }
    };
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Main.MODID, "jeiplugin");
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (RegistryObject<Item> item : jeiInfoArray) {
            registration.addIngredientInfo(item.get().getDefaultInstance(), VanillaTypes.ITEM_STACK,
                    Component.translatable("jei.aeg.%s".formatted(item.getId().getPath())));
        }
    }
}
