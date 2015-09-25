package kssr3951.nicheperipherals.system.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import kssr3951.nicheperipherals.system.blocks.BlockEx;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public abstract class RenderEx implements ISimpleBlockRenderingHandler {

    protected int renderId;
    protected BlockEx subjectBlock;
    
    public static RenderEx newInstance(@SuppressWarnings("rawtypes") Class renderExClass, int renderId, BlockEx subjectBlock) {
        RenderEx renderEx;
        try {
            renderEx = (RenderEx)renderExClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        renderEx.renderId = renderId;
        renderEx.subjectBlock = subjectBlock;
        return renderEx;
    }

    // =====================================================================================
    // ISimpleBlockRenderingHandlerの実装
    // =====================================================================================
    @Override
    public final int getRenderId() {
        return this.renderId;
    }
}
