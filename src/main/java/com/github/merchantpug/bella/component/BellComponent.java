package com.github.merchantpug.bella.component;

import com.github.merchantpug.bella.registry.BellaComponents;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.List;

public class BellComponent implements IBellComponent, AutoSyncedComponent {
	private boolean bell = false;
	private boolean strung = false;
	private final LivingEntity provider;

	public BellComponent(LivingEntity provider) {
		this.provider = provider;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.setBell(tag.getBoolean("has_bell"));
		this.setStrung(tag.getBoolean("strung"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("has_bell", this.bell);
		tag.putBoolean("strung", this.strung);
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
		buf.writeBoolean(this.bell);
	}

	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		this.bell = buf.readBoolean();
	}

	@Override
	public boolean hasBell() {
		if (provider instanceof PlayerEntity) {
			if (provider.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.BELL)) {
				return true;
			} else if (QuiltLoader.isModLoaded("trinkets") && TrinketsApi.getTrinketComponent(provider).isPresent() && TrinketsApi.getTrinketComponent(provider).get().isEquipped(Items.BELL)) {
				return true;
			}
		}
		return bell;
	}

	@Override
	public void setBell(boolean value) {
		this.bell = value;
	}

	@Override
	public boolean isStrung() {
		if (provider instanceof PlayerEntity) {
			if (provider.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.BELL)) {
				ItemStack stack = provider.getEquippedStack(EquipmentSlot.HEAD);
				if (stack.getNbt() == null) {
					return false;
				}
				return stack.getNbt().getCompound("bella:bell").getBoolean("strung");
			} else if (QuiltLoader.isModLoaded("trinkets") && TrinketsApi.getTrinketComponent(provider).isPresent()) {
				List<Pair<SlotReference, ItemStack>> list = TrinketsApi.getTrinketComponent(provider).get().getEquipped(stack -> stack.isOf(Items.BELL));
				return list.stream().anyMatch(pair -> {
					ItemStack stack = pair.getRight();
					if (stack.getNbt() != null) {
						return stack.getNbt().getCompound("bella:bell").getBoolean("strung");
					}
					return false;
				});
			}
		}
		return strung;
	}

	@Override
	public void setStrung(boolean value) {
		this.strung = value;
	}

	@Override
	public void sync() {
		BellaComponents.BELL_COMPONENT.sync(this.provider);
	}
}
