package kssr3951.nicheperipherals.application.metascanner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kssr3951.nicheperipherals.system.peripheral.PeripheralEx;
import net.minecraft.block.Block;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class PeripheralMetaScanner extends PeripheralEx {

    public PeripheralMetaScanner() {
    }

    private static void add64String(int[] trio, StringBuffer sb) {
        //              0--------1---------2---------3---------4---------5---------61234
        String str64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?!";
        int val = (trio[0] << 16) + (trio[1] << 8) + trio[2];
        sb.append(str64.substring((val & Integer.parseInt("111111000000000000000000", 2)) >> 18).substring(0, 1));
        sb.append(str64.substring((val & Integer.parseInt("000000111111000000000000", 2)) >> 12).substring(0, 1));
        sb.append(str64.substring((val & Integer.parseInt("000000000000111111000000", 2)) >>  6).substring(0, 1));
        sb.append(str64.substring((val & Integer.parseInt("000000000000000000111111", 2)) >>  0).substring(0, 1));
    }

    public static String getHashForDetectorTurtle(Block block, int metadata) {
        String text = "detector" + Block.blockRegistry.getNameForObject(block) + metadata;
        MessageDigest md = null;
        try {
            //md = MessageDigest.getInstance("SHA-256");
            // ハッシュの衝突なんて気にしない。
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "error.";
        }

        // 得られたハッシュ値をbase64風の文字列に変換する
        StringBuffer sb = new StringBuffer();
        byte[] digest = md.digest(text.getBytes());
        int[] trio = new int[3];

        int len = digest.length;
        if (len % 3 != 0) {
            len += (3 - (len % 3));
        }
        for (int i = 0; i < len; i++) {
            trio[i % 3] = i < digest.length ? digest[i] + 128 : 0;
            if (i % 3 == 3 - 1) {
                add64String(trio, sb);
            }
        }
        return sb.toString();
    }
}
