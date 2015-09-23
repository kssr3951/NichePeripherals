package kssr3951.nicheperipherals.application.alldirectionalcomparator;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import static kssr3951.nicheperipherals.application.alldirectionalcomparator.PeripheralAllDirectionalComparatorCommand1_ExCompare.CommandVariation.COMPARE_EX;
import static kssr3951.nicheperipherals.application.alldirectionalcomparator.PeripheralAllDirectionalComparatorCommand1_ExCompare.CommandVariation.COMPARE_NEGATIVE;
import static kssr3951.nicheperipherals.application.alldirectionalcomparator.PeripheralAllDirectionalComparatorCommand1_ExCompare.CommandVariation.COMPARE_POSITIVE;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class PeripheralAllDirectionalComparatorHosted implements IPeripheral {

	private ITurtleAccess turtle;
//	private TurtleSide side;

	public PeripheralAllDirectionalComparatorHosted(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
//		this.side = side;
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
		return false;
	}

	
	@Override
	public String[] getMethodNames() {
		return new String[] { "getName", "compareEx", "compareNegative", "comparePositive" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {

		switch(method) {
		case 0:
			return new Object[] { "allDirectionalScanner" };
		case 1:
			// compareEx
			return this.turtle.executeCommand(context,
					new PeripheralAllDirectionalComparatorCommand1_ExCompare(
							COMPARE_EX,
							arguments));
		case 2:
			// compareNegative
			return this.turtle.executeCommand(context,
					new PeripheralAllDirectionalComparatorCommand1_ExCompare(
							COMPARE_NEGATIVE,
							arguments));
		case 3:
			// comparePositive
			return this.turtle.executeCommand(context,
					new PeripheralAllDirectionalComparatorCommand1_ExCompare(
							COMPARE_POSITIVE,
							arguments));
		default:
			return new Object[] {false, "unknown command" };
		}
	}
}
