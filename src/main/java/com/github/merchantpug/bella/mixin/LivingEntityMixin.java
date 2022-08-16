package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.Bella;
import com.github.merchantpug.bella.access.AnimalEntityAccess;
import com.github.merchantpug.bella.registry.BellaComponents;
import com.github.merchantpug.bella.util.BellHandleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private float bella$prevPrevHeadYaw;
	@Unique
	private float bella$prevRotationRadians;

	@Shadow
	public float headYaw;

	@Shadow
	public float prevHeadYaw;

	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void bella$setLoadedIn(EntityType entityType, World world, CallbackInfo ci) {
		this.bella$prevPrevHeadYaw = this.headYaw;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void bella$tickBell(CallbackInfo ci) {
		if (((LivingEntity)(Object)this instanceof AnimalEntity) && BellaComponents.BELL_COMPONENT.isProvidedBy(this)) {
			if (BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
				float f = MathHelper.clamp(this.headYaw - this.bella$prevPrevHeadYaw, -90.0F, 90.0F);
				int bellTicks = ((AnimalEntityAccess)this).bella$getBellTicks();

				if ((f > 10.0 || f < -10.0) && bellTicks == 0) {
					((AnimalEntityAccess)this).bella$setBellTicks(1);
					((AnimalEntityAccess)this).bella$setPreviousMovement(f);
				}

				float previousMovement = ((AnimalEntityAccess)this).bella$getPreviousMovement();
				if (bellTicks > previousMovement * 1.33) {
					((AnimalEntityAccess)this).bella$setBellTicks(0);
					((AnimalEntityAccess)this).bella$setPreviousMovement(0);
					this.bella$prevRotationRadians = 0;
				} else if (bellTicks > 0) {
					if (BellHandleUtil.getBellRotationRadians(bellTicks, previousMovement) > 0.0F && this.bella$prevRotationRadians < 0.0F || BellHandleUtil.getBellRotationRadians(bellTicks, previousMovement) < 0.0F && this.bella$prevRotationRadians > 0.0F) {
						this.playSound(SoundEvents.BLOCK_BELL_USE, Math.min(Math.abs(BellHandleUtil.getBellRotationRadians(bellTicks, previousMovement) + this.bella$prevRotationRadians) * 2, 1.0F), this.random.nextFloat() * 0.4F + 1.0F);
					}
					((AnimalEntityAccess)this).bella$setBellTicks(((AnimalEntityAccess)this).bella$getBellTicks() + 1);
					this.bella$prevRotationRadians = BellHandleUtil.getBellRotationRadians(bellTicks, previousMovement);
				}
			}
			this.bella$prevPrevHeadYaw = this.prevHeadYaw;
		}
	}
}
