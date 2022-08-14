package com.github.merchantpug.bella;

import com.github.merchantpug.bella.networking.BellaPacketsC2S;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bella implements ModInitializer {
	public static final String MODID = "bella";
	public static final Logger LOGGER = LoggerFactory.getLogger("Bella");
	public static String VERSION = "";

	@Override
	public void onInitialize(ModContainer mod) {
		VERSION = mod.metadata().version().raw();
		if (VERSION.contains("+")) {
			VERSION = VERSION.split("\\+")[0];
		}
		if (VERSION.contains("-")) {
			VERSION = VERSION.split("-")[0];
		}

		BellaPacketsC2S.register();

		LOGGER.info("{} v{} has initialized. Ring ring ring!", mod.metadata().name(), VERSION);
	}

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}
}
