package com.c446.ironbound_artefacts.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

public class LightningClientTooltipComponent implements ClientTooltipComponent {

    public record LightningTooltipData(Component text) implements TooltipComponent {}

    private final Component text;
    private static final int COLOR_BLUE = 0x5555FF;
    private static final int COLOR_GOLD = 0xFFAA00;

    public LightningClientTooltipComponent(LightningTooltipData data) {
        this.text = data.text();
    }

    @Override
    public int getHeight() { return 12; }

    @Override
    public int getWidth(Font font) { return font.width(text); }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        int width = getWidth(font);
        int height = getHeight();

        long seed = System.currentTimeMillis() / 100;
        RandomSource random = RandomSource.create(seed);

        if (random.nextFloat() < 0.4f) {
            guiGraphics.pose().pushPose();
            // Confirmed working Z-index
            guiGraphics.pose().translate(0, 0, 500f);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            // Generate 2 thin arcs
            for (int i = 0; i < 2; i++) {
                int x1 = x + random.nextInt(width);
                int y1 = y + 2;
                int x2 = x + random.nextInt(width);
                int y2 = y + height - 2;
                drawThinArc(guiGraphics, x1, y1, x2, y2, random);
            }

            guiGraphics.flush();
            RenderSystem.disableBlend();
            guiGraphics.pose().popPose();
        }
    }

    private void drawThinArc(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, RandomSource random) {
        int curX = x1, curY = y1;
        int segments = 4; // More segments = more "wiggly" and thin

        for (int i = 1; i <= segments; i++) {
            float pct = (float) i / segments;
            int tx = (int) (x1 + (x2 - x1) * pct) + (random.nextInt(5) - 2);
            int ty = (int) (y1 + (y2 - y1) * pct) + (random.nextInt(3) - 1);

            int minX = Math.min(curX, tx);
            int maxX = Math.max(curX, tx);
            int minY = Math.min(curY, ty);
            int maxY = Math.max(curY, ty);

            // 1. OUTER GLOW: 2px thick, semi-transparent cyan
            // We use +1 to the max bounds for a very fine 2-pixel line
            guiGraphics.fill(minX - 1, minY - 1, maxX + 1, maxY + 1, 0x6655FFFF);

            // 2. INNER CORE: 1px thick, fully opaque white
            // No offset means it's as thin as the GUI can render
            guiGraphics.fill(minX, minY, maxX, maxY, 0xFFFFFFFF);

            curX = tx; curY = ty;
        }
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer) {
        String rawString = text.getString();
        long time = System.currentTimeMillis();

        float xOffset = x;
        float shakeX = (float) (Math.sin(time * 0.05) * 0.6f);
        float shakeY = (float) (Math.cos(time * 0.05) * 0.6f);

        for (int i = 0; i < rawString.length(); i++) {
            String letter = String.valueOf(rawString.charAt(i));

            float colorPhase = (float) (Math.sin((time * 0.005) + (i * 0.3f)) + 1.0f) / 2.0f;
            int baseColor = interpolateColor(COLOR_BLUE, COLOR_GOLD, colorPhase);

            float glintSwell = (float) Math.max(0, Math.sin((time * 0.012) + (i * 0.5f)) - 0.7f) * 3.0f;
            int finalColor = applyGlint(baseColor, glintSwell);

            font.drawInBatch(letter, xOffset + shakeX, y + shakeY + 2, finalColor, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
            xOffset += font.width(letter);
        }
    }

    private int interpolateColor(int c1, int c2, float ratio) {
        int r = (int) (((c1 >> 16) & 0xFF) * (1 - ratio) + ((c2 >> 16) & 0xFF) * ratio);
        int g = (int) (((c1 >> 8) & 0xFF) * (1 - ratio) + ((c2 >> 8) & 0xFF) * ratio);
        int b = (int) ((c1 & 0xFF) * (1 - ratio) + (c2 & 0xFF) * ratio);
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    private int applyGlint(int color, float glint) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) + (255 * glint)));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) + (255 * glint)));
        int b = Math.min(255, (int) ((color & 0xFF) + (255 * glint)));
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }
}