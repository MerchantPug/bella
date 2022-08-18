package com.github.merchantpug.bella.registry;

import com.github.merchantpug.bella.Bella;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

public class BellaGameEvents {
	public static final GameEvent ENTITY_BELL_RING = register(Bella.identifier("entity_bell_ring"));

	public static void init() {

	}

	public static GameEvent register(Identifier identifier) {
		return Registry.register(Registry.GAME_EVENT, identifier, new GameEvent(identifier.toString(), GameEvent.DEFAULT_RANGE));
	}
}
