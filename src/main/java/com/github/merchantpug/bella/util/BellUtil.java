package com.github.merchantpug.bella.util;

import com.github.merchantpug.bella.access.AnimalEntityAccess;
import com.github.merchantpug.bella.registry.BellaComponents;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class BellUtil {
	public static ActionResult addBellToEntity(AnimalEntity animalEntity, PlayerEntity player, ItemStack stack) {

		if (((AnimalEntityAccess)animalEntity).bella$hasAnimalModel()) {
			if (!player.getAbilities().creativeMode) {
				stack.decrement(1);
			}
			if (!player.world.isClient) {
				BellaComponents.BELL_COMPONENT.get(animalEntity).setBellAndSync(true);
			}
			return ActionResult.success(player.world.isClient);
		}
		return ActionResult.PASS;
	}

	public static ActionResult removeBellFromEntity(AnimalEntity animalEntity, PlayerEntity player, Hand hand) {
		if (!player.world.isClient) {
			player.setStackInHand(hand, new ItemStack(Items.BELL));
			BellaComponents.BELL_COMPONENT.get(animalEntity).setBellAndSync(false);
		}
		return ActionResult.success(player.world.isClient);
	}
}
