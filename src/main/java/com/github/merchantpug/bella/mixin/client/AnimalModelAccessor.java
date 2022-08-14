package com.github.merchantpug.bella.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AnimalModel.class)
public interface AnimalModelAccessor {
	@Invoker("getHeadParts")
	Iterable<ModelPart> bella$getHeadParts();

	@Accessor("childHeadYOffset")
	float bella$getChildHeadYOffset();

	@Accessor("childHeadZOffset")
	float bella$getChildHeadZOffset();
}
