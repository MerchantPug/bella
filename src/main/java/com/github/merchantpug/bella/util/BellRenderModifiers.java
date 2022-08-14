package com.github.merchantpug.bella.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class BellRenderModifiers {
	public final Predicate<AnimalEntity> predicate;
	public final float x;
	public final float y;
	public final float z;
	public final float scaleOverride;

	public BellRenderModifiers(EntityType<?> entityType, @Nullable Predicate<AnimalEntity> predicate, float x, float y, float z, float scaleOverride) {
		this.predicate = predicate != null ? (animalEntity -> animalEntity.getType() == entityType && predicate.test(animalEntity)) : animalEntity -> animalEntity.getType() == entityType;
		this.x = x;
		this.y = y;
		this.z = z;
		this.scaleOverride = scaleOverride;
	}

	public BellRenderModifiers(EntityType<?> entityType, @Nullable Predicate<AnimalEntity> predicate, float x, float y, float z) {
		this(entityType, predicate, x, y, z, Float.NaN);
	}

	public BellRenderModifiers(EntityType<?> entityType, float x, float y, float z, float scaleOverride) {
		this(entityType, null, x, y, z, scaleOverride);
	}

	public BellRenderModifiers(EntityType<?> entityType, float x, float y, float z) {
		this(entityType, null, x, y, z, Float.NaN);
	}
}
