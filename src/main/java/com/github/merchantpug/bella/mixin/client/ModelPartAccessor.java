package com.github.merchantpug.bella.mixin.client;

import net.minecraft.client.model.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(ModelPart.class)
public interface ModelPartAccessor {
	@Accessor("cuboids")
	List<ModelPart.Cuboid> bella$getCuboids();

	@Accessor("children")
	Map<String, ModelPart> bella$getChildren();
}
