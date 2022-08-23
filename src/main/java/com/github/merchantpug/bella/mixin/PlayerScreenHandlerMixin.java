package com.github.merchantpug.bella.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = PlayerScreenHandler.class, priority = 999)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler {
	protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> screenHandlerType, int i) {
		super(screenHandlerType, i);
	}

	@Inject(method = "transferSlot", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void bella$removeStringUponTransferringSlot(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir) {
		Slot slot = slots.get(index);
		ItemStack stack = slot.getStack();
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
