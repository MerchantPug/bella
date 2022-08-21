package com.github.merchantpug.bella.api;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class BellRenderModifiers {
	public final Predicate<LivingEntity> predicate;
	public final float x;
	public final float y;
	public final float z;
	public final float headScaleX;
	public final float headScaleY;
	public final float headScaleZ;
	public final boolean shouldMoveWithPart;
	public final boolean override;

	public BellRenderModifiers(EntityType<?> entityType, @Nullable Predicate<LivingEntity> predicate, float x, float y, float z, float headScaleX, float headScaleY, float headScaleZ, boolean shouldMoveWithPart, boolean override) {
		this.predicate = predicate != null ? (animalEntity -> animalEntity.getType() == entityType && predicate.test(animalEntity)) : animalEntity -> animalEntity.getType() == entityType;
		this.x = x;
		this.y = y;
		this.z = z;
		this.headScaleX = headScaleX;
		this.headScaleY = headScaleY;
		this.headScaleZ = headScaleZ;
		this.shouldMoveWithPart = shouldMoveWithPart;
		this.override = override;
	}

	public BellRenderModifiers(EntityType<?> entityType, @Nullable Predicate<LivingEntity> predicate, float x, float y, float z) {
		this(entityType, predicate, x, y, z, Float.NaN, Float.NaN, Float.NaN, false, false);
	}

	public BellRenderModifiers(EntityType<?> entityType, float x, float y, float z) {
		this(entityType, entity -> true, x, y, z, Float.NaN, Float.NaN, Float.NaN, false, false);
	}

	public BellRenderModifiers(EntityType<?> entityType, float x, float y, float z, boolean shouldMoveWithPart) {
		this(entityType, entity -> true, x, y, z, Float.NaN, Float.NaN, Float.NaN, shouldMoveWithPart, false);
	}

	public BellRenderModifiers(EntityType<?> entityType, float x, float y, float z, float scaleOverride) {
		this(entityType, entity -> true, x, y, z, Float.NaN, scaleOverride, Float.NaN, false, false);
	}

	public BellRenderModifiers(EntityType<?> entityType, float x, float y, float z, float scaleOverride, boolean shouldMoveWithPart) {
		this(entityType, entity -> true, x, y, z, Float.NaN, scaleOverride, Float.NaN, shouldMoveWithPart, false);
	}

	public BellRenderModifiers(EntityType<?> entityType, float x, float y, float z, float headScaleX, float headScaleY, float headScaleZ, boolean shouldMoveWithPart) {
		this(entityType, entity -> true, x, y, z, headScaleX, headScaleY, headScaleZ, shouldMoveWithPart, false);
	}

	public BellRenderModifiers override(EntityType<?> entityType, float x, float y, float z, float headScaleX, float headScaleY, float headScaleZ, boolean override) {
		return new BellRenderModifiers(entityType, entity -> true, x, y, z, headScaleX, headScaleY, headScaleZ, false, override);
	}
}
