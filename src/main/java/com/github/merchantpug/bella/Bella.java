package com.github.merchantpug.bella;

import com.github.merchantpug.bella.api.BellRenderModifierRegistry;
import com.github.merchantpug.bella.api.BellRenderModifiers;
import com.github.merchantpug.bella.api.BellaEntrypoint;
import com.github.merchantpug.bella.networking.BellaPacketsC2S;
import com.github.merchantpug.bella.registry.BellaGameEvents;
import com.github.merchantpug.bella.registry.BellaTrinketsRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;

public class Bella implements ModInitializer {
	public static final String MODID = "bella";
	public static final Logger LOGGER = LoggerFactory.getLogger("Bella");
	public static String VERSION = "";

	protected static final HashSet<BellRenderModifiers> BELL_RENDER_MODIFIERS = new HashSet<>();

	@Override
	public void onInitialize(ModContainer mod) {
		VERSION = mod.metadata().version().raw();
		if (VERSION.contains("+")) {
			VERSION = VERSION.split("\\+")[0];
		}
		if (VERSION.contains("-")) {
			VERSION = VERSION.split("-")[0];
		}

		if (QuiltLoader.isModLoaded("trinkets")) {
			BellaTrinketsRegistry.register();
		}

		BellRenderModifierRegistry registry = new BellRenderModifierRegistryImpl();
		for (var initializer : QuiltLoader.getEntrypointContainers(BellaEntrypoint.ENTRYPOINT_KEY, BellaEntrypoint.class)) {
			initializer.getEntrypoint().registerRenderModifiers(registry);
		}

		BellaPacketsC2S.register();
		BellaGameEvents.init();

		LOGGER.info("{} v{} has initialized. Ring ring ring!", mod.metadata().name(), VERSION);
	}

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}

	public static List<BellRenderModifiers> getBellRenderModifiers(LivingEntity entity) {
		return BELL_RENDER_MODIFIERS.stream().filter(modifiers -> modifiers.predicate.test(entity)).toList();
	}
}
