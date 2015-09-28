package kssr3951.nicheperipherals.application.sclclient;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import kssr3951.nicheperipherals.system.peripheral.TurtleUpgradeEx;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityCommandBlock;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class SclClientUpgrade extends TurtleUpgradeEx {

    public SclClientUpgrade() {
    }
    
    @Override
    public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        
        SclClientPeripheral peripheral = (SclClientPeripheral) super.createPeripheral(turtle, side);
        peripheral.setCommandBlock((TileEntityCommandBlock)Blocks.command_block.createTileEntity(turtle.getWorld(), 0));
        return peripheral;
    }
}
