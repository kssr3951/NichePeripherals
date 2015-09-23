package kssr3951.nicheperipherals.application.extensioncube;

import kssr3951.nicheperipherals.system.blocks.BlockEx;
import net.minecraft.block.material.Material;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class BlockExtensionCube extends BlockEx {

	public BlockExtensionCube(Material material) {
		super(material);
	}

	// =====================================================================================
	// Blockのオーバーライド
	// =====================================================================================
	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int damageDropped(int metadata) {
		return metadata;
	}
}
