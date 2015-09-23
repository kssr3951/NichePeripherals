package kssr3951.nicheperipherals.system.peripheral;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import kssr3951.nicheperipherals.system.blocks.BlockEx;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public abstract class PeripheralEx implements ITurtleUpgrade  {

	protected int pID;
	protected String unlocalisedAdjective;
	protected BlockEx blockEx;
	@SuppressWarnings("rawtypes")
	protected Class iPeripheralClass;
	
	@SuppressWarnings({ "unchecked", "null" })
	public static PeripheralEx newInstance(@SuppressWarnings("rawtypes") Class peripheralExClass, int pID, BlockEx blockEx, @SuppressWarnings("rawtypes") Class iPeripheralClass) {
		PeripheralEx pEx = null;
		try {
			@SuppressWarnings("rawtypes")
			Constructor ct = peripheralExClass.getConstructor();
			pEx = (PeripheralEx) ct.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.out.println(pEx.toString());
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.out.println(pEx.toString());
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.out.println(pEx.toString());
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			System.out.println(pEx.toString());
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println(pEx.toString());
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			System.out.println(pEx.toString());
			return null;
		}
		pEx.pID = pID;
		pEx.unlocalisedAdjective = "tile." + blockEx.getBlockName() + ".name";
		pEx.blockEx = blockEx;
		pEx.iPeripheralClass = iPeripheralClass;
		return pEx;
	}

	@Override
	public final int getUpgradeID() {
		return this.pID;
	}

	@Override
	public final String getUnlocalisedAdjective() {
		return this.unlocalisedAdjective;
	}

	@Override
	public final ItemStack getCraftingItem() {
		return new ItemStack(this.blockEx);
	}

	@SuppressWarnings("null")
	@Override
	public final IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		IPeripheral peripheral = null;
		try {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor ct = this.iPeripheralClass.getConstructor(ITurtleAccess.class, TurtleSide.class);
			peripheral = (IPeripheral) ct.newInstance(turtle, side);
			return peripheral;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.out.println(peripheral.toString());
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			System.out.println(peripheral.toString());
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.out.println(peripheral.toString());
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.out.println(peripheral.toString());
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println(peripheral.toString());
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			System.out.println(peripheral.toString());
			return null;
		}
	}

	@Override
	public final IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return this.blockEx.getPeripheralIcon();
	}
}