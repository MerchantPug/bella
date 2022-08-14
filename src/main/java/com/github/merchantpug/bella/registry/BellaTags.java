package com.github.merchantpug.bella.registry;

import com.github.merchantpug.bella.Bella;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class BellaTags {
	public static final TagKey<EntityType<?>> BLACKLIST = TagKey.of(Registry.ENTITY_TYPE_KEY, Bella.identifier("blacklist"));
}
