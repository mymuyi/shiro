package chapter5;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.BlowfishCipherService;
import org.apache.shiro.crypto.DefaultBlockCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;

import java.security.Key;

/**
 * @Author: yangke
 * @Date: 2019/8/23
 */
public class CodecAndCryptoTest {

    @Test
    public void testBase64() {
        String str = "hello";
        String base64Encoded = Base64.encodeToString(str.getBytes());
        System.out.println(base64Encoded);
        String str2 = Base64.decodeToString(base64Encoded);
        Assert.assertEquals(str, str2);
    }

    @Test
    public void testHex() {
        String str = "hello";
        String hexEncoded = Hex.encodeToString(str.getBytes());
        System.out.println(hexEncoded);
        String str2 = new String(Hex.decode((hexEncoded.getBytes())));
        Assert.assertEquals(str, str2);
    }

    @Test
    public void testCodecSupport() {
        String str = "hello";
        byte[] bytes = CodecSupport.toBytes(str, "utf-8");
        String str2 = CodecSupport.toString(bytes, "utf-8");
        System.out.println(str2);
        Assert.assertEquals(str, str2);
    }

    @Test
    public void testRandom() {
        SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        generator.setSeed("123".getBytes());
        System.out.println(generator.nextBytes().toHex());
    }

    @Test
    public void testMD5() {
        String str = "hell0";
        String salt = "123";
        String md5 = new Md5Hash(str, salt).toString(); // toBase64()/toHex()
        System.out.println(md5);
    }

    @Test
    public void testSha() {
        String str = "hello";
        String salt = "123";

        String sha1 = new Sha1Hash(str, salt).toString();
        System.out.println(sha1);

        String sha256 = new Sha256Hash(str, salt).toString();
        System.out.println(sha256);

        String sha384 = new Sha384Hash(str, salt).toString();
        System.out.println(sha384);

        String sha512 = new Sha512Hash(str, salt).toString();
        System.out.println(sha512);
    }

    @Test
    public void simpleHash() {
        String str = "hello";
        String salt = "123";
        String simpleHash = new SimpleHash("SHA-1", str, salt).toString();
        System.out.println(simpleHash);
    }

    @Test
    public void testHashService() {
        // 默认算法：SHA-512
        DefaultHashService hashService = new DefaultHashService();
        // 算法设置
        hashService.setHashAlgorithmName("SHA-512");
        // 私盐，散列时自动与用户传入的公盐混合产生一个新盐，默认无
        hashService.setPrivateSalt(new SimpleByteSource("123"));
        // 是否生成公盐，默认 false
        hashService.setGeneratePublicSalt(true);
        // 生成公盐，默认也是这个
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());
        // hash 值迭代次数
        hashService.setHashIterations(1);

        // 传入算法、数据、公盐、迭代次数
        HashRequest request = new HashRequest.Builder()
                .setSource(ByteSource.Util.bytes("hello"))
                .setAlgorithmName("MD5")
                .setSalt(ByteSource.Util.bytes("123"))
                .setIterations(2)
                .build();
        String hex = hashService.computeHash(request).toHex();
        System.out.println(hex);
    }

    @Test
    public void testAesCipherService() {
        AesCipherService aesCipherService = new AesCipherService();

        // 设置 key 长度
        aesCipherService.setKeySize(128);

        // 生成 key
        Key key = aesCipherService.generateNewKey();

        String text = "hello";

        // 加密
        String encryptText = aesCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
        System.out.println(encryptText);

        // 解密
        String text2 = new String(aesCipherService.decrypt(Hex.decode(encryptText), key.getEncoded()).getBytes());

        Assert.assertEquals(text, text2);

    }

    @Test
    public void testBlowfishCipherService() {

        BlowfishCipherService blowfishCipherService = new BlowfishCipherService();

        blowfishCipherService.setKeySize(128);

        Key key = blowfishCipherService.generateNewKey();

        String text = "hello";

        String encryptText = blowfishCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();

        String text2 = new String(blowfishCipherService.decrypt(Hex.decode(encryptText), key.getEncoded()).getBytes());

        Assert.assertEquals(text, text2);

    }

    @Test
    public void testDefaultBlockCipherService() throws Exception {

        //对称加密，使用Java的JCA（javax.crypto.Cipher）加密API，常见的如 'AES', 'Blowfish'
        DefaultBlockCipherService cipherService = new DefaultBlockCipherService("AES");

        cipherService.setKeySize(128);

        //生成key
        Key key = cipherService.generateNewKey();

        String text = "hello";

        //加密
        String encryptText = cipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
        //解密
        String text2 = new String(cipherService.decrypt(Hex.decode(encryptText), key.getEncoded()).getBytes());

        Assert.assertEquals(text, text2);
    }

}
