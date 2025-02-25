package com.c446.ironbound_artefacts.entities.simulacrum;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.*;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

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
        SpellRenderingHelper.renderSpellHelper(ClientMagicData.getSyncedSpellData((LivingEntity)this.animatable), (LivingEntity)this.animatable, poseStack, bufferSource, partialTick);
    }

    public RenderType getRenderType(K animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return animatable.isInvisible() ? RenderType.entityTranslucent(texture) : super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
