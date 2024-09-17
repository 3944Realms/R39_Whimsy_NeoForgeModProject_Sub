package com.r3944realms.leashedplayer.content.items;

import com.r3944realms.leashedplayer.client.processBar.IProcessBar;
import com.r3944realms.leashedplayer.client.processBar.TestProcessBar;
import com.r3944realms.leashedplayer.client.renders.AdaptiveGuiRendererHandler;
import com.r3944realms.leashedplayer.client.renders.IFadingProcessBarRenderer;
import com.r3944realms.leashedplayer.client.renders.IProcessBarRenderer;
import com.r3944realms.leashedplayer.client.renders.TestProcessBarRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TestItem extends Item {
    public TestProcessBar testProcessBar = null;
    public TestItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(!pLevel.isClientSide()){
            //some lo
        }
        else {
            if (testProcessBar == null) {
                testProcessBar = new TestProcessBar();
                testProcessBar.setRenderStatus(true);
                TestProcessBarRenderer testProcessBarRenderer = new TestProcessBarRenderer(testProcessBar);
                AdaptiveGuiRendererHandler.addProcessBar(testProcessBar, testProcessBarRenderer);
            } else {
                if(testProcessBar.getCurrentProcess() >= testProcessBar.getMaxProcess()) {
                    testProcessBar.increase();
                    IProcessBarRenderer<? extends IProcessBar> processBarRenderer = AdaptiveGuiRendererHandler.getProcessBarRenderer(testProcessBar);
                    ((IFadingProcessBarRenderer<?>)processBarRenderer).setFadingOut(true);
                    testProcessBar = null;
                } else {
                    testProcessBar.increase();
                }
            }

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
