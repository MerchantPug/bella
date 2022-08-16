package com.github.merchantpug.bella.component;

import com.github.merchantpug.bella.registry.BellaComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class BellComponent implements IBellComponent, AutoSyncedComponent {
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
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
		buf.writeBoolean(this.bell);
	}

	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		this.bell = buf.readBoolean();
	}

	@Override
	public boolean hasBell() {
		return bell;
	}

	@Override
	public void setBell(boolean value) {
		this.bell = value;
	}

	@Override
	public void setBellAndSync(boolean value) {
		this.bell = value;
		BellaComponents.BELL_COMPONENT.sync(this.provider);
	}
}
