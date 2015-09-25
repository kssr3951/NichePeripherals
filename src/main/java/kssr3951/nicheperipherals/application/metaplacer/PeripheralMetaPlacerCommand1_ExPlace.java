package kssr3951.nicheperipherals.application.metaplacer;

import java.util.ArrayList;
import java.util.List;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import kssr3951.nicheperipherals.application.metascanner.PeripheralMetaScanner;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.Facing;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class PeripheralMetaPlacerCommand1_ExPlace implements ITurtleCommand {

    private int dir;
    private Object[] arguments;

    private String scannedBlockName;
    private int scannedMetadata;

    public PeripheralMetaPlacerCommand1_ExPlace(int dir, Object[] arguments) {

        this.dir = dir;
        this.arguments = arguments;
    }

    public String getScannedBlockName() {
        return scannedBlockName;
    }

    public int getScannedMetadata() {
        return scannedMetadata;
    }

    @Override
    public TurtleCommandResult execute(ITurtleAccess givenTurtle) {

        ITurtleAccess turtle = givenTurtle;

        ItemStack stack = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
        if (stack == null) {
            return TurtleCommandResult.failure("No items to place");
        }

        System.out.println("instance =[" + stack.getItem().getClass().getName() + "]");
        if (!(stack.getItem() instanceof ItemBlock)) {
            return TurtleCommandResult.failure("It's not ItemBlock");
        }

        stack.getItemDamage();
        
        Block block = ((ItemBlock)stack.getItem()).field_150939_a;
        
        int newX = turtle.getPosition().posX + Facing.offsetsXForSide[this.dir];
        int newY = turtle.getPosition().posY + Facing.offsetsYForSide[this.dir];
        int newZ = turtle.getPosition().posZ + Facing.offsetsZForSide[this.dir];

        String argScanCode = (String)arguments[0];

        if (block.equals(Blocks.noteblock)) {
            return executeNoteBlock(argScanCode, givenTurtle, stack, block, newX, newY, newZ);
        } else {
            return executeOtherBlock(argScanCode, givenTurtle, stack, block, newX, newY, newZ);
        }
    }
    
    private int findMatchedMetadata(Block block, String argScanCode, int maxMeta) {
        int matchedMetadata = -1;
        for (int i = 0; i <= 15; i++) {
            String test = PeripheralMetaScanner.getHashForDetectorTurtle(block, i);
            if (argScanCode.equals(test)) {
                matchedMetadata = i;
                break;
            }
        }
        return matchedMetadata;
    }
    
    private boolean setBlockAndDecreaseInventory(ITurtleAccess turtle, Block block, int matchedMetadata, int newX, int newY, int newZ) {

        if (!block.isReplaceable(turtle.getWorld(), newX, newY, newZ)) {
            return false;
        }
        
        int slotNum = turtle.getSelectedSlot();
        ItemStack currentStack = turtle.getInventory().getStackInSlot(slotNum);
        currentStack.stackSize --;
        if (currentStack.stackSize == 0) {
            turtle.getInventory().setInventorySlotContents(slotNum, null);
        }
        turtle.getWorld().setBlock(newX, newY, newZ, block);

        // 効果音を鳴らす
        turtle.getWorld().playAuxSFX(2001, newX, newY, newZ, Block.getIdFromBlock(block) + matchedMetadata * 4096);
        
        return true;
    }
    
    private TurtleCommandResult executeOtherBlock(String argScanCode, ITurtleAccess turtle, ItemStack stack, Block block, int newX, int newY, int newZ) {
        
        // メタデータの最大値は15（0 ～ 15）
        int matchedMetadata = findMatchedMetadata(block, argScanCode, 15);
        if (-1 == matchedMetadata) {
            // アイテム名が一致しないのでエラー
            return TurtleCommandResult.failure("wrong scan code");
        }

        if (matchedMetadata != stack.getItemDamage()) {
            // アイテムのダメージ値と 【scan codeに記録した、配置の為のメタデータの値】 が異なる場合があるが、
            // それを良しとする場合としない場合がある。
            
            // （【階段ブロックなど】） Itemのダメージ値は常に0だが、worldに設置されている時のメタデータは、向きによって異なる場合
            // （【羊毛ブロックなど】） Itemのダメージ値で色が表現されており、worldに設置されている場合のメタデータの値もそのダメージ値である場合
            
            // ～ 階段ブロックの場合、Itemのダメージ値と配置時のメタデータは関係無い為、scan codeから取得されたメタデータをそのまま適用したい。
            //   （scanを行った際の対象ブロックの向きを再現するのが目的なので。）
            // ～羊毛ブロックの場合、Itemのダメージ値と配置時のメタデータの値は同じでないといけない為、scan codeから取得されたメタデータと一致しない場合はエラーとしなければいけない。
            //   （所持しているのが赤い羊毛ブロックなのに、青い羊毛ブロックとして配置できたら不具合なので。）
            
            // 上記の内容を実現する為に、getSubBlock()でとれたブロックのメタ値とmatchedMetadataが一致する場合は別ブロックがあるのでエラーとしています。

            Item it = stack.getItem();
            @SuppressWarnings("rawtypes")
            List returnList = new ArrayList();
            block.getSubBlocks(it, it.getCreativeTab(), returnList);
            for (int i = 0; i < returnList.size(); i++) {
                ItemStack st = (ItemStack) returnList.get(i);
                
                System.out.println("  [" + i + "] item name : " + st.getItem().getUnlocalizedName());
                System.out.println("        damage : " + st.getItemDamage());
                Block blk = ((ItemBlock)stack.getItem()).field_150939_a;
                System.out.println("        block () : " + blk.getUnlocalizedName());
                System.out.println("        block (name for object) : " + Block.blockRegistry.getNameForObject(blk));
                
                if (matchedMetadata == st.getItemDamage()) {
                    // ダメージ値違いの別ブロックが存在するのでエラー
                    return TurtleCommandResult.failure("wrong scan code");
                }
            }
            // ※ここに抜けられる場合は、”階段タイプ”なので、matchedMetadataの値をそのまま使う。
        }
        
        // ブロックの配置を試みる。配置できた場合はインベントリから差し引く
        boolean result = setBlockAndDecreaseInventory(turtle, block, matchedMetadata, newX, newY, newZ);
        if (result) {
            // メタデータを変更する
            turtle.getWorld().setBlockMetadataWithNotify(newX, newY, newZ, matchedMetadata, 7);
            // 処理成功
            return TurtleCommandResult.success();
            
        } else {
            // 処理失敗
            return TurtleCommandResult.failure("Cannot place block here");
        }
    }
    
    private TurtleCommandResult executeNoteBlock(String argScanCode, ITurtleAccess turtle, ItemStack stack, Block block, int newX, int newY, int newZ) {

        // 音ブロックは25音（0 ～ 24）
        int matchedMetadata = findMatchedMetadata(block, argScanCode, 24);
        if (-1 == matchedMetadata) {
            return TurtleCommandResult.failure("wrong scan code");
        }

        // ブロックの配置を試みる。配置できた場合はインベントリから差し引く
        boolean result = setBlockAndDecreaseInventory(turtle, block, matchedMetadata, newX, newY, newZ);
        if (result) {
            // 音ブロックの音階の情報はタイルエンティティ―なのでそこに値を戻す
            TileEntityNote tileentitynote = (TileEntityNote)turtle.getWorld().getTileEntity(newX, newY, newZ);
            if (tileentitynote != null && (0 <= matchedMetadata && matchedMetadata <= 24)) {
                byte old = tileentitynote.note;
                tileentitynote.note = (byte) matchedMetadata;
                if (net.minecraftforge.common.ForgeHooks.onNoteChange(tileentitynote, old)) {
                    tileentitynote.markDirty();
                } 
            }
            // 処理成功
            return TurtleCommandResult.success();

        } else {
            // 処理失敗
            return TurtleCommandResult.failure("Cannot place block here");
        }
    }
}
