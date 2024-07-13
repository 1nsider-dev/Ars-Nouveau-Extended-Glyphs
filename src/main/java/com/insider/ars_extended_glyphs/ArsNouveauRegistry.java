package com.insider.ars_extended_glyphs;

import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.insider.ars_extended_glyphs.glyphs.*;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {

    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>(); //this will come handy for datagen

    public static void registerGlyphs(){
        register(Saturate.INSTANCE);
        register(SummonWildlife.INSTANCE);
        register(Shield.INSTANCE);
        register(Absorption.INSTANCE);
        register(Transmute.INSTANCE);
        register(Burrow.INSTANCE);
        register(SummonHostile.INSTANCE);
        register(AugmentExtreme.INSTANCE);
        register(Repair.INSTANCE);
        register(ReturnHome.INSTANCE);
        register(SetHome.INSTANCE);
        register(ConjureLava.INSTANCE);
        register(Atrophy.INSTANCE);
        register(BreedingCooldown.INSTANCE);
        register(BlockPosMod.INSTANCE);

        register(MethodClosestPlayer.INSTANCE);


        register(Herobrine.INSTANCE);
    }
    public static void register(AbstractSpellPart spellPart){
        GlyphRegistry.registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

}
