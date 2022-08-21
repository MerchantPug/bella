package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.access.SlotAccess;
import com.github.merchantpug.bella.util.BellUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

	@Inject(method = "onClicked", at = @At("HEAD"), cancellable = true)
	private void bella$stringBellOnHead(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
		if (clickType == ClickType.RIGHT && stack.isOf(Items.BELL) && BellUtil.isBellInSlot(player, stack, slot)) {
			if (otherStack.isEmpty() && stack.getNbt() != null && stack.getNbt().getCompound("bella:bell").getBoolean("strung")) {
				ItemStack string = new ItemStack(Items.STRING);
				if (!player.giveItemStack(string)) {
					player.dropItem(string, false);
				}
				player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.7F);

				stack.getNbt().remove("bella:bell");

				cir.setReturnValue(true);
			} else if (otherStack.isOf(Items.STRING) && (stack.getNbt() == null || !stack.getNbt().getCompound("bella:bell").getBoolean("strung"))) {
				if (!player.getAbilities().creativeMode) {
					otherStack.decrement(1);
				}
				NbtCompound compound = stack.getNbt() == null || !stack.getNbt().contains("bella:bell", NbtElement.COMPOUND_TYPE) ? new NbtCompound() : stack.getOrCreateNbt().getCompound("bella:bell");
				compound.putBoolean("strung", true);
				stack.getOrCreateNbt().put("bella:bell", compound);

				player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.7F);

				cir.setReturnValue(true);
			}
		}
	}
}
