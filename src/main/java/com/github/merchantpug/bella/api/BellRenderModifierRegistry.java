package com.github.merchantpug.bella.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface BellRenderModifierRegistry {
	void registerBellRenderModifiers(BellRenderModifiers modifiers);
}
