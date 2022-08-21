package com.github.merchantpug.bella.registry;

import com.github.merchantpug.bella.Bella;
import com.github.merchantpug.bella.integration.trinkets.BellTrinket;
import com.github.merchantpug.bella.util.BellUtil;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class BellaTrinketsRegistry {
	public static void register() {
		TrinketsApi.registerTrinket(Items.BELL, new BellTrinket());

		TrinketsApi.registerTrinketPredicate(Bella.identifier("stack_of_one_bell"), (stack, slot, entity) -> {
			if (stack.isOf(Items.BELL)) {
				Bella.LOGGER.info(String.valueOf(stack.getCount()));
			}
			return TriState.DEFAULT;
		});
		TrinketsApi.registerTrinketPredicate(Bella.identifier("dont_quick_stack_bell"), (stack, slot, entity) -> {
			if (stack.isOf(Items.BELL)) {
				return TriState.FALSE;
			}
			return TriState.DEFAULT;
		});
		TrinketsApi.registerTrinketPredicate(Bella.identifier("only_one_bell"), (stack, slot, entity) -> {
			if (stack.isOf(Items.BELL) && entity instanceof PlayerEntity player && BellUtil.isBellEquipped(player)) {
				return TriState.FALSE;
			}
			return TriState.DEFAULT;
		});
	}
}
