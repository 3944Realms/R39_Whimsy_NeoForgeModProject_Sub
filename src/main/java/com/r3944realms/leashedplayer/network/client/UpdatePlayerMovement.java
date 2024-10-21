package com.r3944realms.leashedplayer.network.client;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author qyl27
 * @param operation
 * @param x
 * @param y
 * @param z
 */
public record UpdatePlayerMovement(Operation operation, double x, double y, double z) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdatePlayerMovement> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LeashedPlayer.MOD_ID,"update_player_movement"));
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static final StreamCodec<FriendlyByteBuf, UpdatePlayerMovement> STREAM_CODEC =
            StreamCodec.composite(
                    NeoForgeStreamCodecs.enumCodec(Operation.class), UpdatePlayerMovement::operation,
                    ByteBufCodecs.DOUBLE, UpdatePlayerMovement::x,
                    ByteBufCodecs.DOUBLE, UpdatePlayerMovement::y,
                    ByteBufCodecs.DOUBLE, UpdatePlayerMovement::z,
                    UpdatePlayerMovement::new
            );
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
                    Player player = context.player();
                    switch (operation) {
                        case ADD ->  player.addDeltaMovement(new Vec3(x, y, z));
                        case SET ->   player.setDeltaMovement(new Vec3(x, y, z));
                        case MULTIPLY -> player.addDeltaMovement(player.getDeltaMovement().multiply(x, y, z));
                    }
                }

        );
    }
    public enum Operation {
        SET,
        ADD,
        MULTIPLY
    }
}


