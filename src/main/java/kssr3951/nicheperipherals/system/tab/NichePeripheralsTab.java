package kssr3951.nicheperipherals.system.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class NichePeripheralsTab extends CreativeTabs {

    private Item item;
    
    public NichePeripheralsTab(String lable) {
        super(lable);
        this.item = Item.getItemFromBlock(Blocks.stone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return this.item;
    }
    
    public void setTabIconItem(Item item) {
        this.item = item;
    }
}
