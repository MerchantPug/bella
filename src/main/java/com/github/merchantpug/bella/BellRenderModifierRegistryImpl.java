package com.github.merchantpug.bella;

import com.github.merchantpug.bella.api.BellRenderModifierRegistry;
import com.github.merchantpug.bella.api.BellRenderModifiers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BellRenderModifierRegistryImpl implements BellRenderModifierRegistry {
	public void registerBellRenderModifiers(BellRenderModifiers modifiers) {
		Bella.BELL_RENDER_MODIFIERS.add(modifiers);
	}
}
