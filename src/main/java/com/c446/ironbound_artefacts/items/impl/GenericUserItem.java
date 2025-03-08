package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.items.UserDependantCurios;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.UUID;

public class GenericUserItem extends UserDependantCurios {
    HashMap<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();

    public GenericUserItem(Properties p) {
        super(p);
    }

    public GenericUserItem withAttribute(Holder<Attribute> attribute, AttributeModifier modifier) {
        this.attributes.put(attribute, modifier);
        return this;
    }

    public GenericUserItem withUser(UUID user) {
        this.user = user;
        return this;
    }

}
