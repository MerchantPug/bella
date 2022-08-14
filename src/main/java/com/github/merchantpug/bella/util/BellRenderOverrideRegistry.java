package com.github.merchantpug.bella.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.passive.AnimalEntity;

import java.util.HashSet;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BellRenderOverrideRegistry {
	private static final HashSet<BellRenderModifiers> BELL_RENDER_MODIFIERS = new HashSet<>();

	public static void registerBellRenderModifiers(BellRenderModifiers modifiers) {
		BELL_RENDER_MODIFIERS.add(modifiers);
	}

	public static List<BellRenderModifiers> getBellRenderModifiers(AnimalEntity entity) {
		return BELL_RENDER_MODIFIERS.stream().filter(modifiers -> modifiers.predicate.test(entity)).toList();
	}
}
