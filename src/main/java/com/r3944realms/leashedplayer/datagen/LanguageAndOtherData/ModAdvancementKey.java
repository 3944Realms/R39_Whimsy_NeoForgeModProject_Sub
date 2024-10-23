package com.r3944realms.leashedplayer.datagen.LanguageAndOtherData;



import com.r3944realms.leashedplayer.LeashedPlayer;

import javax.annotation.Nullable;

public enum ModAdvancementKey {
    LEASH_START("leash_start", null),
    LEASHED_FRIEND("leashed_friend", LEASH_START),
    LEASHED_SELF("leashed_self", LEASH_START),
    LEASH_ARROW("leash_arrow", LEASH_START),
    ADVANCEMENT_LEASH_ARROW("advancement_leash_arrow", LEASH_ARROW),
    FOLLOW_LEASH_ARROW("follow_arrow", LEASH_ARROW),
    DOG_RUNNING_PLAYER("dog_running_player", LEASH_ARROW),
    NO_LEASH("no_leash", LEASH_START),
    ;
    private final String Name;
    @Nullable
    private final ModAdvancementKey Parent;
    ModAdvancementKey(String name, @Nullable ModAdvancementKey parent) {
        this.Name = name;
        this.Parent = parent;
    }

    public @Nullable ModAdvancementKey getParent() {
        return Parent;
    }
    public String getNameKey() {
        return "advancement." + LeashedPlayer.MOD_ID + "." + Name;
    }

    public String getDescKey() {
        return this.getNameKey() + ".desc";
    }
    public String getNameWithNameSpace() {
        return LeashedPlayer.MOD_ID + ":" + this.Name;
    }
}
