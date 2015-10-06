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
import kssr3951.nicheperipherals.application.alldirectionalcomparator.AllDirectionalComparatorBlock;
import kssr3951.nicheperipherals.application.alldirectionalcomparator.AllDirectionalComparatorPeripheral;
import kssr3951.nicheperipherals.application.alldirectionalcomparator.AllDirectionalComparatorRender;
import kssr3951.nicheperipherals.application.alldirectionalcomparator.AllDirectionalComparatorUpgrade;
import kssr3951.nicheperipherals.application.extensioncube.ExtensionCubeBlock;
import kssr3951.nicheperipherals.application.extensioncube.ExtensionCubeRender;
import kssr3951.nicheperipherals.application.metaplacer.MetaPlacerBlock;
import kssr3951.nicheperipherals.application.metaplacer.MetaPlacerPeripheral;
import kssr3951.nicheperipherals.application.metaplacer.MetaPlacerRender;
import kssr3951.nicheperipherals.application.metaplacer.MetaPlacerUpgrade;
import kssr3951.nicheperipherals.application.metascanner.MetaScannerBlock;
import kssr3951.nicheperipherals.application.metascanner.MetaScannerPeripheral;
import kssr3951.nicheperipherals.application.metascanner.MetaScannerRender;
import kssr3951.nicheperipherals.application.metascanner.MetaScannerUpgrade;
import kssr3951.nicheperipherals.application.modemcontroller.ModemControllerBlock;
import kssr3951.nicheperipherals.application.modemcontroller.ModemControllerPeripheral;
import kssr3951.nicheperipherals.application.modemcontroller.ModemControllerRender;
import kssr3951.nicheperipherals.application.modemcontroller.ModemControllerUpgrade;
import kssr3951.nicheperipherals.application.raytracer.RayTracerBlock;
import kssr3951.nicheperipherals.application.raytracer.RayTracerPeripheral;
import kssr3951.nicheperipherals.application.raytracer.RayTracerRender;
import kssr3951.nicheperipherals.application.raytracer.RayTracerUpgrade;
import kssr3951.nicheperipherals.application.sclclient.SclClientBlock;
import kssr3951.nicheperipherals.application.sclclient.SclClientPeripheral;
import kssr3951.nicheperipherals.application.sclclient.SclClientRender;
import kssr3951.nicheperipherals.application.sclclient.SclClientUpgrade;
import kssr3951.nicheperipherals.system.blocks.BlockEx;
import kssr3951.nicheperipherals.system.peripheral.TurtleUpgradeEx;
import kssr3951.nicheperipherals.system.proxy.ProxyClient;
import kssr3951.nicheperipherals.system.proxy.ProxyCommon;
import kssr3951.nicheperipherals.system.render.RenderEx;
import kssr3951.nicheperipherals.system.tab.NichePeripheralsTab;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
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

    // ComputerCraftとの連携
    private static final String MODNAME_CC = "ComputerCraft";
    public static boolean dependency_CC = false;
    
    // BuildCraftとの連携（任意）
    private static final String MODNAME_BUILDCRAFT_CORE = "BuildCraft|Core";
    public static boolean dependency_BuildCraft_Core = false;
    
    public static class Dependency {
        public static Item cc_cable;
        public static Item cc_pocketComputer;
        public static Block cc_turtle;
        public static Block bc_blockConstructionMarker;
        public static Item bc_itemConstructionMarker;
    }
    
    private static final String extendedBlocksName = "NichePeripheralsExtendedBlocks";
    private static NichePeripheralsTab creativeTab;
    private static int PID_ALL_DIRECTIONAL_COMPARATOR;
    private static int PID_META_PLACER;
    private static int PID_META_SCANNER;
    private static int PID_MODEM_CONTROLLER;
    private static int PID_SCL_CLIENT;
    private static int PID_RAY_TRACER;
    
    /** 拡張キューブ（Extension cube） */
    private static ExtensionCubeBlock blockExtensionCube = null;
    /** 全方位コンパレータ（All directional comparator） */
    private static AllDirectionalComparatorBlock blockAllDirectionalComparator = null;
    /** メタプレーサ（Meta placer） */
    private static MetaPlacerBlock blockMetaPlacer = null;
    /** メタスキャナ（Meta scanner） */
    private static MetaScannerBlock blockMetaScanner = null;
    /** モデムコントローラ（Modem controller） */
    private static ModemControllerBlock blockModemController = null;
    /** SCLクライアント(SCL client) */
    private static SclClientBlock blockSclClient = null;
    /** レイトレーサ(Ray Tracer) */
    private static RayTracerBlock blockRayTracer = null;
    
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
        PID_SCL_CLIENT                 = cfg.get("upgrade", "sclClient",                1704).getInt();
        PID_RAY_TRACER                 = cfg.get("upgrade", "rayTracer",                1705).getInt();
        cfg.save();
        
        // ================================================================
        // dependencies
        // ================================================================
        if (Loader.isModLoaded(MODNAME_CC)) {
            dependency_CC = true;
        }
        if (Loader.isModLoaded(MODNAME_BUILDCRAFT_CORE)) {
            dependency_BuildCraft_Core = true;
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // ================================================================
        // dependencies
        // ================================================================
        if (dependency_CC) {
            Dependency.cc_cable = GameRegistry.findItem(MODNAME_CC, "CC-Cable");
            Dependency.cc_pocketComputer = GameRegistry.findItem(MODNAME_CC, "pocketComputer");
            Dependency.cc_turtle = GameRegistry.findBlock(MODNAME_CC, "CC-Turtle");
        }
        if (dependency_BuildCraft_Core) {
            Dependency.bc_blockConstructionMarker = GameRegistry.findBlock(MODNAME_BUILDCRAFT_CORE, "markerBlock");
            Dependency.bc_itemConstructionMarker = GameRegistry.findItem(MODNAME_BUILDCRAFT_CORE, "markerBlock");
        }

        // ================================================================
        // Block
        // ================================================================
        {
            final String sotogawaTexutureName = TEXTURE_PREFIX + NichePeripherals.extendedBlocksName;
            final String blockPrefix = ModInfo.ID.toLowerCase() + "_";
            
            // 拡張キューブ（Extension cube）
            blockExtensionCube = (ExtensionCubeBlock)BlockEx.newInstance(
                    ExtensionCubeBlock.class,
                    blockPrefix + "extensionCube",
                    creativeTab,
                    sotogawaTexutureName,
                    null);

            // 全方位コンパレータ（All directional comparator）
            blockAllDirectionalComparator = (AllDirectionalComparatorBlock)BlockEx.newInstance(
                    AllDirectionalComparatorBlock.class,
                    blockPrefix + "allDirectionalComparator",
                    creativeTab,
                    sotogawaTexutureName,
                    blockExtensionCube);

            // メタプレーサ（Meta placer）
            blockMetaPlacer = (MetaPlacerBlock)BlockEx.newInstance(
                    MetaPlacerBlock.class,
                    blockPrefix + "metaPlacer",
                    creativeTab,
                    sotogawaTexutureName,
                    blockExtensionCube);

            // メタスキャナ（Meta scanner）
            blockMetaScanner = (MetaScannerBlock)BlockEx.newInstance(
                    MetaScannerBlock.class,
                    blockPrefix + "metaScanner",
                    creativeTab,
                    sotogawaTexutureName,
                    blockExtensionCube);
            
            // モデムコントローラ（Modem controller）
            blockModemController = (ModemControllerBlock)BlockEx.newInstance(
                    ModemControllerBlock.class,
                    blockPrefix + "modemController",
                    creativeTab,
                    sotogawaTexutureName,
                    blockExtensionCube);
            
            // SCLクライアント(SCL client)
            blockSclClient = (SclClientBlock)BlockEx.newInstance(
                    SclClientBlock.class,
                    blockPrefix + "sclClient",
                    creativeTab,
                    sotogawaTexutureName,
                    blockExtensionCube);

            // レイトレーサ(Ray tracer)
            blockRayTracer = (RayTracerBlock)BlockEx.newInstance(
                    RayTracerBlock.class,
                    blockPrefix + "rayTracer",
                    creativeTab,
                    sotogawaTexutureName,
                    blockExtensionCube);
        }
        
        // ================================================================
        // Renderer
        // ================================================================
        if (proxy instanceof ProxyClient) {
            int renderID;
            // ---------------------------------------
            // 拡張キューブ（Extension cube）
            // ---------------------------------------
            renderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(
                    renderID,
                    RenderEx.newInstance(ExtensionCubeRender.class, renderID, blockExtensionCube));
            blockExtensionCube.setRenderId(renderID);

            // ---------------------------------------
            // 全方位コンパレータ（All directional comparator）
            // ---------------------------------------
            renderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(
                    renderID,
                    RenderEx.newInstance(AllDirectionalComparatorRender.class, renderID, blockAllDirectionalComparator));
            blockAllDirectionalComparator.setRenderId(renderID);

            // ---------------------------------------
            // メタプレーサ（Meta placer）
            // ---------------------------------------
            renderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(
                    renderID,
                    RenderEx.newInstance(MetaPlacerRender.class, renderID, blockMetaPlacer));
            blockMetaPlacer.setRenderId(renderID);

            // ---------------------------------------
            // メタスキャナ（Meta scanner）
            // ---------------------------------------
            renderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(
                    renderID,
                    RenderEx.newInstance(MetaScannerRender.class, renderID, blockMetaScanner));
            blockMetaScanner.setRenderId(renderID);

            // ---------------------------------------
            // モデムコントローラ（Modem controller）
            // ---------------------------------------
            renderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(
                    renderID,
                    RenderEx.newInstance(ModemControllerRender.class, renderID, blockModemController));
            blockModemController.setRenderId(renderID);

            // ---------------------------------------
            // SCLクライアント(SCL client)
            // ---------------------------------------
            renderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(
                    renderID,
                    RenderEx.newInstance(SclClientRender.class, renderID, blockSclClient));
            blockSclClient.setRenderId(renderID);
        
            // ---------------------------------------
            // レイトレーサ(Ray tracer)
            // ---------------------------------------
            renderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(
                    renderID,
                    RenderEx.newInstance(RayTracerRender.class, renderID, blockRayTracer));
            blockRayTracer.setRenderId(renderID);

        } else {
            blockExtensionCube.setRenderId(-1);
            blockAllDirectionalComparator.setRenderId(-1);
            blockMetaPlacer.setRenderId(-1);
            blockMetaScanner.setRenderId(-1);
            blockModemController.setRenderId(-1);
            blockSclClient.setRenderId(-1);
            blockRayTracer.setRenderId(-1);
        }

        // ================================================================
        // Blocks
        // ================================================================
        GameRegistry.registerBlock(blockExtensionCube,            blockExtensionCube.getBlockName());
        GameRegistry.registerBlock(blockAllDirectionalComparator, blockAllDirectionalComparator.getBlockName());
        GameRegistry.registerBlock(blockMetaPlacer,               blockMetaPlacer.getBlockName());
        GameRegistry.registerBlock(blockMetaScanner,              blockMetaScanner.getBlockName());
        GameRegistry.registerBlock(blockModemController,          blockModemController.getBlockName());
        GameRegistry.registerBlock(blockSclClient,                blockSclClient.getBlockName());
        GameRegistry.registerBlock(blockRayTracer,                blockRayTracer.getBlockName());
        
        // ================================================================
        // Creative Tab
        // ================================================================
        creativeTab.setTabIconItem(Item.getItemFromBlock(blockMetaScanner));

        // ---------------------------------------
        // 拡張キューブ（extension cube）
        // ---------------------------------------
        // ポケットコンピュータをガラス板で囲む
        GameRegistry.addRecipe(
                new ItemStack(blockExtensionCube),
                "xxx",
                "xyx",
                "xxx",
                'x', Blocks.glass_pane,
                'y', new ItemStack(Dependency.cc_pocketComputer));

        // ブロック破壊時の戻りアイテムを設定（ポケットコンピュータが戻ってくる。ガラス板は戻らない）
        blockExtensionCube.setIngredients(new ItemStack[]{
                new ItemStack(Dependency.cc_pocketComputer) });

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
                'b', new ItemStack(Dependency.cc_cable, 1, 1), // <- modem
                'c', new ItemStack(Dependency.cc_cable, 1, 0), // <- cable
                'd', Items.iron_ingot,
                'e', Items.gold_nugget);
        
        // ブロック破壊時の戻りアイテムを設定（全て戻るようにする）
        blockModemController.setIngredients(new ItemStack[]{
                new ItemStack(blockExtensionCube),
                new ItemStack(Dependency.cc_cable, 1, 1), // <- modem
                new ItemStack(Dependency.cc_cable, 2, 0), // <- cable
                new ItemStack(Items.iron_ingot),
                new ItemStack(Items.gold_nugget) });

        // ---------------------------------------
        // SCLクライアント(SCL client)
        // ---------------------------------------
        // 製作レシピ（キューブ＋（◆未定◆））
        GameRegistry.addRecipe(
                new ItemStack(blockModemController),
                "ab ",
                "   ",
                "   ",
                'a', blockExtensionCube,
                'b', Items.iron_ingot);
        
        // ブロック破壊時の戻りアイテムを設定（全て戻るようにする）
        blockSclClient.setIngredients(new ItemStack[]{
                new ItemStack(blockExtensionCube),
                new ItemStack(Items.iron_ingot) });
        
        // ---------------------------------------
        // レイトレーサ(Ray tracer)
        // ---------------------------------------
        // 製作レシピ（キューブ＋（◆未定◆））
        GameRegistry.addRecipe(
                new ItemStack(blockRayTracer),
                "ab ",
                "   ",
                "   ",
                'a', blockExtensionCube,
                'b', Items.diamond);
        
        // ブロック破壊時の戻りアイテムを設定（全て戻るようにする）
        blockRayTracer.setIngredients(new ItemStack[]{
                new ItemStack(blockExtensionCube),
                new ItemStack(Items.diamond) });
        
        // ================================================================
        // Turtles
        // ================================================================
        ComputerCraftAPI.registerTurtleUpgrade(TurtleUpgradeEx.newInstance(
                AllDirectionalComparatorUpgrade.class,
                PID_ALL_DIRECTIONAL_COMPARATOR,
                blockAllDirectionalComparator,
                AllDirectionalComparatorPeripheral.class));

        ComputerCraftAPI.registerTurtleUpgrade(TurtleUpgradeEx.newInstance(
                MetaPlacerUpgrade.class,
                PID_META_PLACER,
                blockMetaPlacer,
                MetaPlacerPeripheral.class));
        
        ComputerCraftAPI.registerTurtleUpgrade(TurtleUpgradeEx.newInstance(
                MetaScannerUpgrade.class,
                PID_META_SCANNER,
                blockMetaScanner,
                MetaScannerPeripheral.class));
        
        ComputerCraftAPI.registerTurtleUpgrade(TurtleUpgradeEx.newInstance(
                ModemControllerUpgrade.class,
                PID_MODEM_CONTROLLER,
                blockModemController,
                ModemControllerPeripheral.class));

        ComputerCraftAPI.registerTurtleUpgrade(TurtleUpgradeEx.newInstance(
                SclClientUpgrade.class,
                PID_SCL_CLIENT,
                blockSclClient,
                SclClientPeripheral.class));

        ComputerCraftAPI.registerTurtleUpgrade(TurtleUpgradeEx.newInstance(
                RayTracerUpgrade.class,
                PID_RAY_TRACER,
                blockRayTracer,
                RayTracerPeripheral.class));

        // ================================================================
        // サバイバル環境テスト用のボーナスチェスト
        // ================================================================
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(blockAllDirectionalComparator), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(blockMetaPlacer), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(blockMetaScanner), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(blockModemController), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(blockSclClient), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(Dependency.cc_turtle), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(Items.diamond), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(Blocks.planks), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(Blocks.coal_block), 64, 64, 100));
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(
                new ItemStack(Items.cooked_beef), 64, 64, 100));
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
