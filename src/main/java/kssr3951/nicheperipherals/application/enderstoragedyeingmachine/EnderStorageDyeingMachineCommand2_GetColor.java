package kssr3951.nicheperipherals.application.enderstoragedyeingmachine;

import codechicken.enderstorage.api.EnderStorageManager;
import codechicken.enderstorage.common.TileFrequencyOwner;
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
public class EnderStorageDyeingMachineCommand2_GetColor implements ITurtleCommand {

    private static final String[] COLOR_VALUE_TO_DYE_NAME = new String[]{
            "Bone Meal",
            "Orange Dye",
            "Magenta Dye",
            "Light Blue Dye",
            "Dandelion Yellow",
            "Lime Dye",
            "Pink Dye",
            "Gray Dye",
            "Light Gray Dye",
            "Cyan Dye",
            "Purple Dye",
            "Lapis Lazuli",
            "Cocoa Beans",
            "Cactus Green",
            "Rose Red",
            "Ink Sac",
    };

    private static final String[] COLOR_VALUE_TO_COLOR_NAME = new String[]{
            "White",
            "Orange",
            "Magenta",
            "LightBlue",
            "Yellow",
            "Lime",
            "Pink",
            "Gray",
            "LightGray",
            "Cyan",
            "Purple",
            "Blue",
            "Brown",
            "Green",
            "Red",
            "Black",
    };

    private final InteractDirection direction;
    private boolean dyeNameOrColorName;

    public EnderStorageDyeingMachineCommand2_GetColor(InteractDirection direction, boolean dyeNameOrColorName) {
        this.direction = direction;
        this.dyeNameOrColorName = dyeNameOrColorName;
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
            String[] colorNames = new String[3];
            // 本当はクラスは使わない
            int metadata = turtle.getWorld().getBlockMetadata(newX, newY, newZ);
            if (0 != metadata && 1 != metadata) {
                return TurtleCommandResult.failure("Unknown meta.");
            }
            TileFrequencyOwner tile = (TileFrequencyOwner) turtle.getWorld().getTileEntity(newX, newY, newZ);
            int[] colors = EnderStorageManager.getColoursFromFreq(tile.freq);
            colorNames[0] = dyeNameOrColorName ? COLOR_VALUE_TO_DYE_NAME[colors[0]] : COLOR_VALUE_TO_COLOR_NAME[colors[0]];
            colorNames[1] = dyeNameOrColorName ? COLOR_VALUE_TO_DYE_NAME[colors[1]] : COLOR_VALUE_TO_COLOR_NAME[colors[1]];
            colorNames[2] = dyeNameOrColorName ? COLOR_VALUE_TO_DYE_NAME[colors[2]] : COLOR_VALUE_TO_COLOR_NAME[colors[2]];
            if (1 == metadata) {
                // EnderTankの場合
                String tmp = colorNames[2];
                colorNames[2] = colorNames[1];
                colorNames[0] = tmp;
            }
            return TurtleCommandResult.success(colorNames);
        } else {
            System.out.println("    not EnderStorage");
            return TurtleCommandResult.failure("not EnderStorage");
        }
    }
}
