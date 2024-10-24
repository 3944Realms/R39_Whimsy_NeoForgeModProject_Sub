package com.r3944realms.leashedplayer.mixin.server;

import com.r3944realms.leashedplayer.content.gamerules.GameruleRegistry;
import com.r3944realms.leashedplayer.content.gamerules.Server.TeleportWithLeashedPlayers;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.Collectors;

import static com.r3944realms.leashedplayer.utils.Logger.logger;
@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {

    @Shadow
    public ServerPlayer player;
    @Unique
    private List<Entity> Pl$LeashPlayers = new ArrayList<>();
    @Inject(method = {"teleport(DDDFFLjava/util/Set;)V"}, at = {@At("HEAD")})
    private void teleportHead(double pX, double pY, double pZ, float pYaw, float pPitch, Set<RelativeMovement> pRelativeSet, CallbackInfo ci) {
        try {
            //獲取Holder
            this.Pl$LeashPlayers = ((PlayerLeashable)this.player).getLeashHolder() != null ? Collections.emptyList() : Objects.requireNonNull(this.player.getServer()).getPlayerList().getPlayers().stream().filter(serverPlayer -> (serverPlayer instanceof PlayerLeashable) && ((PlayerLeashable)serverPlayer).getLeashHolder() == this.player && player != serverPlayer).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Internal Error:",e);
        }
    }
    @Inject(method = {"teleport(DDDFFLjava/util/Set;)V"}, at = {@At("TAIL")})
    private void teleportTail(double pX, double pY, double pZ, float pYaw, float pPitch, Set<RelativeMovement> pRelativeSet, CallbackInfo ci) {
        if(GameruleRegistry.getGameruleBoolValue(this.player.serverLevel(), TeleportWithLeashedPlayers.ID)) {
            for (Entity Pl$LeashPlayer : this.Pl$LeashPlayers) {
                if(Pl$LeashPlayer instanceof ServerPlayer) {
                    if(Pl$LeashPlayer instanceof PlayerLeashable playerLeashable) {
                        playerLeashable.dropLeash(false,false);
                        if(((ServerPlayer) playerLeashable).serverLevel() == this.player.serverLevel()) {
                            ((ServerPlayer) playerLeashable).connection.teleport(pX, pY, pZ, pYaw, pPitch, pRelativeSet);
                        } else {
                            ((ServerPlayer) playerLeashable).teleportTo(this.player.serverLevel(), pX, pY, pZ, pYaw, pPitch);
                            ((ServerPlayer) playerLeashable).stopRiding();
                        }
                        playerLeashable.setLeashedTo(this.player, true);
                    }
                }
            }
        }
    }
    @SuppressWarnings("DiscouragedShift")
    @Inject(method = {"handleMovePlayer"}, at = @At(value = "INVOKE",target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    private void handleMovePlayer(ServerboundMovePlayerPacket pPacket, CallbackInfo ci) {
        if(GameruleRegistry.getGameruleBoolValue(this.player.serverLevel(), TeleportWithLeashedPlayers.ID))
            ci.cancel();
    }
}
