package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.access.SlotAccess;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slot.class)
public class SlotMixin implements SlotAccess {
	@Unique
	public EquipmentSlot bella$equipmentSlot;

	@Override
	public EquipmentSlot bella$getEquipmentSlot() {
		return bella$equipmentSlot;
	}

	@Override
	public void bella$setEquipmentSlot(EquipmentSlot value) {
		this.bella$equipmentSlot = value;
	}

	@Inject(method = "onTakeItem", at = @At("TAIL"))
	private void bella$removeStringWhenTakingItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		if (stack.isOf(Items.BELL) && stack.getNbt() != null && stack.getNbt().getCompound("bella:bell").getBoolean("strung")) {
			ItemStack string = new ItemStack(Items.STRING);
			if (!player.giveItemStack(string)) {
				player.dropItem(string, false);
			}
			stack.getNbt().remove("bella:bell");
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.7F);
		}
	}
}
