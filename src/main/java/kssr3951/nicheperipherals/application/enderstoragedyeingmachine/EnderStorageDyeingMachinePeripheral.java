package kssr3951.nicheperipherals.application.enderstoragedyeingmachine;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.InteractDirection;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class EnderStorageDyeingMachinePeripheral implements IPeripheral {

    private ITurtleAccess turtle;

    public EnderStorageDyeingMachinePeripheral(ITurtleAccess turtle, TurtleSide side) {
        this.turtle = turtle;
    }

    @Override
    public String getType() {
        return "enderStorageDyeingMachine";
    }

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other != null && other.getClass() == getClass();
    }

    private static final String[] METHOD_NAMES = 
            new String[] { 
                    "getName", "dyeBlock", "dyeBlockDown",
                    "getColor", "getColorDown", "getDyeName", "getDyeName", 
                    "getLiquidAmount", "getLiquidAmountUp", "getLiquidAmountDown" };

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
            return new Object[] { "Ender Storage Dyeing Machine" };
        }
        if ("dyeBlock".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand1_DyeBlock(InteractDirection.Forward, arguments));
        }
        if ("dyeBlockDown".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand1_DyeBlock(InteractDirection.Down, arguments));
        }
        if ("getColor".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand2_GetColor(InteractDirection.Forward, false));
        }
        if ("getColorDown".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand2_GetColor(InteractDirection.Down, false));
        }
        if ("getDyeName".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand2_GetColor(InteractDirection.Forward, true));
        }
        if ("getDyeNameDown".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand2_GetColor(InteractDirection.Down, true));
        }
        if ("getLiquidAmount".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand3_GetLiquidAmount(InteractDirection.Forward));
        }
        if ("getLiquidAmountUp".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand3_GetLiquidAmount(InteractDirection.Up));
        }
        if ("getLiquidAmountDown".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new EnderStorageDyeingMachineCommand3_GetLiquidAmount(InteractDirection.Down));
        }
        return new Object[] {false, "unknown command" };
    }
}
