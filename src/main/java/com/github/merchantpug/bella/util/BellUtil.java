package com.github.merchantpug.bella.util;

import com.github.merchantpug.bella.Bella;
import com.github.merchantpug.bella.access.AnimalEntityAccess;
import com.github.merchantpug.bella.access.SlotAccess;
import com.github.merchantpug.bella.component.IBellComponent;
import com.github.merchantpug.bella.registry.BellaComponents;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.quiltmc.loader.api.QuiltLoader;

public class BellUtil {
	public static ActionResult addBellToEntity(MobEntity mobEntity, PlayerEntity player, ItemStack stack) {
		if (player.getAbilities().allowModifyWorld && (mobEntity instanceof AnimalEntity && ((AnimalEntityAccess)mobEntity).bella$hasAnimalModel() || !Bella.getBellRenderModifiers(mobEntity).isEmpty())) {
			if (!player.getAbilities().creativeMode) {
				stack.decrement(1);
			}
			if (!player.world.isClient) {
				BellaComponents.BELL_COMPONENT.get(mobEntity).setBell(true);
				BellaComponents.BELL_COMPONENT.get(mobEntity).sync();
				mobEntity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, mobEntity.getRandom().nextFloat() * 0.4F + 0.7F);
			}
			return ActionResult.success(player.world.isClient);
		}
		return ActionResult.PASS;
	}

	public static ActionResult stringBell(MobEntity mobEntity, PlayerEntity player, ItemStack stack) {
		IBellComponent component = BellaComponents.BELL_COMPONENT.get(mobEntity);
		if (player.getAbilities().allowModifyWorld && !component.isStrung() && (mobEntity instanceof AnimalEntity && ((AnimalEntityAccess)mobEntity).bella$hasAnimalModel() || !Bella.getBellRenderModifiers(mobEntity).isEmpty())) {
			if (!player.getAbilities().creativeMode) {
				stack.decrement(1);
			}
			if (!player.world.isClient) {
				component.setStrung(true);
				component.sync();
				mobEntity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, mobEntity.getRandom().nextFloat() * 0.4F + 0.7F);
			}
			return ActionResult.success(player.world.isClient);
		}
		return ActionResult.PASS;
	}

	public static ActionResult removeBellFromEntity(MobEntity mobEntity, PlayerEntity player, Hand hand) {
		if (player.getAbilities().allowModifyWorld) {
			if (!player.world.isClient) {
				player.setStackInHand(hand, Items.BELL.getDefaultStack());
				IBellComponent component = BellaComponents.BELL_COMPONENT.get(mobEntity);
				if (component.isStrung()) {
					ItemStack string = Items.STRING.getDefaultStack();
					if (!player.giveItemStack(string)) {
						player.dropItem(string, false);
					}
					component.setStrung(false);
				}
				component.setBell(false);
				component.sync();
				mobEntity.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, mobEntity.getRandom().nextFloat() * 0.4F + 0.7F);
			}
			return ActionResult.success(player.world.isClient);
		}
		return ActionResult.PASS;
	}

	public static boolean isBellInSlot(PlayerEntity player, ItemStack stack, Slot slot) {
		if (QuiltLoader.isModLoaded("trinkets")) {
			if (TrinketsApi.getTrinketComponent(player).isPresent()) {
				TrinketComponent component = TrinketsApi.getTrinketComponent(player).get();
				if (component.getEquipped(Items.BELL).stream().anyMatch(pair -> pair.getRight() == stack)) {
					return true;
				}
			}
		}
		return ((SlotAccess)slot).bella$getEquipmentSlot() == EquipmentSlot.HEAD;
	}

	public static boolean isBellEquipped(PlayerEntity player) {
		if (QuiltLoader.isModLoaded("trinkets")) {
			if (TrinketsApi.getTrinketComponent(player).isPresent()) {
				TrinketComponent component = TrinketsApi.getTrinketComponent(player).get();
				if (!component.getEquipped(Items.BELL).isEmpty()) {
					return true;
				}
			}
		}
		return player.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.BELL);
	}
}
