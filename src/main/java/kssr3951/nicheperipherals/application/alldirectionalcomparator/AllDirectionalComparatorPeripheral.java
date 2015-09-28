package kssr3951.nicheperipherals.application.alldirectionalcomparator;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import kssr3951.nicheperipherals.application.alldirectionalcomparator.AllDirectionalComparatorCommand1_ExCompare.CommandVariation;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class AllDirectionalComparatorPeripheral implements IPeripheral {

    private ITurtleAccess turtle;

    public AllDirectionalComparatorPeripheral(ITurtleAccess turtle, TurtleSide side) {
        this.turtle = turtle;
    }

    @Override
    public String getType() {
        return "allDirectionalScanner";
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

    private String[] METHOD_NAMES = { "getName", "compareEx", "compareNegative", "comparePositive" };
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
        if ("compareEx".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context,
                    new AllDirectionalComparatorCommand1_ExCompare(
                            CommandVariation.COMPARE_EX,
                            arguments));
        }
        if ("compareNegative".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context,
                    new AllDirectionalComparatorCommand1_ExCompare(
                            CommandVariation.COMPARE_NEGATIVE,
                            arguments));
        }
        if ("comparePositive".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context,
                    new AllDirectionalComparatorCommand1_ExCompare(
                            CommandVariation.COMPARE_POSITIVE,
                            arguments));
        }
        return new Object[] {false, "unknown command" };
    }
}
