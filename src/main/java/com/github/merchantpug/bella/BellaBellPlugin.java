package com.github.merchantpug.bella;

import com.github.merchantpug.bella.api.BellRenderModifierRegistry;
import com.github.merchantpug.bella.api.BellRenderModifiers;
import com.github.merchantpug.bella.api.BellaEntrypoint;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;

public class BellaBellPlugin implements BellaEntrypoint {
	@Override
	public void registerRenderModifiers(BellRenderModifierRegistry registry) {
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.BEE, 0.0F, 19.0F, -1.0F, true));
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.CAT, animalEntity -> ((CatEntity)animalEntity).isSitting(), 0.0F, 0.0F, 12.0F));

		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.DONKEY, 0.0F, -4.0F, -8.0F, 1.0F));
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.HORSE, 0.0F, -4.0F, -8.0F, 1.0F));
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.MULE, 0.0F, -4.0F, -8.0F, 1.0F));
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.SKELETON_HORSE, 0.0F, -4.0F, -8.0F, 1.0F));
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.ZOMBIE_HORSE, 0.0F, -4.0F, -8.0F, 1.0F));

		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.PLAYER, 0.0F, 2.0F, -4.0F));
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.SHEEP, 0.0F, 0.0F, -3.0F, 1.0F));
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.HOGLIN, 0.0F, 6.0F, 0.0F));
		registry.registerBellRenderModifiers(new BellRenderModifiers(EntityType.GOAT, 0.0F, -6.0F, 2.0F));
	}
}
