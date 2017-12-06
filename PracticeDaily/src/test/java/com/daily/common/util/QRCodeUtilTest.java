package com.daily.common.util;

import com.daily.common.util.generate.UniqueID;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author wencheng
 * @Description:
 * @date 2017/11/22
 */
public class QRCodeUtilTest {
    private static String FORMAT = "jpg";
    private String iconPath = "/Users/wencheng/Downloads/timg.jpeg";
    private String content = "http://www.baidu.com";
    private String path = "/Users/wencheng/Desktop/test_space/parent/PracticeDaily/src/test/";
    private String barCodeContent="6936983800013";
    private byte[] srcBytes = null;

    @Before
    public void init() throws IOException {
        ByteOutputStream out = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(iconPath);
            out = new ByteOutputStream();
            out.write(in);
            srcBytes = out.getBytes();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Test
    public void testCreateFileAndParse() throws Exception {
        /**
         * 二维码测试。
         */

        File qrCode = new File(path + UniqueID.uuid() + "." + FORMAT);
        File qrCodeWithIcon = new File(path + UniqueID.uuid() + "." + FORMAT);
        // 生成二维码
//        QRCodeUtil.createQRCodeToFile(content, qrCode);
        // 生成带图标的二维码
//        QRCodeUtil.createQRCodeWithIncoToFile(content,iconPath, qrCodeWithIcon);
        QRCodeUtil.createQRCodeWithIncoToFile(content,srcBytes,qrCodeWithIcon);

        // 解析二维码
//        System.out.println(QRCodeUtil.parseImage(qrCode));
        // 解析带图标的二维码
        System.out.println(QRCodeUtil.parseImage(qrCodeWithIcon));

        /**
         * 一维码测试。
         */
        // 解析一维码
        File barCode = new File(path + UniqueID.uuid() + "." + FORMAT);
        // 生成一维码
        QRCodeUtil.createBarCodeToFile(barCodeContent,barCode);
        System.out.println(QRCodeUtil.parseImage(barCode));
    }

    @Test
    public void testCreatePathAndParse() throws Exception {
        System.out.println(QRCodeUtil.parseImage(new File(path + "/" + QRCodeUtil.createBarCodeToPath(barCodeContent, path))));
        System.out.println(QRCodeUtil.parseImage(new File(QRCodeUtil.createQRCodeToPath(content, path))));
        System.out.println(QRCodeUtil.parseImage(new File(QRCodeUtil.createQRCodeWithIncoToPath(content,iconPath,path))));
        System.out.println(QRCodeUtil.parseImage(new File(QRCodeUtil.createQRCodeWithIncoToPath(content,srcBytes,path))));
    }

    @Test
    public void testCreateBytesAndParse() throws Exception {
        // 编码成字节数组
        System.out.println(QRCodeUtil.parseImage(QRCodeUtil.createBarCodeToBytes(barCodeContent)));
        System.out.println(QRCodeUtil.parseImage(QRCodeUtil.createQRCodeToBytes(content)));
        System.out.println(QRCodeUtil.parseImage(QRCodeUtil.createQRCodeWithIncoToBytes(content,iconPath)));
        System.out.println(QRCodeUtil.parseImage(QRCodeUtil.createQRCodeWithIncoToBytes(content,srcBytes)));
    }

}
