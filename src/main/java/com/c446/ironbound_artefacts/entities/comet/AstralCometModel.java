package com.c446.ironbound_artefacts.entities.comet;

// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.c446.ironbound_artefacts.IronboundArtefact;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class AstralCometModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(IronboundArtefact.prefix("comet"), "main");
    private final ModelPart bb_main;

    public AstralCometModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public AstralCometModel(EntityRendererProvider.Context context) {
        var model = context.bakeLayer(LAYER_LOCATION);
        this.bb_main = model.getChild("bb_main");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(24, 37).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(6.0F, -24.0F, -6.0F, 0.0F, 25.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(48, 12).addBox(-6.0F, -24.0F, 6.0F, 12.0F, 25.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-6.0F, -24.0F, -6.0F, 0.0F, 25.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(48, 57).addBox(-6.0F, -24.0F, -6.0F, 12.0F, 25.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(-6.0F, 1.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(3.0F, -42.0F, -3.0F, 0.0F, 43.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 57).addBox(-3.0F, -42.0F, -3.0F, 6.0F, 43.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 57).addBox(-3.0F, -42.0F, 3.0F, 6.0F, 43.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(12, 37).addBox(-3.0F, -42.0F, -3.0F, 0.0F, 43.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
        bb_main.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
    }

}
