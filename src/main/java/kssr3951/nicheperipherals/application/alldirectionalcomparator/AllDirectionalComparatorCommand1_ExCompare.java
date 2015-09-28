package kssr3951.nicheperipherals.application.alldirectionalcomparator;

import static kssr3951.nicheperipherals.application.Const.SIDE_TEXT_BACK;
import static kssr3951.nicheperipherals.application.Const.SIDE_TEXT_BOTTOM;
import static kssr3951.nicheperipherals.application.Const.SIDE_TEXT_FRONT;
import static kssr3951.nicheperipherals.application.Const.SIDE_TEXT_LEFT;
import static kssr3951.nicheperipherals.application.Const.SIDE_TEXT_RIGHT;
import static kssr3951.nicheperipherals.application.Const.SIDE_TEXT_TOP;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import cpw.mods.fml.relauncher.ReflectionHelper;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class AllDirectionalComparatorCommand1_ExCompare implements ITurtleCommand {

    public enum CommandVariation { COMPARE_EX, COMPARE_NEGATIVE, COMPARE_POSITIVE }

    private CommandVariation variation;
    private Object[] arguments;

    public AllDirectionalComparatorCommand1_ExCompare(CommandVariation variation, Object[] arguments) {
        this.variation = variation;
        this.arguments = arguments;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TurtleCommandResult execute(ITurtleAccess turtle) {
        String argDir = null;
        Collection<Object> argSlots = null;
        {
            System.out.println("arguments.length = " + arguments.length);
            if (!(arguments.length == 2 && arguments[0] instanceof String &&
                ((variation == CommandVariation.COMPARE_EX && arguments[1] instanceof Double) ||
                 (variation != CommandVariation.COMPARE_EX && arguments[1] instanceof HashMap)))) {
                return TurtleCommandResult.failure("wrong arguments");
            }
            System.out.println("arguments[0]     = " + arguments[0].getClass().getName());
            System.out.println("arguments[1]     = " + arguments[1].getClass().getName());
            System.out.println("commandVariation = " + variation);

            argDir = (String)arguments[0];
            final String checkStr = "/" + SIDE_TEXT_FRONT + "/" + SIDE_TEXT_BACK + "/" + SIDE_TEXT_LEFT
                                  + "/" + SIDE_TEXT_RIGHT + "/" + SIDE_TEXT_TOP  + "/" + SIDE_TEXT_BOTTOM + "/";
            if (-1 == checkStr.indexOf("/" + argDir + "/")) {
                return TurtleCommandResult.failure("wrong direction");
            }

            if (variation == CommandVariation.COMPARE_EX) {
                argSlots = new ArrayList<Object>();
                argSlots.add(arguments[1]);
            } else if (variation == CommandVariation.COMPARE_NEGATIVE ||
                       variation == CommandVariation.COMPARE_POSITIVE) {
                argSlots = ((HashMap<Object, Object>)arguments[1]).values();
            }

            if (0 == argSlots.size()) {
                return TurtleCommandResult.failure("slots are not specified");
            }
            for(Object val : argSlots) {
                if (!(val instanceof Double)) {
                    return TurtleCommandResult.failure("invalid slot");
                }
                int slot = ((Double)val).intValue();
                if (slot < 1 || 16 < slot) {
                    return TurtleCommandResult.failure("specify the slot number in 1 to 16");
                }
            }
        }

        ForgeDirection forgeDir = ForgeDirection.getOrientation(turtle.getDirection());
        {
            if (SIDE_TEXT_FRONT.equals(argDir)) {
                // 何もしない
            } else if (SIDE_TEXT_LEFT.equals(argDir)) {
                forgeDir = forgeDir.getRotation(ForgeDirection.DOWN);
            } else if (SIDE_TEXT_RIGHT.equals(argDir)) {
                forgeDir = forgeDir.getRotation(ForgeDirection.UP);
            } else if (SIDE_TEXT_BACK.equals(argDir)) {
                forgeDir = forgeDir.getOpposite();
            } else if (SIDE_TEXT_TOP.equals(argDir)) {
                forgeDir = ForgeDirection.UP;
            } else if (SIDE_TEXT_BOTTOM.equals(argDir)) {
                forgeDir = ForgeDirection.DOWN;
            }
        }

        System.out.println("  forgeDir = " + forgeDir);

        {
            boolean falseAll = true;
            boolean trueExists = false;
            TurtleCommandResult rslt = null;
            for (Object val : argSlots) {
                int slot = ((Double)val).intValue();

                System.out.println("    slot = " + slot);

                rslt = compare(turtle, forgeDir.ordinal(), slot);
                if (rslt.isSuccess()) {
                    System.out.println("      -> success");
                    falseAll = false;
                    trueExists = true;
                } else {
                    System.out.println("      -> fail");
                }
            }
            if (variation == CommandVariation.COMPARE_EX) {
                return rslt;
            } else if (variation == CommandVariation.COMPARE_NEGATIVE) {
                return falseAll ? TurtleCommandResult.success() : TurtleCommandResult.failure();
            } else if (variation == CommandVariation.COMPARE_POSITIVE) {
                return trueExists ? TurtleCommandResult.success() : TurtleCommandResult.failure();
            }
        }
        return TurtleCommandResult.failure("The situation which cannot happen.");
    }

    private static ChunkCoordinates moveCoords(ChunkCoordinates coordinates, int dir) {
        return new ChunkCoordinates(coordinates.posX + Facing.offsetsXForSide[dir], coordinates.posY + Facing.offsetsYForSide[dir], coordinates.posZ + Facing.offsetsZForSide[dir]);
    }
    
    private static boolean isBlockInWorld(World world, ChunkCoordinates coordinates) {
        return coordinates.posY >= 0 && coordinates.posY < world.getHeight();
    }
    
    private static TurtleCommandResult compare(ITurtleAccess turtle, int worldDir, int slotNo) {
        
        int direction = worldDir;

        ItemStack selectedStack = turtle.getInventory().getStackInSlot(slotNo - 1);

        World world = turtle.getWorld();
        ChunkCoordinates oldPosition = turtle.getPosition();
        ChunkCoordinates newPosition = moveCoords(oldPosition, direction);

        System.out.println("         turtlePos : x = " + oldPosition.posX + " / y = " + oldPosition.posY + " / z = " + oldPosition.posZ);
        System.out.println("         cmparePos : x = " + newPosition.posX + " / y = " + newPosition.posY + " / z = " + newPosition.posZ);

        ItemStack lookAtStack = null;
        if ((isBlockInWorld(world, newPosition))
                && (!(world.isAirBlock(newPosition.posX,
                        newPosition.posY, newPosition.posZ)))) {
            Block lookAtBlock = world.getBlock(newPosition.posX,
                    newPosition.posY, newPosition.posZ);
            if ((lookAtBlock != null)
                    && (!(lookAtBlock.isAir(world, newPosition.posX,
                            newPosition.posY,
                            newPosition.posZ)))) {
                int lookAtMetadata = world.getBlockMetadata(
                        newPosition.posX, newPosition.posY,
                        newPosition.posZ);
                if (!(lookAtBlock.hasTileEntity(lookAtMetadata))) {
                    try {
                        Method method = ReflectionHelper.findMethod(
                                Block.class, lookAtBlock, new String[] {
                                        "func_149644_j", "j",
                                        "createStackedBlock" },
                                new Class[] { Integer.TYPE });

                        if (method != null) {
                            lookAtStack = (ItemStack) method.invoke(
                                    lookAtBlock, new Object[] { Integer
                                            .valueOf(lookAtMetadata) });
                        }
                    } catch (Exception e) {
                    }
                }

                for (int i = 0; (i < 5) && (lookAtStack == null); ++i) {
                    @SuppressWarnings("rawtypes")
                    ArrayList drops = lookAtBlock.getDrops(world,
                            newPosition.posX,
                            newPosition.posY,
                            newPosition.posZ, lookAtMetadata, 0);
                    if ((drops == null) || (drops.size() <= 0))
                        continue;
                    @SuppressWarnings("rawtypes")
                    Iterator it = drops.iterator();
                    while (it.hasNext()) {
                        ItemStack drop = (ItemStack) it.next();
                        if (drop.getItem() == Item
                                .getItemFromBlock(lookAtBlock)) {
                            lookAtStack = drop;
                            break;
                        }
                    }
                }

                if (lookAtStack == null) {
                    Item item = Item.getItemFromBlock(lookAtBlock);
                    if (item.getHasSubtypes()) {
                        lookAtStack = new ItemStack(item, 1, lookAtMetadata);
                    } else {
                        lookAtStack = new ItemStack(item, 1, 0);
                    }
                }
            }
        }

        if ((selectedStack == null) && (lookAtStack == null)) {
            return TurtleCommandResult.success();
        }

        if (selectedStack == null) {
            System.out.println("selectedStack is null!");
        } else {
            System.out.println("selectedStack.getItem() = " + selectedStack.getItem());
            if (selectedStack.getItem() != null) {
                System.out.println("selectedStack.getItem().getUnlocalizedName() =[" + selectedStack.getItem().getUnlocalizedName() + "]");
            }
        }
        if (lookAtStack == null) {
            System.out.println("lookAtStack is null!");
        } else {
            System.out.println("lookAtStack.getItem() = " + lookAtStack.getItem());
            if (lookAtStack.getItem() != null) {
                System.out.println("lookAtStack.getItem().getUnlocalizedName() =[" + lookAtStack.getItem().getUnlocalizedName() + "]");
            }
        }


        if ((selectedStack != null) && (lookAtStack != null)
                && (selectedStack.getItem() == lookAtStack.getItem())) {
            if (!(selectedStack.getHasSubtypes())) {
                return TurtleCommandResult.success();
            }
            if (selectedStack.getItemDamage() == lookAtStack.getItemDamage()) {
                return TurtleCommandResult.success();
            }
            if (selectedStack.getUnlocalizedName().equals(lookAtStack.getUnlocalizedName())) {
                return TurtleCommandResult.success();
            }
        }
        return TurtleCommandResult.failure();
    }
}
