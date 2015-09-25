package kssr3951.nicheperipherals.application.modemcontroller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.block.Block;
import net.minecraft.util.Facing;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class PeripheralModemControllerCommand1_Activate implements ITurtleCommand {

    private int dir;
    private boolean activate;
    private String messageWhenSuccess = "";
    
    /** モデムはあるが、ケーブルがつながっていない場合のメタ値　*/
    private static final List<Integer> META_LIST__NO_CABLE_CONNECTED = Collections.unmodifiableList(Arrays.asList(5, 3, 4, 2, 0, 1));
    
    /** モデムとケーブルがつながっており、ON/OFF可能な状態のメタ値 */
    private static final List<Integer> META_LIST__SWITCHABLE = Collections.unmodifiableList(Arrays.asList(11, 9, 10, 8, 6, 7));
    
    /** ケーブルしかない状態のメタ値 */
    private static final int META_CABLE_ONLY = 13;
    
    public PeripheralModemControllerCommand1_Activate(TurtleSide side, int dir, boolean activate) {

        this.dir = dir;
        this.activate = activate;
    }

    public String getMessageWhenSuccess() {
        return messageWhenSuccess;
    }

    @Override
    public TurtleCommandResult execute(ITurtleAccess turtle) {

        int newX = turtle.getPosition().posX + Facing.offsetsXForSide[this.dir];
        int newY = turtle.getPosition().posY + Facing.offsetsYForSide[this.dir];
        int newZ = turtle.getPosition().posZ + Facing.offsetsZForSide[this.dir];
        Block block = turtle.getWorld().getBlock(newX, newY, newZ);
        if (block.equals(dan200.computercraft.ComputerCraft.Blocks.cable)) {
            
            // ケーブルと有線モデムの組み合わせは、同じ「cable」ブロックのメタ違いになっている。
            // ケーブルだけの場合や、「モデムはあるが、ケーブルがつながっていない場合」は、ON/OFFは行えないのでエラーにする。
            int metadata = turtle.getWorld().getBlockMetadata(newX, newY, newZ);
            if (META_LIST__NO_CABLE_CONNECTED.contains(metadata)) {

                // そこにモデムはあるが、ケーブルがつながっていない場合
                return TurtleCommandResult.failure("A cable isn't connected.");
            }
            if (META_CABLE_ONLY == metadata) {
                // ケーブルだけの場合
                return TurtleCommandResult.failure("No modems.(cable only)");
            }
            if (!META_LIST__SWITCHABLE.contains(metadata)) {
                // 想定していないメタ値が来た場合
                return TurtleCommandResult.failure("No modems.(unknown META :" + metadata + ")");
            }
            // ケーブルのタイルエンティティにON/OFFを切り替えるメソッドがあるので、タイルエンティティを取得して使用する。「togglePeripheralAccess()」
            // 現在のON/OFFは「getConnectedPeripheralName()」で確認する。
            dan200.computercraft.shared.peripheral.modem.TileCable tileCable = (dan200.computercraft.shared.peripheral.modem.TileCable) turtle.getWorld().getTileEntity(newX, newY, newZ);
            String connName = tileCable.getConnectedPeripheralName();
            if (null != connName && !"".equals(connName)) {
                // モデムはONになっている
                if (activate) {
                    // ONにする
                    // すでにONなので何もしないが失敗ではない
                    this.messageWhenSuccess = "The modem is ON already.";
                    return TurtleCommandResult.success();
                } else {
                    // OFFにする
                    tileCable.togglePeripheralAccess();
                    tileCable.markDirty();
                    this.messageWhenSuccess = "A toggle was done.";
                    return TurtleCommandResult.success();
                }
            } else {
                // モデムはOFFになっている
                if (activate) {
                    // ONにする
                    tileCable.togglePeripheralAccess();
                    tileCable.markDirty();
                    this.messageWhenSuccess = "A toggle was done.";
                    return TurtleCommandResult.success();
                } else {
                    // OFFにする
                    // 既にOFFなので何もしないが失敗ではない
                    this.messageWhenSuccess = "The modem is OFF already.";
                    return TurtleCommandResult.success();
                }
            }
        } else {
            // モデムではない
            return TurtleCommandResult.failure("No modems.");
        }
    }
}
