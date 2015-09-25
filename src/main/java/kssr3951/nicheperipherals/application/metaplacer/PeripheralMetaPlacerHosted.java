package kssr3951.nicheperipherals.application.metaplacer;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.InteractDirection;
import dan200.computercraft.shared.turtle.core.TurtlePlaceCommand;
import kssr3951.nicheperipherals.application.metascanner.PeripheralMetaScannerCommand1_Compass;
import kssr3951.nicheperipherals.application.metascanner.PeripheralMetaScannerCommand2_Scan;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class PeripheralMetaPlacerHosted implements IPeripheral {

    private static final int DIR_UP = 1;
    private static final int DIR_DOWN = 0;
    private ITurtleAccess turtle;
    private TurtleSide side;

    public PeripheralMetaPlacerHosted(ITurtleAccess turtle, TurtleSide side) {

        this.turtle = turtle;
        this.side = side;

    }

    @Override
    public String getType() {

        return "metaPlacer";

    }

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }

    @Override
    public boolean equals(IPeripheral other) {
        return false;
    }

    private static final String[] METHOD_NAMES = new String[] { "getName", "compass", "scan", "scanUp", "scanDown", "place", "placeUp","placeDown" };
    @Override
    public String[] getMethodNames() {
        return METHOD_NAMES;
    }
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
            throws LuaException, InterruptedException {

        if (method < 0 || (METHOD_NAMES.length - 1)  < method) {
            return new Object[] {false, "unknown command" };
        }
        if ("getName".equals(METHOD_NAMES[method])) {
            return new Object[] { "Meta Placer" };
        }
        if ("compass".equals(METHOD_NAMES[method])) {
            return this.compassCommand(context, this.turtle.getDirection());
        }
        if ("scan".equals(METHOD_NAMES[method])) {
            return this.scanCommand(context, this.turtle.getDirection());
        }
        if ("scanUp".equals(METHOD_NAMES[method])) {
            return this.scanCommand(context, DIR_UP);
        }
        if ("scanDown".equals(METHOD_NAMES[method])) {
            return this.scanCommand(context, DIR_DOWN);
        }
        if ("place".equals(METHOD_NAMES[method])) {
            return this.exPlaceCommand(context, this.turtle.getDirection(), arguments);
        }
        if ("placeUp".equals(METHOD_NAMES[method])) {
            return this.exPlaceCommand(context, DIR_UP, arguments);
        }
        if ("placeDown".equals(METHOD_NAMES[method])) {
            return this.exPlaceCommand(context, DIR_DOWN, arguments);
        }
        return new Object[] {false, "unknown command" };
    }

    private Object[] compassCommand(ILuaContext context, int dir)
            throws LuaException, InterruptedException {
        PeripheralMetaScannerCommand1_Compass cmd = new PeripheralMetaScannerCommand1_Compass(dir);
        Object[] rslt = this.turtle.executeCommand(context, cmd);
        Object[] ret = new Object[rslt.length + 1];
        for (int i = 0; i < rslt.length; i++) {
            ret[i] = rslt[i];
        }
        ret[rslt.length + 0] = cmd.getCompassCode();
        return ret;
    }

    private Object[] scanCommand(ILuaContext context, int dir)
            throws LuaException, InterruptedException {
        PeripheralMetaScannerCommand2_Scan cmd = new PeripheralMetaScannerCommand2_Scan(this.side, dir);
        Object[] rslt = this.turtle.executeCommand(context, cmd);
        Object[] ret = new Object[rslt.length + 1];
        for (int i = 0; i < rslt.length; i++) {
            ret[i] = rslt[i];
        }
        ret[rslt.length + 0] = cmd.getScanCode();
        return ret;
    }

    private Object[] exPlaceCommand(ILuaContext context, int dir, Object[] arguments)
            throws LuaException, InterruptedException {

        if (0 == arguments.length) {
            if (0 == dir) {
                return this.turtle.executeCommand(context, new TurtlePlaceCommand(InteractDirection.Up, arguments));
            } else if (1 == dir) {
                return this.turtle.executeCommand(context, new TurtlePlaceCommand(InteractDirection.Down, arguments));
            } else {
                return this.turtle.executeCommand(context, new TurtlePlaceCommand(InteractDirection.Forward, arguments));
            }
        } else {
            Object[] rslt = this.turtle.executeCommand(context, new PeripheralMetaPlacerCommand1_ExPlace(dir, arguments));
            return rslt;
        }
    }
}
