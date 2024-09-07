package com.r3944realms.leashedplayer.mixin.item;

import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LeadItem.class)
public class MixinLeadItem {
    /**
     * 拴住自己的逻辑
     */
    @Inject(
            method = {"bindPlayerMobs"},
            at = @At("HEAD"),
            cancellable = true)
    private static void selfLeash(Player pPlayer, Level pLevel, BlockPos pPos, CallbackInfoReturnable<InteractionResult> cir) {
        List<Leashable> list = LeadItem.leashableInArea(pLevel, pPos, p_353025_ -> p_353025_.getLeashHolder() == pPlayer);
        if (list.isEmpty()) {
            ItemStack mainHandItem = pPlayer.getMainHandItem();
            if (!(mainHandItem.getItem() instanceof LeadItem )) {
                return;
            }
            //非创造模式减少，防止刷物品
            if(!pPlayer.isCreative()) mainHandItem.shrink(1);
            //自己
            Entity leashDataEntity = PlayerLeashable.getLeashDataEntity((ServerPlayer) pPlayer, (ServerLevel) pLevel);
            PlayerLeashable playerLeashable = (PlayerLeashable) pPlayer;
            if(leashDataEntity != null) {
                playerLeashable.dropLeash(true, true);
            }
            //获取拴绳结实体
            LeashFenceKnotEntity leashfenceknotentity = LeashFenceKnotEntity.getOrCreateKnot(pLevel, pPos);
            //播放绳结被放置的声音
            leashfenceknotentity.playPlacementSound();
            //将自己与拴绳结绑定LeashData
            playerLeashable.setLeashedTo(leashfenceknotentity, true);
            pLevel.gameEvent(GameEvent.BLOCK_ATTACH, pPos, GameEvent.Context.of(pPlayer));
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
