package com.github.merchantpug.bella.integration.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BellTrinket implements Trinket {
	@Override
	public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		return stack.getCount() == 1;
	}
}
