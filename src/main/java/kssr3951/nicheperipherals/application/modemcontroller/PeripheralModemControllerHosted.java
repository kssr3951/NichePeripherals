package kssr3951.nicheperipherals.application.modemcontroller;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class PeripheralModemControllerHosted implements IPeripheral {

	private ITurtleAccess turtle;
	private TurtleSide side;
	
	private static final int DIR_UP = 1;
	private static final int DIR_DOWN = 0;

	public PeripheralModemControllerHosted(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		this.side = side;
	}

	@Override
	public String getType() {
		return "modemController";
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

	private static final String[] METHOD_NAMES = new String[] { "getName", "activate", "activateUp", "activateDown" };
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
			return new Object[] { "Modem Controller" };
		} 
		if ("activate".equals(METHOD_NAMES[method])) {
			return this.tryCommand(context, this.turtle.getDirection(), arguments);
		}
		if ("activateUp".equals(METHOD_NAMES[method])) {
			return this.tryCommand(context, DIR_UP, arguments);
		}
		if ("activateDown".equals(METHOD_NAMES[method])) {
			return this.tryCommand(context, DIR_DOWN, arguments);
		}
		return new Object[] {false, "unknown command" };
	}

	private Object[] tryCommand(ILuaContext context, int dir, Object[] arguments)
			throws LuaException, InterruptedException {
		
		if (1 != arguments.length) {
			return new Object[] {false, "Expected boolean" };
		}
		boolean activate;
		{
			Object o = arguments[0];
			if (o instanceof Boolean) {
				activate = (Boolean)o;
			} else {
				return new Object[] {false, "Expected boolean" };
			}
		}
		PeripheralModemControllerCommand1_Activate cmd = new PeripheralModemControllerCommand1_Activate(this.side, dir, activate);
		Object[] rslt = this.turtle.executeCommand(context, cmd);
		if ("".equals(cmd.getMessageWhenSuccess())) {
			return rslt;
		} else {
			// 成功時のおまけメッセージを追加してreturnする。
			Object[] ret = new Object[rslt.length + 1];
			for (int i = 0; i < rslt.length; i++) {
				ret[i] = rslt[i];
			}
			ret[ret.length - 1] = cmd.getMessageWhenSuccess();
			return ret;
		}
	}
}
