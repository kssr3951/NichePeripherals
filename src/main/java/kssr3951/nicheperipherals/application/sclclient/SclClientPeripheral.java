package kssr3951.nicheperipherals.application.sclclient;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;

/**
 * @author kssr3951
 *
 *         This mod is distributed under the terms of the Minecraft Mod Public
 *         License Japanese Transration (MMPL_J) Version 1.0.1. Please check the
 *         contents of the license located in
 *         http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 *
 *         この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J)
 *         Version 1.0.1 の条件のもとに配布されています。 ライセンスの内容は次のサイトを確認してください。
 *         http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class SclClientPeripheral implements IPeripheral {

    private ITurtleAccess turtle;
    private TileEntityCommandBlock m_commandBlock;

    public SclClientPeripheral(ITurtleAccess turtle, TurtleSide side) {
        this.turtle = turtle;
    }

    public void setCommandBlock(TileEntityCommandBlock commandBlock) {
        this.m_commandBlock = commandBlock;
    }

    @Override
    public String getType() {
        return "sclClient";
    }

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other != null && other.getClass() == this.getClass();
    }

    private static final String[] METHOD_NAMES = new String[] { "getName", "setScl", "removeScl" };

    @Override
    public String[] getMethodNames() {
        return METHOD_NAMES;
    }

    private class SclLuaTask implements ILuaTask {
        private String command;
        private TileEntityCommandBlock m_commandBlock;
        
        public SclLuaTask(String command, TileEntityCommandBlock commandBlock) {
            this.command = command;
            this.m_commandBlock = commandBlock;
        }
        @Override
        public Object[] execute() throws LuaException {
            // setCommand start
            SclClientPeripheral.this.m_commandBlock.func_145993_a().func_145752_a(command);
            SclClientPeripheral.this.turtle.getWorld().markBlockForUpdate(
                    SclClientPeripheral.this.m_commandBlock.xCoord,
                    SclClientPeripheral.this.m_commandBlock.yCoord,
                    SclClientPeripheral.this.m_commandBlock.zCoord);
            // setCommand end
            // runCommand start
            CommandBlockLogic logic = this.m_commandBlock.func_145993_a();
            logic.func_145755_a(SclClientPeripheral.this.turtle.getWorld());
            int result = this.m_commandBlock.func_145993_a().func_145760_g();
            if (result > 0) {
                return (new Object[] { Boolean.valueOf(true), "Command success" });
            } else {
                return (new Object[] { Boolean.valueOf(false), "Command failed" });
            }
            // runCommand end
        }
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] args)
            throws LuaException, InterruptedException {

        if (method < 0 || (METHOD_NAMES.length - 1) < method) {
            return new Object[] { false, "unknown command" };
        }
        if ("getName".equals(METHOD_NAMES[method])) {
            return new Object[] { "SCL Client" };
        }
        if ("setScl".equals(METHOD_NAMES[method])) {
            
            String command;
            String loaderName;
            int dimensionId = this.turtle.getWorld().provider.dimensionId;
            int chunkX = this.turtle.getPosition().posX / 16;
            int chunkZ = this.turtle.getPosition().posY / 16;
            int level = 1; // デフォルト値
            if (args.length == 1) {
                // <loaderName>
                if ((args[0] instanceof String)) {
                    loaderName = (String) args[0];
                    command = String.format("/scl set %s %d %d %d %d", loaderName, dimensionId, chunkX, chunkZ, level);
                    System.out.println("(arg count = 1) command = [" + command + "]");
                } else {
                    throw new LuaException("Expected string");
                }
            } else if (args.length == 3) {
                // <loaderName> <chunk_x> <chunk_z>
                if (   (args[0] instanceof String)
                    && (args[1] instanceof Double)
                    && (args[2] instanceof Double)) {
                    loaderName = (String) args[0];
                    chunkX = (Integer) args[1];
                    chunkZ = (Integer) args[2];
                    command = String.format("/scl set %s %d %d %d %d", loaderName, dimensionId, chunkX, chunkZ, level);
                    System.out.println("(arg count = 3) command = [" + command + "]");
                } else {
                    throw new LuaException("Expected string integer integer");
                }
            } else if (args.length == 4) {
                // <loaderName> <chunk_x> <chunk_z> <level>
                if (   (args[0] instanceof String)
                        && (args[1] instanceof Double)
                        && (args[2] instanceof Double)
                        && (args[3] instanceof Double)) {
                    loaderName = (String) args[0];
                    chunkX = (Integer) args[1];
                    chunkZ = (Integer) args[2];
                    level = (Integer) args[3];
                    command = String.format("/scl set %s %d %d %d %d", loaderName, dimensionId, chunkX, chunkZ, level);
                    System.out.println("(arg count = 4) command = [" + command + "]");
                } else {
                    throw new LuaException("Expected string integer integer");
                }
            } else {
                throw new LuaException("Bad arguments");
            }
            return context.executeMainThreadTask(new SclLuaTask(command, this.m_commandBlock));
        }
        if ("removeScl".equals(METHOD_NAMES[method])) {
            if (args.length < 1 || !(args[0] instanceof String)) {
                // <loaderName>
                throw new LuaException("Expected string");
            } else {
                final String command = "/scl remove " + (String) args[0];
                return context.executeMainThreadTask(new SclLuaTask(command, this.m_commandBlock));
            }
        }
        return new Object[] { false, "unknown command" };
    }
}
