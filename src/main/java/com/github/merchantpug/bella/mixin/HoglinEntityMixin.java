package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.registry.BellaComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoglinEntity.class)
public abstract class HoglinEntityMixin extends AnimalEntity {
	protected HoglinEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "zombify", at = @At("HEAD"))
	private void bella$dropBellOnZombify(ServerWorld world, CallbackInfo ci) {
		if (BellaComponents.BELL_COMPONENT.isProvidedBy(this) && BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			this.dropStack(Items.BELL.getDefaultStack());
		}
	}
}
