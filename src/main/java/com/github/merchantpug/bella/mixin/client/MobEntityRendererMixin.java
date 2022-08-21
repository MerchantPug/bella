package com.github.merchantpug.bella.mixin.client;

import com.github.merchantpug.bella.renderer.EntityBellFeatureRenderer;
import com.sun.jna.platform.win32.WinBase;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntityRenderer.class)
public abstract class MobEntityRendererMixin<T extends MobEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
	public MobEntityRendererMixin(EntityRendererFactory.Context context, M entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void bella$injectBellRendererIntoAnimals(EntityRendererFactory.Context context, EntityModel entityModel, float f, CallbackInfo ci) {
		this.addFeature(new EntityBellFeatureRenderer((MobEntityRenderer)(Object)this, context.getModelLoader()));
	}
}
