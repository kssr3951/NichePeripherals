package kssr3951.nicheperipherals.application.enderstoragedyeingmachine;

import codechicken.enderstorage.api.EnderStorageManager;
import codechicken.enderstorage.common.TileFrequencyOwner;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.shared.turtle.core.InteractDirection;
import kssr3951.nicheperipherals.NichePeripherals;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;

public class EnderStorageDyeingMachineCommand1_DyeBlock implements ITurtleCommand {

    private final InteractDirection direction;
    private final Object arguments[];

    public EnderStorageDyeingMachineCommand1_DyeBlock(InteractDirection direction, Object arguments[]) {
        this.direction = direction;
        this.arguments = arguments;
    }
    
    @Override
    public TurtleCommandResult execute(ITurtleAccess turtle) {

        if (null == this.arguments || this.arguments.length < 1 || !(this.arguments[0] instanceof Double)) {
            return TurtleCommandResult.failure("expected integer");
        }
        if (!(1.0 == (Double)this.arguments[0] || 2.0 != (Double)this.arguments[0] || 3.0 != (Double)this.arguments[0])) {
            return TurtleCommandResult.failure("expected integer(1|2|3)");
        }
        int padNum = ((Double)this.arguments[0]).intValue() - 1;
        System.out.println("padNum = " + padNum);
        
        int dir = this.direction.toWorldDir(turtle);
        int newX = turtle.getPosition().posX + Facing.offsetsXForSide[dir];
        int newY = turtle.getPosition().posY + Facing.offsetsYForSide[dir];
        int newZ = turtle.getPosition().posZ + Facing.offsetsZForSide[dir];
        Block block = turtle.getWorld().getBlock(newX, newY, newZ);
        if (block.equals(NichePeripherals.Dependency.es_enderStorage)) {
            System.out.println("    EnderStorage");

            // 本当はクラスは使わない
            int metadata = turtle.getWorld().getBlockMetadata(newX, newY, newZ);
            if (0 == metadata && 1 == metadata) {
                return TurtleCommandResult.failure("Unknown meta.");
            }
            if (1 == metadata) {
                // EnderTankの場合
                padNum = 2 - padNum;
            }
            TileFrequencyOwner tile = (TileFrequencyOwner) turtle.getWorld().getTileEntity(newX, newY, newZ);
            System.out.println("[ender chest] owner  = " + tile.owner);
            System.out.println("[ender chest] freq   = " + tile.freq);
            
            int slotNum = turtle.getSelectedSlot();
            ItemStack stack = turtle.getInventory().getStackInSlot(slotNum);
            if (null == stack) {
                return TurtleCommandResult.failure("Slot is empty.");
            }
            if (!Items.dye.equals(stack.getItem())) {
                return TurtleCommandResult.failure("This is not a dye.");
            }

            {
                int dye = stack.getItemDamage();
                int[] colors = EnderStorageManager.getColoursFromFreq(tile.freq);
                if (colors[padNum] == (~dye & 0xF)) {
                    return TurtleCommandResult.success(new Object[]{ "Same color." });
                }
                colors[padNum] = ~dye & 0xF;
                tile.setFreq(EnderStorageManager.getFreqFromColours(colors));
            }

            // アイテムを減らす
            stack.stackSize --;
            if (stack.stackSize == 0) {
                turtle.getInventory().setInventorySlotContents(slotNum, null);
            }
            return TurtleCommandResult.success();
        } else {
            System.out.println("    not EnderStorage");
            return TurtleCommandResult.failure("not EnderStorage");
        }
    }
}
