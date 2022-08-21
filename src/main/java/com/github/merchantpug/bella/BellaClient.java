package com.github.merchantpug.bella;

import com.github.merchantpug.bella.networking.BellaPacketsS2C;
import com.github.merchantpug.bella.renderer.EntityBellModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class BellaClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityModelLayerRegistry.registerModelLayer(EntityBellModel.LAYER_LOCATION, EntityBellModel::createModelData);

		BellaPacketsS2C.register();
	}
}
