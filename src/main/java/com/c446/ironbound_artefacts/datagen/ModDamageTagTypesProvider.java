package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.ironbound_spells.spells.HeavenlyPunishment;
import com.c446.ironbound_artefacts.registries.DamageSourcesReg;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.c446.ironbound_artefacts.registries.DamageSourcesReg.*;

public class ModDamageTagTypesProvider extends DamageTypeTagsProvider {
    public ModDamageTagTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, IronboundArtefact.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(DamageTypeTagGenerator.FIRE_MAGIC)
                .add(VOID_SWORD_HEAVEN);
//        tag(Tags.SpellTags.IGNORE_SPELL_RES)
//                .add(VOID_SWORD_UPPER);
        tag(DamageTypeTags.BYPASSES_EFFECTS)
                .add(VOID_SWORD_FUTURE);
        tag(DamageTypeTags.BYPASSES_SHIELD)
                .add(VOID_SWORD_LIGHT)
                .add(VOID_SWORD_HEAVEN);

        tag(DamageTypeTags.BYPASSES_COOLDOWN)
                .add(VOID_SWORD_LIGHT)
                .add(VOID_SWORD_HOPE)
                .add(VOID_SWORD_ZERO)
                .add(VOID_SWORD_HEAVEN)
                .add(VOID_SWORD_UPPER)
        ;

        super.addTags(provider);
    }
}
