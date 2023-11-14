package com.insider.ars_extended_glyphs;

import com.insider.ars_extended_glyphs.glyphs.*;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {

    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>(); //this will come handy for datagen

    public static void registerGlyphs(){
        register(Saturate.INSTANCE);
        register(SummonWildlife.INSTANCE);
        register(Shield.INSTANCE);
        register(Absorption.INSTANCE);
        register(MethodClosestPlayer.INSTANCE);
        register(Transmute.INSTANCE);
        register(Burrow.INSTANCE);
        register(SummonHostile.INSTANCE);
        register(AugmentExtreme.INSTANCE);
        register(CreateSource.INSTANCE);
        register(Repair.INSTANCE);
        register(ReturnHome.INSTANCE);
        register(SetHome.INSTANCE);

        register(TargetSelf.INSTANCE);

        register(Herobrine.INSTANCE);
    }
    public static void register(AbstractSpellPart spellPart){
        ArsNouveauAPI.getInstance().registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

}
