package com.c446.ironbound_artefacts.common.datagen;

import com.c446.ironbound_artefacts.IBA;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.c446.ironbound_artefacts.registries.RegistryDamageSources.*;

public class ModDamageTagTypesProvider extends DamageTypeTagsProvider {
    public ModDamageTagTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, IBA.MODID, existingFileHelper);
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
