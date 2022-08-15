package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.registry.BellaComponents;
import com.github.merchantpug.bella.registry.BellaTags;
import com.github.merchantpug.bella.util.BellHandleUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity {
	protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "interactMob", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void bella$checkIfEntityHasAnimalModel(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir, ItemStack stack, Item item) {
		if (!this.isTamed() || !this.isOwner(player)) return;
		if (stack.isOf(Items.BELL) && !this.getType().isIn(BellaTags.BLACKLIST) && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && !BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			ActionResult actionResult = BellHandleUtil.addBellToEntity((AnimalEntity)(Object)this, player, stack);
			if (actionResult != ActionResult.PASS) {
				cir.setReturnValue(actionResult);
			}
		} else if (hand.equals(Hand.MAIN_HAND) && stack.isEmpty() && player.isSneaking() && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			ActionResult actionResult = BellHandleUtil.removeBellFromEntity((AnimalEntity)(Object)this, player, hand);
			cir.setReturnValue(actionResult);
		}
	}
}
