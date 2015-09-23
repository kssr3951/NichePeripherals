package kssr3951.nicheperipherals.application.metaplacer;

import kssr3951.nicheperipherals.system.render.RenderEx;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
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
public class RenderBlockMetaPlacer extends RenderEx {

	public RenderBlockMetaPlacer() {
	}

	// =====================================================================================
	// ISimpleBlockRenderingHandlerの実装
	// =====================================================================================
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		
		// 中身
//		renderer.setOverrideBlockTexture(renderer.getBlockIcon(this.subjectBlock.getNakamiBlock()));
		renderer.setOverrideBlockTexture(this.subjectBlock.getPeripheralIcon());
//		renderer.setRenderBounds(0.2D, 0.2D, 0.2D, 0.8D, 0.8D, 0.8D);
		renderer.setRenderBounds(0.14D, 0.18D, 0.14D, 0.86D, 0.9D, 0.86D);
		this.renderStarndardInventoryBlock(block, 0, renderer, 0.6F);

		// 外側
		renderer.setOverrideBlockTexture(renderer.getBlockIcon(this.subjectBlock.getSotogawaBlock()));
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		this.renderStarndardInventoryBlock(block, 0, renderer, 1.0F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		
		// 中身
//		renderer.setOverrideBlockTexture(renderer.getBlockIcon(this.subjectBlock.getNakamiBlock()));
		renderer.setOverrideBlockTexture(this.subjectBlock.getPeripheralIcon());
//		renderer.setRenderBounds(0.2D, 0.2D, 0.2D, 0.8D, 0.8D, 0.8D);
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

	// =====================================================================================
	// ISimpleBlockRenderingHandler用のヘルパ
	// =====================================================================================
	private void renderStarndardInventoryBlock(Block block, int metadata, RenderBlocks renderer, float scale) {

		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tessellator.draw();

		renderer.clearOverrideBlockTexture();
	}
}
