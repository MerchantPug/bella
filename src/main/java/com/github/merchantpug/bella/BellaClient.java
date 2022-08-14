package com.github.merchantpug.bella;

import com.github.merchantpug.bella.util.BellRenderOverrideRegistry;
import com.github.merchantpug.bella.renderer.EntityBellModel;
import com.github.merchantpug.bella.util.BellRenderModifiers;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class BellaClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityModelLayerRegistry.registerModelLayer(EntityBellModel.LAYER_LOCATION, EntityBellModel::createModelData);
		BellRenderOverrideRegistry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.SHEEP, 0.0F, 0.0F, -3.0F, 1.0F));
		BellRenderOverrideRegistry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.CAT, animalEntity -> ((CatEntity)animalEntity).isSitting(),0.0F, 0.0F, -10.0F));
	}
}
