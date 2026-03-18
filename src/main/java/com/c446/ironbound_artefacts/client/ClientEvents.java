package com.c446.ironbound_artefacts.client;

import com.c446.ironbound_artefacts.ClientSetup;
import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.client.tooltip.FlamingClientTooltipComponent;
import com.c446.ironbound_artefacts.client.tooltip.LightningClientTooltipComponent;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();

        for (int i = 0; i < elements.size(); i++) {
            var element = elements.get(i);
            final int index = i;

            element.left().ifPresent(formattedText -> {
                String rawText = formattedText.getString();

                if (rawText.contains(ArtefactStyles.MARKER)) {
                    int first = rawText.indexOf(ArtefactStyles.MARKER);
                    int last = rawText.lastIndexOf(ArtefactStyles.MARKER);

                    if (first != -1 && last != -1 && first != last) {
                        String type = rawText.substring(first + 1, first + 2);
                        String content = rawText.substring(first + 2, last);

                        String prefix = rawText.substring(0, first);
                        String suffix = rawText.substring(last + 1);

                        // --- STYLE EXTRACTION ---
                        // Since FormattedText doesn't have .getStyle(), we check if it's a Component.
                        // If it's not, we fall back to Style.EMPTY.
                        Style extractedStyle = Style.EMPTY;
                        if (formattedText instanceof Component comp) {
                            extractedStyle = comp.getStyle();
                        }

                        elements.remove(index);
                        int currentPos = index;

                        if (!prefix.isEmpty()) {
                            elements.add(currentPos++, Either.left(Component.literal(prefix).withStyle(extractedStyle)));
                        }

                        // Reconstruct with the extracted style
                        Component styledContent = Component.literal(content).withStyle(extractedStyle);

                        if (type.equals(ArtefactStyles.TYPE_LIGHTNING)) {
                            elements.add(currentPos++, Either.right(new LightningClientTooltipComponent.LightningTooltipData(styledContent)));
                        } else if (type.equals(ArtefactStyles.TYPE_FLAME)) {
                            elements.add(currentPos++, Either.right(new FlamingClientTooltipComponent.FlamingTooltipData(styledContent)));
                        }

                        if (!suffix.isEmpty()) {
                            elements.add(currentPos, Either.left(Component.literal(suffix).withStyle(extractedStyle)));
                        }
                    }
                }
            });
        }
    }

    private static class ArtefactStyles {
        // Using Private Use Area (PUA) to avoid mod conflicts
        public static final String MARKER = "\uE446";
        public static final String TYPE_LIGHTNING = "L";
        public static final String TYPE_FLAME = "F";

        public static MutableComponent lightning(String text) {
            return Component.literal(MARKER + TYPE_LIGHTNING + text + MARKER);
        }

        public static MutableComponent flaming(String text) {
            return Component.literal(MARKER + TYPE_FLAME + text + MARKER);
        }
    }
}
