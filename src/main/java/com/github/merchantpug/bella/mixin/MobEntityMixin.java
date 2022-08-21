package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.registry.BellaComponents;
import com.github.merchantpug.bella.registry.BellaTags;
import com.github.merchantpug.bella.util.BellUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "interactMob", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void bella$handleBell(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isOf(Items.BELL) && !this.getType().isIn(BellaTags.BLACKLIST) && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && !BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			ActionResult actionResult = BellUtil.addBellToEntity((MobEntity)(Object)this, player, stack);
			if (actionResult != ActionResult.PASS) {
				cir.setReturnValue(actionResult);
			}
		} else if (stack.isOf(Items.STRING) && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && BellaComponents.BELL_COMPONENT.get(this).hasBell() && !BellaComponents.BELL_COMPONENT.get(this).isStrung()) {
			ActionResult actionResult = BellUtil.stringBell((MobEntity)(Object)this, player, stack);
			cir.setReturnValue(actionResult);
		} else if (hand.equals(Hand.MAIN_HAND) && stack.isEmpty() && player.isSneaking() && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			ActionResult actionResult = BellUtil.removeBellFromEntity((MobEntity)(Object)this, player, hand);
			cir.setReturnValue(actionResult);
		}
	}
}
