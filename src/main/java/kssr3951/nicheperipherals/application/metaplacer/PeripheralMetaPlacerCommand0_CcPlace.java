// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   TurtlePlaceCommand.java

package kssr3951.nicheperipherals.application.metaplacer;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.shared.turtle.core.InteractDirection;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.core.TurtlePlayer;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.IEntityDropConsumer;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemLilyPad;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

// Referenced classes of package dan200.computercraft.shared.turtle.core:
//            InteractDirection, TurtleBrain, TurtlePlayer

public class PeripheralMetaPlacerCommand0_CcPlace implements ITurtleCommand {

    public PeripheralMetaPlacerCommand0_CcPlace(InteractDirection direction, Object arguments[]) {
        this.m_direction = direction;
        this.m_extraArguments = arguments;
    }
    
    private static int level = 0;
    private static String lv() {
        final String SPACE = "                                                                   ";
        final int RATE = 4;
        if ((level * RATE) - 1 <= SPACE.length() - 1) {
            return SPACE.substring(0, level * RATE);
        } else {
            return SPACE;
        }
    }

    @Override
    public TurtleCommandResult execute(ITurtleAccess turtle) {
        try { //☆
            level = 0; //☆
            
            System.out.println(lv() + "====================================================");
            System.out.println(lv() + "==");
            System.out.println(lv() + "== PeripheralMetaPlacerCommand0_CcPlace");
            System.out.println(lv() + "==");
            System.out.println(lv() + "====================================================");
            System.out.println(lv() + "-----------------------------------");
            System.out.println(lv() + "-- execute()");
            System.out.println(lv() + "-----------------------------------");
            ItemStack stack = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
            if (stack == null) {
                System.out.println(lv() + "execute|1|"); //☆
                return TurtleCommandResult.failure("No items to place");
            } else {
                System.out.println(lv() + "execute|2|"); //☆
            }
            int direction = this.m_direction.toWorldDir(turtle);
            World world = turtle.getWorld();
            ChunkCoordinates coordinates = WorldUtil.moveCoords(turtle.getPosition(), direction);
            Block previousBlock;
            int previousMetadata;
            if (WorldUtil.isBlockInWorld(world, coordinates)) {
                System.out.println(lv() + "execute|3|"); //☆
                previousBlock = world.getBlock(coordinates.posX, coordinates.posY, coordinates.posZ);
                previousMetadata = world.getBlockMetadata(coordinates.posX, coordinates.posY, coordinates.posZ);
            } else {
                System.out.println(lv() + "execute|4|"); //☆
                previousBlock = null;
                previousMetadata = -1;
            }
            String errorMessage[] = new String[1];
            ItemStack remainder = deploy(stack, turtle, direction, this.m_extraArguments, errorMessage);
            if (remainder != stack) {
                System.out.println(lv() + "execute|5|"); //☆
                turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), remainder);
                turtle.getInventory().markDirty();
                if ((turtle instanceof TurtleBrain) && previousBlock != null) {
                    System.out.println(lv() + "execute|6|"); //☆
                    TurtleBrain brain = (TurtleBrain) turtle;
                    brain.saveBlockChange(coordinates, previousBlock, previousMetadata);
                } else {
                    System.out.println(lv() + "execute|7|"); //☆
                    
                }
                turtle.playAnimation(TurtleAnimation.Wait);
                return TurtleCommandResult.success();
            } else {
                System.out.println(lv() + "execute|8|"); //☆
                
            }
            if (errorMessage[0] != null) {
                System.out.println(lv() + "execute|9|"); //☆
                return TurtleCommandResult.failure(errorMessage[0]);
            } else {
                System.out.println(lv() + "execute|10|"); //☆
                
            }
            if (stack.getItem() instanceof ItemBlock) {
                System.out.println(lv() + "execute|11|"); //☆
                return TurtleCommandResult.failure("Cannot place block here");
            } else {
                System.out.println(lv() + "execute|12|"); //☆
                return TurtleCommandResult.failure("Cannot place item here");
            }
        } finally { //☆
            level -= 1; //☆
        } //☆
    }

    public static ItemStack deploy(ItemStack stack, ITurtleAccess turtle, int direction, Object extraArguments[],
            String o_errorMessage[]) {
        try { //☆
            level += 1; //☆
            
            System.out.println(lv() + "-----------------------------------");
            System.out.println(lv() + "-- deploy()");
            System.out.println(lv() + "-----------------------------------");
            ChunkCoordinates playerPosition = WorldUtil.moveCoords(turtle.getPosition(), direction);
            TurtlePlayer turtlePlayer = createPlayer(turtle, playerPosition, direction);
            ItemStack remainder = deployOnEntity(stack, turtle, turtlePlayer, direction, extraArguments, o_errorMessage);
            if (remainder != stack) {
                System.out.println(lv() + "deploy|1|"); //☆
                return remainder;
            } else {
                System.out.println(lv() + "deploy|2|"); //☆
                
            }
            ChunkCoordinates position = turtle.getPosition();
            ChunkCoordinates newPosition = WorldUtil.moveCoords(position, direction);
            remainder = deployOnBlock(stack, turtle, turtlePlayer, newPosition, Facing.oppositeSide[direction],
                    extraArguments, true, o_errorMessage);
            if (remainder != stack) {
                System.out.println(lv() + "deploy|3|"); //☆
                return remainder;
            } else {
                System.out.println(lv() + "deploy|4|"); //☆
                
            }
            remainder = deployOnBlock(stack, turtle, turtlePlayer, WorldUtil.moveCoords(newPosition, direction),
                    Facing.oppositeSide[direction], extraArguments, false, o_errorMessage);
            if (remainder != stack) {
                System.out.println(lv() + "deploy|5|"); //☆
                return remainder;
            } else {
                System.out.println(lv() + "deploy|6|"); //☆
                
            }
            if (direction >= 2) {
                System.out.println(lv() + "deploy|7|"); //☆
                remainder = deployOnBlock(stack, turtle, turtlePlayer, WorldUtil.moveCoords(newPosition, 0), 1,
                        extraArguments, false, o_errorMessage);
                if (remainder != stack) {
                    System.out.println(lv() + "deploy|8|"); //☆
                    return remainder;
                } else {
                    System.out.println(lv() + "deploy|9|"); //☆
                    
                }
            } else {
                System.out.println(lv() + "deploy|10|"); //☆
                
            }
            remainder = deployOnBlock(stack, turtle, turtlePlayer, position, direction, extraArguments, false,
                    o_errorMessage);
            if (remainder != stack) {
                System.out.println(lv() + "deploy|11|"); //☆
                return remainder;
            } else {
                System.out.println(lv() + "deploy|12|"); //☆
                return stack;
            }
        } finally { //☆
            level -= 1; //☆
        } //☆
    }

    public static TurtlePlayer createPlayer(ITurtleAccess turtle, ChunkCoordinates position, int direction) {
        try { //☆
            level += 1; //☆
            
            System.out.println(lv() + "-----------------------------------");
            System.out.println(lv() + "-- createPlayer");
            System.out.println(lv() + "-----------------------------------");
            TurtlePlayer turtlePlayer = new TurtlePlayer((WorldServer) turtle.getWorld());
            orientPlayer(turtle, turtlePlayer, position, direction);
            return turtlePlayer;
        } finally { //☆
            level -= 1; //☆
        } //☆
    }

    public static void orientPlayer(ITurtleAccess turtle, TurtlePlayer turtlePlayer, ChunkCoordinates position,
            int direction) {
        try { //☆
            level += 1; //☆
            
            System.out.println(lv() + "-----------------------------------");
            System.out.println(lv() + "-- orientPlayer");
            System.out.println(lv() + "-----------------------------------");
            turtlePlayer.posX = position.posX + 0.5D;
            turtlePlayer.posY = position.posY + 0.5D;
            turtlePlayer.posZ = position.posZ + 0.5D;
            if (turtle.getPosition().equals(position)) {
                System.out.println(lv() + "orientPlayer|1|"); //☆
                turtlePlayer.posX += 0.47999999999999998D * Facing.offsetsXForSide[direction];
                turtlePlayer.posY += 0.47999999999999998D * Facing.offsetsYForSide[direction];
                turtlePlayer.posZ += 0.47999999999999998D * Facing.offsetsZForSide[direction];
            } else {
                System.out.println(lv() + "orientPlayer|2|"); //☆
                
            }
            if (direction > 2) {
                System.out.println(lv() + "orientPlayer|3|"); //☆
                turtlePlayer.rotationYaw = DirectionUtil.toYawAngle(direction);
                turtlePlayer.rotationPitch = 0.0F;
            } else {
                System.out.println(lv() + "orientPlayer|4|"); //☆
                turtlePlayer.rotationYaw = DirectionUtil.toYawAngle(turtle.getDirection());
                turtlePlayer.rotationPitch = DirectionUtil.toPitchAngle(direction);
            }
            turtlePlayer.prevPosX = turtlePlayer.posX;
            turtlePlayer.prevPosY = turtlePlayer.posY;
            turtlePlayer.prevPosZ = turtlePlayer.posZ;
            turtlePlayer.prevRotationPitch = turtlePlayer.rotationPitch;
            turtlePlayer.prevRotationYaw = turtlePlayer.rotationYaw;
            System.out.println(lv() + "orientPlayer|5|"); //☆
        } finally { //☆
            level -= 1; //☆
        } //☆
    }

    public static class CCPlaceDropConsumer implements IEntityDropConsumer {

        private ITurtleAccess turtle;
        private World world;
        private ChunkCoordinates position;

        CCPlaceDropConsumer(ITurtleAccess turtle, World world, ChunkCoordinates position) {
            super();
            try { //☆
                level += 1; //☆
                
                System.out.println(lv() + "-----------------------------------");
                System.out.println(lv() + "-- CCPlaceDropConsumer()");
                System.out.println(lv() + "-----------------------------------");
                this.turtle = turtle;
                this.world = world;
                this.position = position;
            } finally { //☆
                level -= 1; //☆
            } //☆
        }

        @Override
        public void consumeDrop(Entity entity, ItemStack drop) {
            try { //☆
                level += 1; //☆
                
                System.out.println(lv() + "-----------------------------------");
                System.out.println(lv() + "-- consumeDrop()");
                System.out.println(lv() + "-----------------------------------");
                ItemStack remainder = InventoryUtil.storeItems(drop, this.turtle.getInventory(), 0,
                        this.turtle.getInventory().getSizeInventory(), this.turtle.getSelectedSlot());
                if (remainder != null) {
                    System.out.println(lv() + "consumeDrop|1|"); //☆
                    WorldUtil.dropItemStack(remainder, this.world, this.position.posX, this.position.posY,
                            this.position.posZ, Facing.oppositeSide[this.turtle.getDirection()]);
                } else {
                    System.out.println(lv() + "consumeDrop|2|"); //☆
                    
                }
                System.out.println(lv() + "consumeDrop|3|"); //☆
            } finally { //☆
                level -= 1; //☆
            } //☆
        }
    }

    private static ItemStack deployOnEntity(ItemStack stack, ITurtleAccess turtle, TurtlePlayer turtlePlayer,
            int direction, Object extraArguments[], String o_errorMessage[]) {
        try { //☆
            level += 1; //☆
            
            System.out.println(lv() + "-----------------------------------");
            System.out.println(lv() + "-- deployOnEntity()");
            System.out.println(lv() + "-----------------------------------");
            World world = turtle.getWorld();
            ChunkCoordinates position = turtle.getPosition();
            Vec3 turtlePos = Vec3.createVectorHelper(turtlePlayer.posX, turtlePlayer.posY, turtlePlayer.posZ);
            Vec3 rayDir = turtlePlayer.getLook(1.0F);
            Vec3 rayStart = turtlePos.addVector(rayDir.xCoord * 0.40000000000000002D, rayDir.yCoord * 0.40000000000000002D,
                    rayDir.zCoord * 0.40000000000000002D);
            Entity hitEntity = WorldUtil.rayTraceEntities(world, rayStart, rayDir, 1.1000000000000001D);
            if (hitEntity == null) {
                System.out.println(lv() + "deployOnEntity|1|"); //☆
                return stack;
            } else {
                System.out.println(lv() + "deployOnEntity|2|"); //☆
                
            }
            Item item = stack.getItem();
            ItemStack stackCopy = stack.copy();
            turtlePlayer.loadInventory(stackCopy);
            ComputerCraft.setEntityDropConsumer(hitEntity, new CCPlaceDropConsumer(turtle, world, position));
    
            boolean placed = false;
            if (hitEntity.interactFirst(turtlePlayer)) {
                System.out.println(lv() + "deployOnEntity|3|"); //☆
                placed = true;
            } else if (hitEntity instanceof EntityLivingBase) {
                System.out.println(lv() + "deployOnEntity|4|"); //☆
                placed = item.itemInteractionForEntity(stackCopy, turtlePlayer, (EntityLivingBase) hitEntity);
                if (placed) {
                    System.out.println(lv() + "deployOnEntity|5|"); //☆
                    turtlePlayer.loadInventory(stackCopy);
                } else {
                    System.out.println(lv() + "deployOnEntity|6|"); //☆
                    
                }
            }
            ComputerCraft.clearEntityDropConsumer(hitEntity);
            ItemStack remainder = turtlePlayer.unloadInventory(turtle);
            if (!placed && remainder != null && ItemStack.areItemStacksEqual(stack, remainder)) {
                System.out.println(lv() + "deployOnEntity|7|"); //☆
                return stack;
            } else {
                System.out.println(lv() + "deployOnEntity|8|"); //☆
                
            }
            if (remainder != null && remainder.stackSize > 0) {
                System.out.println(lv() + "deployOnEntity|9|"); //☆
                return remainder;
            } else {
                System.out.println(lv() + "deployOnEntity|10|"); //☆
                return null;
            }
        } finally { //☆
            level -= 1; //☆
        } //☆
    }

    private static boolean canDeployOnBlock(ItemStack stack, ITurtleAccess turtle, TurtlePlayer player,
            ChunkCoordinates position, int side, boolean allowReplaceable, String o_errorMessage[]) {
        try { //☆
            level += 1; //☆
            
            System.out.println(lv() + "-----------------------------------");
            System.out.println(lv() + "-- canDeployOnBlock()");
            System.out.println(lv() + "-----------------------------------");
            World world = turtle.getWorld();
            if (WorldUtil.isBlockInWorld(world, position) && !world.isAirBlock(position.posX, position.posY, position.posZ)
                    && (!(stack.getItem() instanceof ItemBlock) || !WorldUtil.isLiquidBlock(world, position))) {
                System.out.println(lv() + "canDeployOnBlock|1|"); //☆
                Block block = world.getBlock(position.posX, position.posY, position.posZ);
                int metadata = world.getBlockMetadata(position.posX, position.posY, position.posZ);
                boolean replaceable = block.isReplaceable(world, position.posX, position.posY, position.posZ);
                if (allowReplaceable || !replaceable) {
                    System.out.println(lv() + "canDeployOnBlock|2|"); //☆
                    if (ComputerCraft.turtlesObeyBlockProtection) {
                        System.out.println(lv() + "canDeployOnBlock|3|"); //☆
                        boolean editable = true;
                        if (replaceable) {
                            System.out.println(lv() + "canDeployOnBlock|4|"); //☆
                            editable = ComputerCraft.isBlockEditable(world, position.posX, position.posY, position.posZ,
                                    player);
                        } else {
                            System.out.println(lv() + "canDeployOnBlock|5|"); //☆
                            ChunkCoordinates shiftedPos = WorldUtil.moveCoords(position, side);
                            if (WorldUtil.isBlockInWorld(world, shiftedPos)) {
                                System.out.println(lv() + "canDeployOnBlock|6|"); //☆
                                editable = ComputerCraft.isBlockEditable(world, shiftedPos.posX, shiftedPos.posY,
                                        shiftedPos.posZ, player);
                            } else {
                                System.out.println(lv() + "canDeployOnBlock|7|"); //☆
                                
                            }
                        }
                        if (!editable) {
                            System.out.println(lv() + "canDeployOnBlock|8|"); //☆
                            if (o_errorMessage != null) {
                                System.out.println(lv() + "canDeployOnBlock|9|"); //☆
                                o_errorMessage[0] = "Cannot place in protected area";
                            } else {
                                System.out.println(lv() + "canDeployOnBlock|10|"); //☆
                                
                            }
                            return false;
                        } else {
                            System.out.println(lv() + "canDeployOnBlock|11|"); //☆
                            
                        }
                    } else {
                        System.out.println(lv() + "canDeployOnBlock|12|"); //☆
                        
                    }
                    if (block.canCollideCheck(metadata, true)) {
                        System.out.println(lv() + "canDeployOnBlock|13|"); //☆
                        return true;
                    } else {
                        System.out.println(lv() + "canDeployOnBlock|14|"); //☆
                        
                    }
                } else {
                    System.out.println(lv() + "canDeployOnBlock|15|"); //☆
                    
                }
            } else {
                System.out.println(lv() + "canDeployOnBlock|16|"); //☆
                
            }
            System.out.println(lv() + "canDeployOnBlock|17|"); //☆
            return false;
        } finally { //☆
            level -= 1; //☆
        } //☆
    }

    private static ItemStack deployOnBlock(ItemStack stack, ITurtleAccess turtle, TurtlePlayer turtlePlayer,
            ChunkCoordinates position, int side, Object extraArguments[], boolean allowReplace,
            String o_errorMessage[]) {
        try { //☆
            level += 1; //☆
            
            System.out.println(lv() + "-----------------------------------");
            System.out.println(lv() + "-- deployOnBlock()");
            System.out.println(lv() + "-----------------------------------");
            if (!canDeployOnBlock(stack, turtle, turtlePlayer, position, side, allowReplace, o_errorMessage)) {
                System.out.println(lv() + "deployOnBlock|1|"); //☆
                return stack;
            } else {
                System.out.println(lv() + "deployOnBlock|2|"); //☆
                
            }
            int playerDir = Facing.oppositeSide[side];
            ChunkCoordinates playerPosition = WorldUtil.moveCoords(position, side);
            orientPlayer(turtle, turtlePlayer, playerPosition, playerDir);
            float hitX = 0.5F + Facing.offsetsXForSide[side] * 0.5F;
            float hitY = 0.5F + Facing.offsetsYForSide[side] * 0.5F;
            float hitZ = 0.5F + Facing.offsetsZForSide[side] * 0.5F;
            if (Math.abs(hitY - 0.5F) < 0.01F) {
                System.out.println(lv() + "deployOnBlock|3|"); //☆
                hitY = 0.45F;
            } else {
                System.out.println(lv() + "deployOnBlock|4|"); //☆
                
            }
            Item item = stack.getItem();
            ItemStack stackCopy = stack.copy();
            turtlePlayer.loadInventory(stackCopy);
            boolean placed = false;
            if (item.onItemUseFirst(stackCopy, turtlePlayer, turtle.getWorld(), position.posX, position.posY, position.posZ,
                    side, hitX, hitY, hitZ)
                    || item.onItemUse(stackCopy, turtlePlayer, turtle.getWorld(), position.posX, position.posY,
                            position.posZ, side, hitX, hitY, hitZ)) {
                System.out.println(lv() + "deployOnBlock|5|"); //☆
                placed = true;
                turtlePlayer.loadInventory(stackCopy);
            } else if ((item instanceof ItemBucket) || (item instanceof ItemBoat) || (item instanceof ItemLilyPad)
                    || (item instanceof ItemGlassBottle)) {
                System.out.println(lv() + "deployOnBlock|6|"); //☆
                ItemStack result = item.onItemRightClick(stackCopy, turtle.getWorld(), turtlePlayer);
                if (!ItemStack.areItemStacksEqual(stack, result)) {
                    System.out.println(lv() + "deployOnBlock|7|"); //☆
                    placed = true;
                    turtlePlayer.loadInventory(result);
                } else {
                    System.out.println(lv() + "deployOnBlock|8|"); //☆
                    
                }
            }
            if (placed && (item instanceof ItemSign) && extraArguments != null && extraArguments.length >= 1
                    && (extraArguments[0] instanceof String)) {
                System.out.println(lv() + "deployOnBlock|9|"); //☆
                World world = turtle.getWorld();
                TileEntity tile = world.getTileEntity(position.posX, position.posY, position.posZ);
                if (tile == null) {
                    System.out.println(lv() + "deployOnBlock|10|"); //☆
                    ChunkCoordinates newPosition = WorldUtil.moveCoords(position, side);
                    tile = world.getTileEntity(newPosition.posX, newPosition.posY, newPosition.posZ);
                } else {
                    System.out.println(lv() + "deployOnBlock|11|"); //☆
                    
                }
                if (tile != null && (tile instanceof TileEntitySign)) {
                    System.out.println(lv() + "deployOnBlock|12|"); //☆
                    TileEntitySign signTile = (TileEntitySign) tile;
                    String s = (String) extraArguments[0];
                    String split[] = s.split("\n");
                    String signText[] = { "", "", "", "" };
                    int firstLine = split.length > 2 ? 0 : 1;
                    for (int i = 0; i < Math.min(split.length, signText.length); i++) {
                        if (split[i].length() > 15) {
                            System.out.println(lv() + "deployOnBlock|13|"); //☆
                            signText[firstLine + i] = split[i].substring(0, 15);
                        } else {
                            System.out.println(lv() + "deployOnBlock|14|"); //☆
                            signText[firstLine + i] = split[i];
                        }
                    }
    
                    signTile.signText = signText;
                    signTile.markDirty();
                    world.markBlockForUpdate(signTile.xCoord, signTile.yCoord, signTile.zCoord);
                } else {
                    System.out.println(lv() + "deployOnBlock|15|"); //☆
                    
                }
            } else {
                System.out.println(lv() + "deployOnBlock|16|"); //☆
                
            }
            ItemStack remainder = turtlePlayer.unloadInventory(turtle);
            if (!placed && remainder != null && ItemStack.areItemStacksEqual(stack, remainder)) {
                System.out.println(lv() + "deployOnBlock|17|"); //☆
                return stack;
            } else {
                System.out.println(lv() + "deployOnBlock|18|"); //☆
                
            }
            if (remainder != null && remainder.stackSize > 0) {
                System.out.println(lv() + "deployOnBlock|19|"); //☆
                return remainder;
            } else {
                System.out.println(lv() + "deployOnBlock|20|"); //☆
                return null;
            }
        } finally { //☆
            level -= 1; //☆
        } //☆
    }

    private final InteractDirection m_direction;
    private final Object m_extraArguments[];
}
