package com.r3944realms.leashedplayer.datagen.provider;

import com.r3944realms.leashedplayer.datagen.LanguageAndOtherData.ModLangKeyValue;
import com.r3944realms.leashedplayer.utils.Enum.LanguageEnum;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.r3944realms.leashedplayer.datagen.LanguageAndOtherData.ModLangKeyValue.getLan;


public class ModLanguageProvider extends LanguageProvider {
    private final LanguageEnum Language;
    private final Map<String, String> LanKeyMap;
    private static final List<String> objects = new ArrayList<>();
    public ModLanguageProvider(PackOutput output, String modId, LanguageEnum Lan) {
        super(output, modId, Lan.local);
        this.Language = Lan;
        LanKeyMap = new HashMap<>();
        init();
    }
    private void init() {
        for (ModLangKeyValue key : ModLangKeyValue.values()) {
            addLang(key.getKey(), getLan(Language, key));
        }
    }
    private void addLang(String Key, String value) {
        if(!objects.contains(Key)) objects.add(Key);
        LanKeyMap.put(Key, value);
    }

    @Override
    protected void addTranslations() {
        objects.forEach(key -> add(key,LanKeyMap.get(key)));
    }
}
