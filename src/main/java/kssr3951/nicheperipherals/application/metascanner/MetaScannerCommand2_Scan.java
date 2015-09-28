package kssr3951.nicheperipherals.application.metascanner;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityNote;
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
public class MetaScannerCommand2_Scan implements ITurtleCommand {

    private TurtleSide side;
    private int dir;

    private String scannedBlockName;
    private int scannedMetadata;

    private String scanCode;

    public MetaScannerCommand2_Scan(TurtleSide side, int dir) {
        this.side = side;
        this.dir = dir;
    }

    public String getScannedBlockName() {
        return scannedBlockName;
    }

    public int getScannedMetadata() {
        return scannedMetadata;
    }

    public String getScanCode() {
        return scanCode;
    }

    @Override
    public TurtleCommandResult execute(ITurtleAccess turtle) {

        int newX = turtle.getPosition().posX + Facing.offsetsXForSide[this.dir];
        int newY = turtle.getPosition().posY + Facing.offsetsYForSide[this.dir];
        int newZ = turtle.getPosition().posZ + Facing.offsetsZForSide[this.dir];
        Block block = turtle.getWorld().getBlock(newX, newY, newZ);

        System.out.println("  **** block () : " + Block.blockRegistry.getNameForObject(block));
        
        int metadata = -1;
        System.out.println("Blocks.noteblock = " + Blocks.noteblock.getUnlocalizedName());
        System.out.println("  this block     = " + block.getUnlocalizedName());
        System.out.println("    (use equals) : " + block.equals(Blocks.noteblock));
        System.out.println("    (use ==    ) : " + (block == Blocks.noteblock));
        System.out.println("cable            = " + dan200.computercraft.ComputerCraft.Blocks.cable.getUnlocalizedName());
        if (block.equals(Blocks.noteblock)) {
            System.out.println("    this is a noteblock");
            // 音ブロックだけ特別に
            TileEntityNote tileentitynote = (TileEntityNote)turtle.getWorld().getTileEntity(newX, newY, newZ);
            if (tileentitynote != null) {
                
                metadata = tileentitynote.note;
                System.out.println("      tile is not null / note = " + tileentitynote.note);
            } else {
                System.out.println("      tile is null");
            }
        } else {
            // 他はメタデータ
            metadata = turtle.getWorld().getBlockMetadata(newX, newY, newZ);
        }

        System.out.println("[NichePeripherals scan]block=" + block.getUnlocalizedName() + " /metadata=" + metadata);
        scanCode = MetaScannerUpgrade.getHashForDetectorTurtle(block, metadata);

        if(turtle.getWorld().isAirBlock(newX, newY, newZ)) {
            return TurtleCommandResult.failure("no scan target");
        }

        if(side == TurtleSide.Left) {
            turtle.playAnimation(TurtleAnimation.SwingLeftTool);
        } else {
            turtle.playAnimation(TurtleAnimation.SwingRightTool);
        }

        return TurtleCommandResult.success();
    }
}
