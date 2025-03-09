package com.c446.ironbound_artefacts.entities.simulacrum;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;

public class SimulacrumRenderer<K extends SimulacrumEntity> extends HumanoidRenderer<K> {
    private ResourceLocation textureResource;

    public SimulacrumRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimulacrumModel<>());
        this.shadowRadius = 0.5F;
//        this.addRenderLayer(new io.redspace.ironsspellbooks.render.EnergySwirlLayer.Geo(this, io.redspace.ironsspellbooks.render.EnergySwirlLayer.EVASION_TEXTURE, 2L));
//        this.addRenderLayer(new io.redspace.ironsspellbooks.render.EnergySwirlLayer.Geo(this, EnergySwirlLayer.CHARGE_TEXTURE, 64L));
//        this.addRenderLayer(new ChargeSpellLayer.Geo(this));
//        this.addRenderLayer(new GlowingEyesLayer.Geo(this));
//        this.addRenderLayer(new SpellTargetingLayer.Geo(this));
//        this.addRenderLayer(new GeoSpinAttackLayer(this));
    }

    public SimulacrumRenderer(EntityRendererProvider.Context renderManager, SimulacrumModel model) {
        super(renderManager, model);
        this.shadowRadius = 0.5F;
//        this.addRenderLayer(new io.redspace.ironsspellbooks.render.EnergySwirlLayer.Geo(this, io.redspace.ironsspellbooks.render.EnergySwirlLayer.EVASION_TEXTURE, 2L));
//        this.addRenderLayer(new io.redspace.ironsspellbooks.render.EnergySwirlLayer.Geo(this, EnergySwirlLayer.CHARGE_TEXTURE, 64L));
//        this.addRenderLayer(new ChargeSpellLayer.Geo(this));
//        this.addRenderLayer(new GlowingEyesLayer.Geo(this));
//        this.addRenderLayer(new SpellTargetingLayer.Geo(this));
//        this.addRenderLayer(new GeoSpinAttackLayer(this));
    }

    public static ItemStack makePotion(SimulacrumEntity entity) {
        ItemStack healthPotion = new ItemStack(Items.POTION);
        return Utils.setPotion(healthPotion, entity.isInvertedHealAndHarm() ? Potions.HARMING : Potions.HEALING);
    }

    public void render(K entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        SpellRenderingHelper.renderSpellHelper(ClientMagicData.getSyncedSpellData(this.animatable), this.animatable, poseStack, bufferSource, partialTick);
    }

    public RenderType getRenderType(K animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return animatable.isInvisible() ? RenderType.entityTranslucent(texture) : super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
