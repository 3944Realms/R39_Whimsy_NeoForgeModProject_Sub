package com.r3944realms.leashedplayer.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // 检查当前是否在数据生成阶段
        // 根据需要添加逻辑来控制特定Mixin类的应用
        boolean isRegister = mixinClassName.split("com\\.r3944realms\\.leashedplayer\\.mixin\\.")[1].split("\\.")[0].equals("registry");
        if (isRegister) {
            return isDataGeneration();
        }
        return true; // 其他Mixin类默认应用
    }

    private boolean isDataGeneration() {
        // 使用你的逻辑来检查是否在数据生成阶段
        String environment = System.getProperty("gradle.task");
        return "runData".equals(environment);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
