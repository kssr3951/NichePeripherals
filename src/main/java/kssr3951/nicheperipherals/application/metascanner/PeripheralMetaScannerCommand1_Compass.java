package kssr3951.nicheperipherals.application.metascanner;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class PeripheralMetaScannerCommand1_Compass implements ITurtleCommand {

	private int compassCode;
	private int dir;

	public PeripheralMetaScannerCommand1_Compass(int dir) {
		this.dir = dir;
	}

	public int getCompassCode() {
		return this.compassCode;
	}
	
	@Override
	public TurtleCommandResult execute(ITurtleAccess givenTurtle) {
		
		switch(this.dir) {
		case 2:
			// 北(North)
			this.compassCode = 0;
			break;
		case 5:
			// 東(East)
			this.compassCode = 1;
			break;
		case 3:
			// 南(South)
			this.compassCode = 2;
			break;
		case 4:
			// 西(West)
			this.compassCode = 3;
			break;
		default:
			return TurtleCommandResult.failure("Something happened");
		}
		
		// 処理成功
		return TurtleCommandResult.success();
	}
}
