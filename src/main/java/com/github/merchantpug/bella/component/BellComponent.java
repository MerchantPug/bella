package com.github.merchantpug.bella.component;

import com.github.merchantpug.bella.registry.BellaComponents;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;

public class BellComponent implements IBellComponent {
	private boolean bell = false;
	private final AnimalEntity provider;

	public BellComponent(AnimalEntity provider) {
		this.provider = provider;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.setBell(tag.getBoolean("has_bell"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("has_bell", this.bell);
	}

	@Override
	public boolean hasBell() {
		return bell;
	}

	@Override
	public void setBell(boolean value) {
		this.bell = value;
		BellaComponents.BELL_COMPONENT.sync(provider);
	}
}
