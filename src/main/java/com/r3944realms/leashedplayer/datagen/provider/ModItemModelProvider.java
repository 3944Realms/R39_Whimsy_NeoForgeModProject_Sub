package com.r3944realms.leashedplayer.datagen.provider;


import com.r3944realms.leashedplayer.datagen.LanguageAndOtherData.ModLangKeyValue;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItemModelProvider extends ItemModelProvider {
    private static List<Item> objectList;
    public ModItemModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper) {
        super(output, modId, existingFileHelper);
        objectList = new ArrayList<>();
        init();
    }
    private void init() {
        for(ModLangKeyValue obj : ModLangKeyValue.values()) {
            if(!obj.isDefaultItem()) continue;
            objectList.add(obj.getItem());
        }
    }
    /**
     * @implNote 先有纹理才会成功构建
     */
    private void DefaultModItemModelRegister() {
        objectList.forEach(this::basicItem);
    }
    private void AdvancedModItemModelRegister() {
        /*手动生成更快，其实*/
    }

    @Override
    protected void registerModels() {
        // 注册默认材质物品模型
        DefaultModItemModelRegister();
        // 注册进阶材质物品模型（非模板大量需要，可以用BlockBench生成的就行了）
        AdvancedModItemModelRegister();
    }
}
