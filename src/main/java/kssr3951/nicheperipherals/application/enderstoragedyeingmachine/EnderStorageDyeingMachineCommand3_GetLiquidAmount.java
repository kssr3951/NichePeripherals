package kssr3951.nicheperipherals.application.enderstoragedyeingmachine;

import codechicken.enderstorage.storage.liquid.TileEnderTank;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.shared.turtle.core.InteractDirection;
import kssr3951.nicheperipherals.NichePeripherals;
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
public class EnderStorageDyeingMachineCommand3_GetLiquidAmount implements ITurtleCommand {

    private final InteractDirection direction;

    public EnderStorageDyeingMachineCommand3_GetLiquidAmount(InteractDirection direction) {

        this.direction = direction;
    }

    @Override
    public TurtleCommandResult execute(ITurtleAccess turtle) {

        int dir = this.direction.toWorldDir(turtle);
        int newX = turtle.getPosition().posX + Facing.offsetsXForSide[dir];
        int newY = turtle.getPosition().posY + Facing.offsetsYForSide[dir];
        int newZ = turtle.getPosition().posZ + Facing.offsetsZForSide[dir];
        Block block = turtle.getWorld().getBlock(newX, newY, newZ);
        if (block.equals(NichePeripherals.Dependency.es_enderStorage)) {
            System.out.println("    EnderStorage");
            // 本当はクラスは使わない
            int metadata = turtle.getWorld().getBlockMetadata(newX, newY, newZ);
            if (1 == metadata) {
                TileEnderTank tile = (TileEnderTank) turtle.getWorld().getTileEntity(newX, newY, newZ);
                System.out.println("[ender tank ] owner  = " + tile.owner);
                System.out.println("[ender tank ] freq   = " + tile.freq);
                System.out.println("[ender tank ] amount = " + tile.getStorage().getFluid().amount);
                
                // EnderTank内の液体の量を返す
                return TurtleCommandResult.success(new Object[]{ tile.getStorage().getFluid().amount });
            } else {
                return TurtleCommandResult.failure("This is not a EnderTank");
            }
        } else {
            System.out.println("    not EnderStorage");
            return TurtleCommandResult.failure("This is not a EnderStorage");
        }
    }
}
