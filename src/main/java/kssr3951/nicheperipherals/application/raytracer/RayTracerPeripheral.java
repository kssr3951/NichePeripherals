package kssr3951.nicheperipherals.application.raytracer;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import kssr3951.nicheperipherals.application.raytracer.RayTracerCommand1_RayTrace.CommandDirection;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class RayTracerPeripheral implements IPeripheral {

    private ITurtleAccess turtle;
    private TurtleSide side;
    
    /**
     * rayの発射位置。左側に付けた場合、1.0はタートルの左隣のブロックの中心。最小値は１.0とする。
     */
    private double baselineLength = 1.0;
    /**
     * 方位角。タートルの中心を軸として-45度から＋45度まで調整可能。それ以上の角度が必要な場合はタートル本体を回転させてください。
     */
    private double azimuth = 0;
    /**
     * 仰角。タートルの中心を軸として-45度から＋45度まで調整可能。それ以上の角度が必要な場合はrayTraceUp()、rayTraceDown()を使ってください。
     * @param turtle
     * @param side
     */
    private double elevation = 0;

    public RayTracerPeripheral(ITurtleAccess turtle, TurtleSide side) {
        this.turtle = turtle;
        this.side = side;
    }

    @Override
    public String getType() {
        return "rayTracer";
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

    private Object[] setBaselineLength(double baselineLength) {
        if (baselineLength < 1.0 || 10.0 < baselineLength) {
            return new Object[]{ false, "cannot set " + baselineLength + ". (min:1.0, max:10.0)" };
        } else {
            this.baselineLength = baselineLength;
            return new Object[]{ true };
        }
    }
    
    private Object[] setAzimuth(double azimuth) {
        if (azimuth < -45.0 || 45.0 < azimuth) {
            return new Object[]{ false, "cannot set " + azimuth + ". (min:-45.0, max:45.0)" };
        } else {
            this.azimuth = azimuth;
            return new Object[]{ true };
        }
    }

    private Object[] setElevation(double elevation) {
        if (elevation < -45.0 || 45.0 < elevation) {
            return new Object[]{ false, "cannot set " + elevation + ". (min:-45.0, max:45.0)" };
        } else {
            this.elevation = elevation;
            return new Object[]{ true };
        }
    }

    private String[] METHOD_NAMES_LEFT = {
            "getName", "rayTrace", "rayTraceUp", "rayTraceDown", "rayTraceLeft",
            "setBaselineLength", "setAzimuth", "setElevation",
            "getBaselineLength", "getAzimuth", "getElevation" };
    private String[] METHOD_NAMES_RIGHT = {
            "getName", "rayTrace", "rayTraceUp", "rayTraceDown", "rayTraceRight",
            "setBaselineLength", "setAzimuth", "setElevation",
            "getBaselineLength", "getAzimuth", "getElevation" };
    @Override
    public String[] getMethodNames() {
        if (TurtleSide.Left == this.side) {
            return METHOD_NAMES_LEFT;
        } else if (TurtleSide.Right == this.side) {
            return METHOD_NAMES_RIGHT;
        } else {
            return null;
        }
    }
    
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] args)
            throws LuaException, InterruptedException {

        String[] METHOD_NAMES = this.getMethodNames();
        if (method < 0 || (METHOD_NAMES.length - 1)  < method) {
            return new Object[] {false, "unknown command" };
        }
        if ("getName".equals(METHOD_NAMES[method])) {
            return new Object[] { "Lay Tracer" };
        }
        if ("rayTrace".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new RayTracerCommand1_RayTrace(
                    this.side, CommandDirection.FORWARD, baselineLength, azimuth, elevation, args));
        }
        if ("rayTraceUp".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new RayTracerCommand1_RayTrace(
                    this.side, CommandDirection.UP, baselineLength, azimuth, elevation, args));
        }
        if ("rayTraceDown".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new RayTracerCommand1_RayTrace(
                    this.side, CommandDirection.DOWN, baselineLength, azimuth, elevation, args));
        }
        if ("rayTraceLeft".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new RayTracerCommand1_RayTrace(
                    this.side, CommandDirection.LEFT, baselineLength, azimuth, elevation, args));
        }
        if ("rayTraceRight".equals(METHOD_NAMES[method])) {
            return this.turtle.executeCommand(context, new RayTracerCommand1_RayTrace(
                    this.side, CommandDirection.RIGHT, baselineLength, azimuth, elevation, args));
        }
        if ("setBaselineLength".equals(METHOD_NAMES[method])) {
            if (null == args || 1 != args.length || !(args[0] instanceof Double)) {
                return new Object[] { false, "Expected double" };
            }
            return setBaselineLength((Double)args[0]);
        }
        if ("setAzimuth".equals(METHOD_NAMES[method])) {
            if (null == args || 1 != args.length || !(args[0] instanceof Double)) {
                return new Object[] { false, "Expected double" };
            }
            return setAzimuth((Double)args[0]);
        }
        if ("setElevation".equals(METHOD_NAMES[method])) {
            if (null == args || 1 != args.length || !(args[0] instanceof Double)) {
                return new Object[] { false, "Expected double" };
            }
            return setElevation((Double)args[0]);
        }
        if ("getBaselineLength".equals(METHOD_NAMES[method])) {
            return new Object[]{ this.baselineLength };
        }
        if ("getAzimuth".equals(METHOD_NAMES[method])) {
            return new Object[] { this.azimuth };
        }
        if ("getElevation".equals(METHOD_NAMES[method])) {
            return new Object[]{ this.elevation };
        }
        return new Object[] {false, "unknown command" };
    }
}
