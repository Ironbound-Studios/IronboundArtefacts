package com.c446.ironbound_artefacts.registries;


import com.c446.ironbound_artefacts.components.GenericUUIDComponent;
import com.c446.ironbound_artefacts.components.KillCounterComponent;
import com.c446.ironbound_artefacts.components.TuningForkAttachment;
import com.c446.ironbound_artefacts.components.UniversalPositionComponent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

//
public class ComponentRegistry {
    public static final Codec<TuningForkAttachment> TUNING_FORK_ATTACHMENT_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.fieldOf("dimension").forGetter(TuningForkAttachment::dim)
            ).apply(builder, TuningForkAttachment::new));
    public static final Codec<UniversalPositionComponent> UNIVERSAL_POSITION_COMPONENT_CODEC = RecordCodecBuilder.create(b ->
            b.group(
                    Codec.DOUBLE.fieldOf("xPos").forGetter(UniversalPositionComponent::x),
                    Codec.DOUBLE.fieldOf("yPos").forGetter(UniversalPositionComponent::y),
                    Codec.DOUBLE.fieldOf("zPos").forGetter(UniversalPositionComponent::z),
                    Codec.STRING.fieldOf("xPos").forGetter(UniversalPositionComponent::dimension)
            ).apply(b, UniversalPositionComponent::new)
    );
    private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(IronsSpellbooks.MODID);
    private static final StreamCodec<ByteBuf, TuningForkAttachment> TUNING_FORK = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, TuningForkAttachment::dim,
            TuningForkAttachment::new
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TuningForkAttachment>> TUNING_FORK_ATTACHMENT = COMPONENTS.registerComponentType("tuning_fork_attachment", builder -> builder.networkSynchronized(TUNING_FORK).persistent(TUNING_FORK_ATTACHMENT_CODEC));
    private static final StreamCodec<ByteBuf, UniversalPositionComponent> BYTE_BUF_UNIVERSAL_POSITION_COMPONENT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, UniversalPositionComponent::x,
            ByteBufCodecs.DOUBLE, UniversalPositionComponent::y,
            ByteBufCodecs.DOUBLE, UniversalPositionComponent::z,
            ByteBufCodecs.STRING_UTF8, UniversalPositionComponent::dimension,
            UniversalPositionComponent::new
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UniversalPositionComponent>> UNIVERSAL_POS = COMPONENTS.registerComponentType("tuning_rod_attachment", builder -> builder.networkSynchronized(BYTE_BUF_UNIVERSAL_POSITION_COMPONENT_STREAM_CODEC).persistent(UNIVERSAL_POSITION_COMPONENT_CODEC));
    private static final StreamCodec<ByteBuf, KillCounterComponent> KILL_COUNT = StreamCodec.composite(
            ByteBufCodecs.FLOAT, KillCounterComponent::killCount,
            KillCounterComponent::new
    );
    private static final Codec<KillCounterComponent> KILLCOUNT_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.FLOAT.fieldOf("count").forGetter(KillCounterComponent::killCount)
    ).apply(builder, KillCounterComponent::new));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<KillCounterComponent>> KILL_COUNT_COMPONENT = COMPONENTS.registerComponentType("kill_count", builder -> builder.networkSynchronized(KILL_COUNT).persistent(KILLCOUNT_CODEC));
    private static final StreamCodec<ByteBuf, GenericUUIDComponent> UUID_COMPONENT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, GenericUUIDComponent::uuid,
            GenericUUIDComponent::new
    );
    private static final Codec<GenericUUIDComponent> UUID_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("uuid").forGetter(GenericUUIDComponent::uuid)
    ).apply(builder, GenericUUIDComponent::new));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GenericUUIDComponent>> UUID_DATA_COMPONENT = COMPONENTS.registerComponentType("uuid_component", builder -> builder.networkSynchronized(UUID_COMPONENT_STREAM_CODEC).persistent(UUID_CODEC));

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String pName, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
        return COMPONENTS.register(pName, () -> pBuilder.apply(DataComponentType.builder()).build());
    }
}
