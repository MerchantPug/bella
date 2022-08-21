package com.github.merchantpug.bella.api;

public interface BellaEntrypoint {
	String ENTRYPOINT_KEY = "bella";

	void registerRenderModifiers(BellRenderModifierRegistry registry);
}
