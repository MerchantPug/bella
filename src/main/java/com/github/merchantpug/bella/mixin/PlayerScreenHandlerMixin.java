package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.access.SlotAccess;
import com.github.merchantpug.bella.util.BellUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/screen/PlayerScreenHandler$1")
public class PlayerScreenHandlerMixin {
	@Shadow
	@Final
	EquipmentSlot field_7834;

	@Shadow
	@Final
	PlayerEntity field_39410;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void bella$setEquipmentSlot(PlayerScreenHandler playerScreenHandler, Inventory inventory, int i, int j, int k, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, CallbackInfo ci) {
		((SlotAccess)this).bella$setEquipmentSlot(field_7834);
	}

	@Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
	private void bella$allowWearingBell(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.isOf(Items.BELL) && field_7834 == EquipmentSlot.HEAD && !BellUtil.isBellEquipped(this.field_39410)) {
			cir.setReturnValue(true);
		}
	}
}
