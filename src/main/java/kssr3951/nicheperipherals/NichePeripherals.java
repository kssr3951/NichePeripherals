package kssr3951.nicheperipherals;

import static kssr3951.nicheperipherals.application.Const.TEXTURE_PREFIX;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import kssr3951.nicheperipherals.application.alldirectionalcomparator.BlockAllDirectionalComparator;
import kssr3951.nicheperipherals.application.alldirectionalcomparator.PeripheralAllDirectionalComparator;
import kssr3951.nicheperipherals.application.alldirectionalcomparator.PeripheralAllDirectionalComparatorHosted;
import kssr3951.nicheperipherals.application.alldirectionalcomparator.RenderBlockAllDirectionalComparator;
import kssr3951.nicheperipherals.application.extensioncube.BlockExtensionCube;
import kssr3951.nicheperipherals.application.extensioncube.RenderBlockExtensionCube;
import kssr3951.nicheperipherals.application.metaplacer.BlockMetaPlacer;
import kssr3951.nicheperipherals.application.metaplacer.PeripheralMetaPlacer;
import kssr3951.nicheperipherals.application.metaplacer.PeripheralMetaPlacerHosted;
import kssr3951.nicheperipherals.application.metaplacer.RenderBlockMetaPlacer;
import kssr3951.nicheperipherals.application.metascanner.BlockMetaScanner;
import kssr3951.nicheperipherals.application.metascanner.PeripheralMetaScanner;
import kssr3951.nicheperipherals.application.metascanner.PeripheralMetaScannerHosted;
import kssr3951.nicheperipherals.application.metascanner.RenderBlockMetaScanner;
import kssr3951.nicheperipherals.application.modemcontroller.BlockModemController;
import kssr3951.nicheperipherals.application.modemcontroller.PeripheralModemController;
import kssr3951.nicheperipherals.application.modemcontroller.PeripheralModemControllerHosted;
import kssr3951.nicheperipherals.application.modemcontroller.RenderBlockModemController;
import kssr3951.nicheperipherals.system.blocks.BlockEx;
import kssr3951.nicheperipherals.system.peripheral.PeripheralEx;
import kssr3951.nicheperipherals.system.proxy.ProxyClient;
import kssr3951.nicheperipherals.system.proxy.ProxyCommon;
import kssr3951.nicheperipherals.system.render.RenderEx;
import kssr3951.nicheperipherals.system.tab.NichePeripheralsTab;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCY)
public class NichePeripherals {
	@Instance(value = "NichePeripherals")
	public static NichePeripherals instance;
	@SidedProxy(clientSide = ModInfo.PROXY_LOCATION + "ProxyClient", serverSide = ModInfo.PROXY_LOCATION + "ProxyServer")
	public static ProxyCommon proxy;

	private static final String modName_CC = "ComputerCraft";
	public static boolean dependency_CC = false;
	public static Item cc_cable;
	private static Item cc_pocketComputer;
	public static Block cc_turtle;

	private static final String extendedBlocksName = "NichePeripheralsExtendedBlocks";
	private static NichePeripheralsTab creativeTab;
	private static int PID_ALL_DIRECTIONAL_COMPARATOR;
	private static int PID_META_PLACER;
	private static int PID_META_SCANNER;
	private static int PID_MODEM_CONTROLLER;
	
	/** エクステンションキューブ（Extension cube） */
	private static BlockExtensionCube blockExtensionCube = null;
	/** 全方位コンパレータ（All directional comparator） */
	private static BlockAllDirectionalComparator blockAllDirectionalComparator = null;
	/** メタプレーサ（Meta placer） */
	private static BlockMetaPlacer blockMetaPlacer = null;
	/** メタスキャナ（Meta scanner） */
	private static BlockMetaScanner blockMetaScanner = null;
	/** モデムコントローラ（Modem controller） */
	private static BlockModemController blockModemController = null;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// ================================================================
		// Tab
		// ================================================================
		creativeTab = new NichePeripheralsTab("NichePeripherals");

		// ================================================================
		// configuration
		// ================================================================
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();
		// ---------------------------------------
		// 周辺機器（peripheral）のID
		// ---------------------------------------
		PID_ALL_DIRECTIONAL_COMPARATOR = cfg.get("upgrade", "allDirectionalComparator", 1700).getInt();
		PID_META_PLACER                = cfg.get("upgrade", "metaPlacer",               1701).getInt();
		PID_META_SCANNER               = cfg.get("upgrade", "metaScanner",              1702).getInt();
		PID_MODEM_CONTROLLER           = cfg.get("upgrade", "modemController",          1703).getInt();
		cfg.save();
		
