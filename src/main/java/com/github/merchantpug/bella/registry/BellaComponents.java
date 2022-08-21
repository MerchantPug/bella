package com.github.merchantpug.bella.registry;

import com.github.merchantpug.bella.Bella;
import com.github.merchantpug.bella.component.BellComponent;
import com.github.merchantpug.bella.component.IBellComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

public class BellaComponents implements EntityComponentInitializer {
	public static final ComponentKey<IBellComponent> BELL_COMPONENT = ComponentRegistry.getOrCreate(Bella.identifier("bell"), IBellComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, BELL_COMPONENT, BellComponent::new);
	}
}
