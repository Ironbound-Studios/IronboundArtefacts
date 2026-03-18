package com.c446.ironbound_artefacts.perks.spellperks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PerkTree {
    String spellId(); // The ID of the spell this tree belongs to
}

