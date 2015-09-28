package kssr3951.nicheperipherals.application.metaplacer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kssr3951.nicheperipherals.application.Const;
import kssr3951.nicheperipherals.system.blocks.BlockEx;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class MetaPlacerBlock extends BlockEx {

    public MetaPlacerBlock(Material material) {
        super(material);
    }

    // =====================================================================================
    // Blockのオーバーライド
    // =====================================================================================
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.peripheralIcon = iconRegister.registerIcon(Const.TEXTURE_PREFIX + "metaPlacer");
    }
}
