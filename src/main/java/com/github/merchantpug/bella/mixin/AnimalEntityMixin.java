package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.access.AnimalEntityAccess;
import com.github.merchantpug.bella.registry.BellaComponents;
import com.github.merchantpug.bella.registry.BellaTags;
import com.github.merchantpug.bella.util.BellHandleUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity implements AnimalEntityAccess {
	@Unique
	private boolean bella$hasAnimalModel = false;
	@Unique
	private int bella$bellTicks;
	@Unique
	private float bella$previousMovement;

	protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ActionResult interactMob(PlayerEntity player, Hand hand);

	@Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;isBreedingItem(Lnet/minecraft/item/ItemStack;)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void bella$addBellToEntity(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir, ItemStack stack) {
		if (stack.isOf(Items.BELL) && !this.getType().isIn(BellaTags.BLACKLIST) && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && !BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			ActionResult actionResult = BellHandleUtil.addBellToEntity((AnimalEntity)(Object)this, player, stack);
			if (actionResult != ActionResult.PASS) {
				cir.setReturnValue(actionResult);
			}
		} else if (hand.equals(Hand.MAIN_HAND) && stack.isEmpty() && player.isSneaking() && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			cir.setReturnValue(BellHandleUtil.removeBellFromEntity((AnimalEntity)(Object)this, player, hand));
		}
	}

	@Override
	public boolean bella$hasAnimalModel() {
		return bella$hasAnimalModel;
	}

	@Override
	public void bella$setHasAnimalModel(boolean value) {
		this.bella$hasAnimalModel = value;
	}

	@Override
	public int bella$getBellTicks() {
		return bella$bellTicks;
	}

	@Override
	public void bella$setBellTicks(int value) {
		this.bella$bellTicks = value;
	}

	@Override
	public float bella$getPreviousMovement() {
		return this.bella$previousMovement;
	}

	@Override
	public void bella$setPreviousMovement(float value) {
		this.bella$previousMovement = value;
	}
}
