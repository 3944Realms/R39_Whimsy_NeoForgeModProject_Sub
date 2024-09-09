package com.r3944realms.leashedplayer.content.criteriaTriggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LeashPlayerTrigger extends SimpleCriterionTrigger<LeashPlayerTrigger.TriggerInstance> {

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }
    public void trigger(ServerPlayer pPlayer, Entity pHolderEntity) {
        this.trigger(pPlayer, pl -> pl.matches(pPlayer, pHolderEntity));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<EntityPredicate> holder) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                EntityPredicate.CODEC.optionalFieldOf("holder").forGetter(TriggerInstance::holder)
                        )
                        .apply(instance, TriggerInstance::new));
//        public static Criterion<TriggerInstance> LeashSelf() {
//            return ModCriteriaTriggers.LEASH_PLAYER_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
//        }
        public static  Criterion<TriggerInstance> LeashPlayer(EntityPredicate.Builder holder) {
            return ModCriteriaTriggers.LEASH_PLAYER_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(holder.build())));
        }

        public boolean matches(ServerPlayer player, Entity holder) {
            PlayerLeashable ppl = (PlayerLeashable) player;
            return ppl.isLeashed() && holder.equals(PlayerLeashable.getLeashDataEntity(player, player.serverLevel())) && this.holder.isPresent() && this.holder.get().matches(player, holder);
        }
    }

}
