package kssr3951.nicheperipherals.application.modemcontroller;

import kssr3951.nicheperipherals.system.render.RenderEx;
import kssr3951.nicheperipherals.system.render.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class RenderBlockModemController extends RenderEx {

    public RenderBlockModemController() {
    }

    // =====================================================================================
    // ISimpleBlockRenderingHandlerの実装
    // =====================================================================================
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        
        // 中身
        renderer.setOverrideBlockTexture(this.subjectBlock.getPeripheralIcon());
        renderer.setRenderBounds(0.14D, 0.18D, 0.14D, 0.86D, 0.9D, 0.86D);
        RenderHelper.renderStarndardInventoryBlock(block, 0, renderer, 0.6F);

        // 外側
        renderer.setOverrideBlockTexture(renderer.getBlockIcon(this.subjectBlock.getSotogawaBlock()));
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        RenderHelper.renderStarndardInventoryBlock(block, 0, renderer, 1.0F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        
        // 中身
        renderer.setOverrideBlockTexture(this.subjectBlock.getPeripheralIcon());
        renderer.setRenderBounds(0.14D, 0.18D, 0.14D, 0.86D, 0.9D, 0.86D);
        renderer.renderAllFaces = true;
        renderer.renderStandardBlock(block, x, y, z);

        // 外側
        renderer.setOverrideBlockTexture(renderer.getBlockIcon(this.subjectBlock.getSotogawaBlock()));
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.renderAllFaces = false;
        renderer.clearOverrideBlockTexture();

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }
}
