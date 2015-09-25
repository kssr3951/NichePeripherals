package kssr3951.nicheperipherals.system.blocks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
abstract public class BlockEx extends Block {

    private int renderId = -1;
    private Block sotogawaBlock;
    private String blockName;
    
    /** レシピの（材料の）情報。ブロック破壊時に返すアイテムを指定する。 */
    private ItemStack[] ingredients;

    @SideOnly(Side.CLIENT)
    protected IIcon peripheralIcon;
 
    public static BlockEx newInstance(@SuppressWarnings("rawtypes") Class blockExClass, String blockName, CreativeTabs creativeTabs, String textureName, Block sotogawaBlock) {
        BlockEx blockEx = null;
        try {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            Constructor ct = blockExClass.getConstructor(Material.class);
            blockEx = (BlockEx) ct.newInstance(Material.glass);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        blockEx.blockName = blockName;
        blockEx.sotogawaBlock = sotogawaBlock;
        blockEx.setBlockName(blockName);
        blockEx.setHardness(0.3F);
        blockEx.setStepSound(soundTypeGlass);
        blockEx.setCreativeTab(creativeTabs);
        blockEx.setBlockTextureName(textureName);
        return blockEx;
    }
    
    protected BlockEx(Material material) {
        super(material);
    }
    
    public void setRenderId(int renderId) {
        this.renderId = renderId;
    }

    public final Block getSotogawaBlock() {
        return this.sotogawaBlock;
    }
    
    public final String getBlockName() {
        return this.blockName;
    }
    
    public final IIcon getPeripheralIcon() {
        return this.peripheralIcon;
    }
    
    public void setIngredients(ItemStack[] ingredients) {
        this.ingredients = ingredients;
    }
    // =====================================================================================
    // Blockのオーバーライド
    // =====================================================================================
    @Override
    public final boolean renderAsNormalBlock(){
        return false;
    }
    @Override
    public final boolean isOpaqueCube() {
        return false;
    }
    @Override
    public final int damageDropped(int metadata) {
        return metadata;
    }
    @Override
    public final int getRenderType() {
        return this.renderId;
    }
    @Override
    public final boolean canHarvestBlock(EntityPlayer player, int meta) {
        return false;
    }
    @Override
    public final void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        if (null == this.ingredients) {
            return;
        }
        Random random = new Random(System.currentTimeMillis());
        for (int i1 = 0; i1 < ingredients.length; i1++)
        {
            ItemStack itemstack = new ItemStack(ingredients[i1].getItem(), ingredients[i1].stackSize, ingredients[i1].getItemDamage());

            if (itemstack != null)
            {
                float f = random.nextFloat() * 0.8F + 0.1F;
                float f1 = random.nextFloat() * 0.8F + 0.1F;
                float f2 = random.nextFloat() * 0.8F + 0.1F;

                while (itemstack.stackSize > 0)
                {
                    int j1 = random.nextInt(21) + 10;

                    if (j1 > itemstack.stackSize)
                    {
                        j1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= j1;
                    EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                    if (itemstack.hasTagCompound())
                    {
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }

                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)random.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)random.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)random.nextGaussian() * f3);
                    world.spawnEntityInWorld(entityitem);
                }
            }
        }
        world.func_147453_f(x, y, z, block);
    }
}