		// ================================================================
		// dependencies
		// ================================================================
		if (Loader.isModLoaded(modName_CC)) {
			dependency_CC = true;
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// ================================================================
		// dependencies
		// ================================================================
		if(dependency_CC) {
			cc_cable = GameRegistry.findItem(modName_CC, "CC-Cable");
			cc_turtle = GameRegistry.findBlock(modName_CC, "CC-Turtle");
			cc_pocketComputer = GameRegistry.findItem(modName_CC, "pocketComputer");
		}

		// ================================================================
		// Block
		// ================================================================
		{
			final String sotogawaTexutureName = TEXTURE_PREFIX + NichePeripherals.extendedBlocksName;
			final String blockPrefix = ModInfo.ID.toLowerCase() + "_";
			
			// エクステンションキューブ（Extension cube）
			blockExtensionCube = (BlockExtensionCube)BlockEx.newInstance(BlockExtensionCube.class, blockPrefix + "extensionCube", creativeTab, sotogawaTexutureName, null, null);
			// 全方位コンパレータ（All directional comparator）
			blockAllDirectionalComparator = (BlockAllDirectionalComparator)BlockEx.newInstance(BlockAllDirectionalComparator.class, blockPrefix + "allDirectionalComparator", creativeTab, sotogawaTexutureName, Blocks.crafting_table, blockExtensionCube);
			// メタプレーサ（Meta placer）
			blockMetaPlacer = (BlockMetaPlacer)BlockEx.newInstance(BlockMetaPlacer.class, blockPrefix + "metaPlacer", creativeTab, sotogawaTexutureName, Blocks.anvil, blockExtensionCube);
			// メタスキャナ（Meta scanner）
			blockMetaScanner = (BlockMetaScanner)BlockEx.newInstance(BlockMetaScanner.class, blockPrefix + "metaScanner", creativeTab, sotogawaTexutureName, Blocks.daylight_detector, blockExtensionCube);
			// モデムコントローラ（Modem controller）
			blockModemController = (BlockModemController)BlockEx.newInstance(BlockModemController.class, blockPrefix + "modemController", creativeTab, sotogawaTexutureName, Blocks.brick_block, blockExtensionCube);
		}
		
		// ================================================================
		// Renderer
		// ================================================================
		if (proxy instanceof ProxyClient) {
			int renderID;
			// ---------------------------------------
			// エクステンションキューブ（Extension cube）
			// ---------------------------------------
			renderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(
					renderID,
					RenderEx.newInstance(RenderBlockExtensionCube.class, renderID, blockExtensionCube));
			blockExtensionCube.setRenderId(renderID);
			// ---------------------------------------
			// 全方位コンパレータ（All directional comparator）
			// ---------------------------------------
			renderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(
					renderID,
					RenderEx.newInstance(RenderBlockAllDirectionalComparator.class, renderID, blockAllDirectionalComparator));
			blockAllDirectionalComparator.setRenderId(renderID);
			// ---------------------------------------
			// メタプレーサ（Meta placer）
			// ---------------------------------------
			renderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(
					renderID,
					RenderEx.newInstance(RenderBlockMetaPlacer.class, renderID, blockMetaPlacer));
			blockMetaPlacer.setRenderId(renderID);
			// ---------------------------------------
			// メタスキャナ（Meta scanner）
			// ---------------------------------------
			renderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(
					renderID,
					RenderEx.newInstance(RenderBlockMetaScanner.class, renderID, blockMetaScanner));
			blockMetaScanner.setRenderId(renderID);
			// ---------------------------------------
			// モデムコントローラ（Modem controller）
			// ---------------------------------------
			renderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(
					renderID,
					RenderEx.newInstance(RenderBlockModemController.class, renderID, blockModemController));
			blockModemController.setRenderId(renderID);
		} else {
			blockExtensionCube.setRenderId(-1);
			blockAllDirectionalComparator.setRenderId(-1);
			blockMetaPlacer.setRenderId(-1);
			blockMetaScanner.setRenderId(-1);
			blockModemController.setRenderId(-1);
		}

		// ================================================================
		// Blocks
		// ================================================================
		GameRegistry.registerBlock(blockExtensionCube,            blockExtensionCube.getBlockName());
		GameRegistry.registerBlock(blockAllDirectionalComparator, blockAllDirectionalComparator.getBlockName());
		GameRegistry.registerBlock(blockMetaPlacer,               blockMetaPlacer.getBlockName());
		GameRegistry.registerBlock(blockMetaScanner,              blockMetaScanner.getBlockName());
		GameRegistry.registerBlock(blockModemController,          blockModemController.getBlockName());
		
		// ================================================================
		// Creative Tab
		// ================================================================
		creativeTab.setTabIconItem(Item.getItemFromBlock(blockMetaScanner));

		// ---------------------------------------
		// エクステンションキューブ（extension cube）
		// ---------------------------------------
		// ポケットコンピュータをガラス板で囲む
		GameRegistry.addRecipe(
				new ItemStack(blockExtensionCube),
				"xxx",
				"xyx",
				"xxx",
				'x', Blocks.glass_pane,
				'y', new ItemStack(cc_pocketComputer));
		// ブロック破壊時の戻りアイテムを設定（全て戻るようにする）
		blockExtensionCube.setIngredients(new ItemStack[]{
				new ItemStack(Blocks.glass_pane, 8),
				new ItemStack(cc_pocketComputer) });

		// ---------------------------------------
		// 全方位コンパレータ（All directional comparator）
		// ---------------------------------------
		// 製作レシピ（キューブ＋コンパレータｘ６）
		GameRegistry.addRecipe(
				new ItemStack(blockAllDirectionalComparator),
				"a  ",
				"bbb",
				"bbb",
				'a', blockExtensionCube,
				'b', Items.comparator);
		// ブロック破壊時の戻りアイテムを設定（全て戻るようにする）
		blockAllDirectionalComparator.setIngredients(new ItemStack[]{
				new ItemStack(blockExtensionCube),
				new ItemStack(Items.comparator, 6) });

		// ---------------------------------------
		// メタプレーサ（Meta placer）
		// ---------------------------------------
		// 製作レシピ（キューブ＋コンパス＋コンパレータｘ３＋ピストンｘ２、ダイヤのクワ２個）
		GameRegistry.addRecipe(
				new ItemStack(blockMetaPlacer),
				"abc",
				"ddc",
				"eec",
				'a', blockExtensionCube,
				'b', Items.compass,
				'c', Items.comparator,
				'd', Blocks.piston,
				'e', Items.diamond_hoe);
		// ブロック破壊時の戻りアイテムを設定（全て戻るようにする）
		blockMetaPlacer.setIngredients(new ItemStack[]{
				new ItemStack(blockExtensionCube),
				new ItemStack(Items.compass),
				new ItemStack(Items.comparator, 3),
				new ItemStack(Blocks.piston, 2),
				new ItemStack(Items.diamond_hoe, 2) });

		// ---------------------------------------
		// メタスキャナ（Meta scanner）
		// ---------------------------------------
		// 製作レシピ（キューブ＋コンパス＋コンパレータｘ３＋レッドストーンブロックｘ４）
		GameRegistry.addRecipe(
				new ItemStack(blockMetaScanner),
				"abc",
				"  c",
				"  c",
				'a', blockExtensionCube,
				'b', Items.compass,
				'c', Items.comparator);
		// ブロック破壊時の戻りアイテムを設定（全て戻るようにする）
		blockMetaScanner.setIngredients(new ItemStack[]{
				new ItemStack(blockExtensionCube),
				new ItemStack(Items.compass),
				new ItemStack(Items.comparator, 3) });
		
		// ---------------------------------------
		// モデムコントローラ（modem controller）
		// ---------------------------------------
		// 製作レシピ（キューブ＋有線モデム＋ケーブルｘ２＋鉄インゴット＋金ナゲット）
		GameRegistry.addRecipe(
				new ItemStack(blockModemController),
				"a  ",
				"b e",
				"ccd",
				'a', blockExtensionCube,
				'b', new ItemStack(cc_cable, 1, 1), // <- modem
				'c', new ItemStack(cc_cable, 1, 0), // <- cable
				'd', Items.iron_ingot,
				'e', Items.gold_nugget);
		// ブロック破壊時の戻りアイテムを設定（全て戻るようにする）
		blockModemController.setIngredients(new ItemStack[]{
				new ItemStack(blockExtensionCube),
				new ItemStack(cc_cable, 1, 1), // <- modem
				new ItemStack(cc_cable, 2, 0), // <- cable
				new ItemStack(Items.iron_ingot),
				new ItemStack(Items.gold_nugget) });

		// ================================================================
		// Turtles
		// ================================================================
		ComputerCraftAPI.registerTurtleUpgrade(PeripheralEx.newInstance(PeripheralAllDirectionalComparator.class, PID_ALL_DIRECTIONAL_COMPARATOR, blockAllDirectionalComparator, PeripheralAllDirectionalComparatorHosted.class));
		ComputerCraftAPI.registerTurtleUpgrade(PeripheralEx.newInstance(PeripheralMetaPlacer.class, PID_META_PLACER, blockMetaPlacer, PeripheralMetaPlacerHosted.class));
		ComputerCraftAPI.registerTurtleUpgrade(PeripheralEx.newInstance(PeripheralMetaScanner.class, PID_META_SCANNER, blockMetaScanner, PeripheralMetaScannerHosted.class));
		ComputerCraftAPI.registerTurtleUpgrade(PeripheralEx.newInstance(PeripheralModemController.class, PID_MODEM_CONTROLLER, blockModemController, PeripheralModemControllerHosted.class));
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}
