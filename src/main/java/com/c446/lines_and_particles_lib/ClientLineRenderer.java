package com.c446.lines_and_particles_lib;

import com.c446.lines_and_particles_lib.shapes.ArcData;
import com.c446.lines_and_particles_lib.shapes.DrawableElement;
import com.c446.lines_and_particles_lib.shapes.LineData;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientLineRenderer {

    private static final ConcurrentLinkedQueue<DrawableElement> drawableElements = new ConcurrentLinkedQueue<>();

    public static void addDrawable(DrawableElement element) {
        drawableElements.add(element);
        System.out.println("adding drawable");
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        long currentTick = Minecraft.getInstance().level.getGameTime();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        Iterator<DrawableElement> iterator = drawableElements.iterator();
        while (iterator.hasNext()) {
            DrawableElement element = iterator.next();

            if (element.shouldRemove(currentTick)) {
                iterator.remove();
                continue;
            }

            double alpha = element.getAlpha(currentTick);
            if (alpha <= 0.0) {
                continue;
            }

            int color = element.getColor();
            float r = ((color >> 16) & 0xFF) / 255.0F;
            float g = ((color >> 8) & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            float a = (float) alpha;

            if (element instanceof LineData line) {
                double progress = line.getDrawProgress(currentTick);
                Vec3 start = line.startPoint;
                Vec3 end = line.endPoint;

                // Interpolate the end point based on draw progress
                Vec3 currentEnd = start.lerp(end, progress);

                bufferBuilder.addVertex(poseStack.last().pose(), (float) start.x, (float) start.y, (float) start.z).setColor(r, g, b, a);
                bufferBuilder.addVertex(poseStack.last().pose(), (float) currentEnd.x, (float) currentEnd.y, (float) currentEnd.z).setColor(r, g, b, a);
            } else if (element instanceof ArcData arc) {
                // Rendu de l'arc (simplifié pour l'exemple, à améliorer pour un vrai arc)
                // Pour un arc, on peut dessiner plusieurs petits segments
                double progress = arc.getDrawProgress(currentTick);
                // Pour l'exemple, on dessine une ligne du centre au startPoint, puis au endPoint interpolé
                // Une implémentation réelle d'arc nécessiterait une logique géométrique plus complexe

                // Simple line from center to start point for now
                bufferBuilder.addVertex(poseStack.last().pose(), (float) arc.center.x, (float) arc.center.y, (float) arc.center.z).setColor(r, g, b, a);
                bufferBuilder.addVertex(poseStack.last().pose(), (float) arc.startPoint.x, (float) arc.startPoint.y, (float) arc.startPoint.z).setColor(r, g, b, a);

                // Simple line from start point to interpolated end point
                Vec3 currentArcEnd = arc.startPoint.lerp(arc.endPoint, progress);
                bufferBuilder.addVertex(poseStack.last().pose(), (float) arc.startPoint.x, (float) arc.startPoint.y, (float) arc.startPoint.z).setColor(r, g, b, a);
                bufferBuilder.addVertex(poseStack.last().pose(), (float) currentArcEnd.x, (float) currentArcEnd.y, (float) currentArcEnd.z).setColor(r, g, b, a);
            }
        }

        poseStack.popPose();

        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
    }
}
