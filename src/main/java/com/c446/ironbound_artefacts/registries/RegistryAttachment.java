package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.attachment.*;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class RegistryAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE_DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, IBA.MODID);

    public static final Supplier<AttachmentType<IBSpellCasterData>> GENERIC_CASTING_DATA = ATTACHMENT_TYPE_DEFERRED_REGISTER.register(
            "generic_spell_caster_data_ib_artefacts", () -> AttachmentType.serializable(IBSpellCasterData::new).build()
    );
    public static final Supplier<AttachmentType<FirstLoginData>> PLAYER_FIRST_LOGIN_ATTACHMENT_IB_ARTEFACTS = ATTACHMENT_TYPE_DEFERRED_REGISTER.register(
            "player_first_login_attachment_ib_artefacts", () -> AttachmentType.serializable(FirstLoginData::new).build()
    );
    public static final Supplier<AttachmentType<SchoolAffinityComponent>> MARKOHESHKIR_ATTACHMENT = ATTACHMENT_TYPE_DEFERRED_REGISTER.register(
            "markoheshkir_attachment_ib_artefacts", () -> AttachmentType.serializable(SchoolAffinityComponent::new).build()
    );

    public static final Supplier<AttachmentType<WillStorage>> WILL = ATTACHMENT_TYPE_DEFERRED_REGISTER.register(
            "will_storage_ib_artefacts", () -> AttachmentType.serializable(WillStorage::new).build()
    );

    public static final Supplier<AttachmentType<SpellPerkAttachment>> SPELL_PERKS = ATTACHMENT_TYPE_DEFERRED_REGISTER.register(
            "spell_perks_ib_artefacts",
            () -> AttachmentType.serializable(SpellPerkAttachment::new).build()
    );
}
