package com.r3944realms.leashedplayer.client.renders.gui;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.client.processBar.IProcessBar;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.Map;

@EventBusSubscriber(modid = LeashedPlayer.MOD_ID, value = Dist.CLIENT)
public class AdaptiveGuiRendererHandler {
    private static final Map<IProcessBar, IProcessBarRenderer<? extends IProcessBar>> processBars = Maps.newConcurrentMap();
    public static void addProcessBar(IProcessBar processBar, IProcessBarRenderer<? extends IProcessBar> renderer) {
        processBars.put(processBar, renderer);
    }
    public static IProcessBarRenderer<? extends IProcessBar> getProcessBarRenderer(IProcessBar processBar) {
        return processBars.get(processBar);
    }

    @SubscribeEvent
    public static void onRendererLevel(RenderGuiEvent.Pre event) {
        PoseStack matrixStack = event.getGuiGraphics().pose();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        processBars.keySet().forEach(
                processBar -> {
                    IProcessBarRenderer<? extends IProcessBar> renderer = processBars.get(processBar);
                    if(processBar.shouldRender()) {
                        renderer.renderProcessBar(matrixStack, guiGraphics);
                    } else {
                        if (processBar.aliveCount() <= 0) {//计数为非正时移除
                            processBars.remove(processBar);
                        }
                    }
                }
        );
    }
}
