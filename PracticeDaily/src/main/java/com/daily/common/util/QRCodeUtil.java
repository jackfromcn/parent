package com.daily.common.util;

import com.daily.common.util.generate.UniqueID;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

/**
 * @author wencheng
 * @Description:
 * @date 2017/11/6
 */
public class QRCodeUtil {
    private static final String CHARSET = "utf-8";
    /**
     * 一维码宽度
     */
    private static int BARCODE_WIDTH = 80;
    /**
     * 二维码宽度
     */
    private static int QRCODE_WIDTH = 300;
    /**
     * 生成的图片格式
     */
    private static String FORMAT = "jpg";
    /**
     * 编码的颜色
     */
    private static int BLACK = 0x000000;
    /**
     * 空白的颜色
     */
    private static int WHITE = 0xFFFFFF;

    /**
     * 二维码中间的图像配置。注意，由于二维码的容错率有限，因此中间遮挡的面积不要太大，否则可能解析不出来。
     */
    private static int ICON_WIDTH = (int)(QRCODE_WIDTH / 6);
    private static int HALF_ICON_WIDTH = ICON_WIDTH / 2;
    /**
     * Icon四周的边框宽度
     */
    private static int FRAME_WIDTH = 2;

    /**
     * 将String编码成一维条形码后,使用字节数组表示,便于传输.
     * @param content
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] createBarCodeToBytes(String content)
            throws WriterException, IOException {
        BufferedImage image = createBarCode(content);
        return writeToBytes(image);
    }

    /**
     * 将String编码成二维码的图片后，使用字节数组表示，便于传输。
     *
     * @param content 内容
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] createQRCodeToBytes(String content)
            throws WriterException, IOException {
        BufferedImage image = createQRCode(content);
        return writeToBytes(image);
    }

    /**
     * 将String编码成二维码的图片&插入LOGO后，使用字节数组表示，便于传输。
     * @param content 内容
     * @param iconPath LOGO地址
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public static byte[] createQRCodeWithIncoToBytes(String content, String iconPath)
            throws IOException, WriterException {
        BufferedImage image = createQRCodeWithIcon(content, readByBytes(iconPath));
        return writeToBytes(image);
    }

    /**
     * 将String编码成二维码的图片&插入LOGO后，使用字节数组表示，便于传输。
     * @param content 内容
     * @param iconBytes LOGO字节数组
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public static byte[] createQRCodeWithIncoToBytes(String content, byte[] iconBytes)
            throws IOException, WriterException {
        BufferedImage image = createQRCodeWithIcon(content, readByBytes(iconBytes));
        return writeToBytes(image);
    }

    /**
     * 将String编码成一维条形码后,写入指定文件
     * @param content
     * @param barCodeFile
     * @throws WriterException
     * @throws IOException
     */
    public static void createBarCodeToFile(String content, File barCodeFile)
            throws WriterException, IOException {
        writeToFile(createBarCode(content),barCodeFile);
    }

    /**
     * 将String编码成二维码的图片后，写入指定文件
     * @param content 内容
     * @param qrCodeFile 二维码指定文件
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCodeToFile(String content, File qrCodeFile)
            throws WriterException, IOException {
        writeToFile(createQRCode(content), qrCodeFile);
    }

    /**
     * 将String编码成二维码的图片&插入LOGO后，写入指定文件
     * @param content 内容
     * @param iconPath LOGO地址
     * @param qrCodeFile 二维码指定文件
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCodeWithIncoToFile(String content, String iconPath, File qrCodeFile)
            throws WriterException, IOException {
        writeToFile(createQRCodeWithIcon(content, readByBytes(iconPath)), qrCodeFile);
    }

    /**
     * 将String编码成二维码的图片&插入LOGO后，写入指定文件
     * @param content 内容
     * @param iconBytes LOGO字节数组
     * @param qrCodeFile 二维码指定文件
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCodeWithIncoToFile(String content, byte[] iconBytes, File qrCodeFile)
            throws WriterException, IOException {
        writeToFile(createQRCodeWithIcon(content, readByBytes(iconBytes)), qrCodeFile);
    }

    /**
     * 将String编码成一维条形码的图片后，写入指定路径,fileName
     * @param content 内容
     * @param path 输出路径
     * @throws WriterException
     * @throws IOException
     */
    public static String createBarCodeToPath(String content, String path)
            throws WriterException, IOException {
        return writeToPath(createBarCode(content),path);
    }

    /**
     * 将String编码成二维码的图片后，写入指定路径,fileName
     * @param content 内容
     * @param path 输出路径
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static String createQRCodeToPath(String content, String path)
            throws WriterException, IOException {
        return writeToPath(createQRCode(content), path);
    }

    /**
     * 将String编码成二维码的图片&插入LOGO后，写入指定路径,fileName
     * @param content 内容
     * @param iconPath LOGO文件路径
     * @param path 输出路径
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static String createQRCodeWithIncoToPath(String content, String iconPath, String path)
            throws WriterException, IOException {
        return writeToPath(createQRCodeWithIcon(content, readByBytes(iconPath)), path);
    }

    /**
     * 将String编码成二维码的图片&插入LOGO后，写入指定路径,返回fileName
     * @param content 内容
     * @param iconBytes LOGO字节数组
     * @param path 输出路径
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static String createQRCodeWithIncoToPath(String content, byte[] iconBytes, String path)
            throws WriterException, IOException {
        return writeToPath(createQRCodeWithIcon(content, readByBytes(iconBytes)), path);
    }

    /**
     * 从一个二维码图片的字节信息解码出二维码中的内容。
     *
     * @param data 二维码字节数组
     * @return
     * @throws ReaderException
     * @throws IOException
     */
    public static String parseImage(byte[] data)
            throws ReaderException, IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        BufferedImage image = ImageIO.read(is);
        return parseImage(image);
    }

    /**
     * 从一个图片文件中解码出二维码中的内容。
     *
     * @param file 二维码文件
     * @return 解析后的内容。
     * @throws IOException
     * @throws ReaderException
     */
    public static String parseImage(File file)
            throws IOException, ReaderException {
        BufferedImage image = ImageIO.read(file);
        return parseImage(image);
    }

    /**
     * 将字符串编码成一维码（条形码）。
     * @param content 内容
     * @return
     * @throws WriterException
     * @throws IOException
     */
    private static BufferedImage createBarCode(String content)
            throws WriterException, IOException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        // 二维码读码器和写码器
        // 一维码的宽>高。这里我设置为 宽:高=2:1
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.EAN_13, BARCODE_WIDTH * 3, BARCODE_WIDTH,hints);
        return toBufferedImage(matrix);
    }

    /**
     * 把一个String编码成二维码的BufferedImage.
     *
     * @param content 内容
     * @return
     * @throws WriterException
     */
    private static BufferedImage createQRCode(String content)
            throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        // 长和宽一样，所以只需要定义一个SIZE即可
        // 二维码读码器和写码器
        BitMatrix matrix = new MultiFormatWriter().encode(
                content, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_WIDTH,hints);
        return toBufferedImage(matrix);
    }

    /**
     * 编码字符串为二维码，并在该二维码中央插入指定的图标。
     * @param content 内容
     * @param iconImage LOGO图片
     * @return
     * @throws WriterException
     */
    private static BufferedImage createQRCodeWithIcon(
            String content, BufferedImage iconImage) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        // 二维码读码器和写码器
        BitMatrix matrix = new MultiFormatWriter().encode(
                content, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_WIDTH,hints);
        // 读取Icon图像
        BufferedImage scaleImage = null;
        try {
            scaleImage = scaleImage(iconImage, ICON_WIDTH, ICON_WIDTH, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[][] iconPixels = new int[ICON_WIDTH][ICON_WIDTH];
        for (int i = 0; i < scaleImage.getWidth(); i++) {
            for (int j = 0; j < scaleImage.getHeight(); j++) {
                iconPixels[i][j] = scaleImage.getRGB(i, j);
            }
        }

        // 二维码的宽和高
        int halfW = matrix.getWidth() / 2;
        int halfH = matrix.getHeight() / 2;

        // 计算图标的边界：
        int minX = halfW - HALF_ICON_WIDTH;//左
        int maxX = halfW + HALF_ICON_WIDTH;//右
        int minY = halfH - HALF_ICON_WIDTH;//上
        int maxY = halfH + HALF_ICON_WIDTH;//下

        int[] pixels = new int[QRCODE_WIDTH * QRCODE_WIDTH];

        // 修改二维码的字节信息，替换掉一部分为图标的内容。
        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // 如果点在图标的位置，用图标的内容替换掉二维码的内容
                if (x > minX && x < maxX && y > minY && y < maxY) {
                    int indexX = x - halfW + HALF_ICON_WIDTH;
                    int indexY = y - halfH + HALF_ICON_WIDTH;
                    pixels[y * QRCODE_WIDTH + x] = iconPixels[indexX][indexY];
                }
                // 在图片四周形成边框
                else if ((x > minX - FRAME_WIDTH && x < minX + FRAME_WIDTH
                        && y > minY - FRAME_WIDTH && y < maxY + FRAME_WIDTH)
                        || (x > maxX - FRAME_WIDTH && x < maxX + FRAME_WIDTH
                        && y > minY - FRAME_WIDTH && y < maxY + FRAME_WIDTH)
                        || (x > minX - FRAME_WIDTH && x < maxX + FRAME_WIDTH
                        && y > minY - FRAME_WIDTH && y < minY + FRAME_WIDTH)
                        || (x > minX - FRAME_WIDTH && x < maxX + FRAME_WIDTH
                        && y > maxY - FRAME_WIDTH && y < maxY + FRAME_WIDTH)) {
                    pixels[y * QRCODE_WIDTH + x] = WHITE;
                }
                else {
                    // 这里是其他不属于图标的内容。即为二维码没有被图标遮盖的内容，用矩阵的值来显示颜色。
                    pixels[y * QRCODE_WIDTH + x] = matrix.get(x, y) ? BLACK : WHITE;
                }
            }
        }

        // 用修改后的字节数组创建新的BufferedImage.
        BufferedImage image = new BufferedImage(
                QRCODE_WIDTH, QRCODE_WIDTH, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, QRCODE_WIDTH, QRCODE_WIDTH, pixels);

        return image;
    }

    /**
     * 从图片中解析出一维码或者二维码的内容。如果解析失败，则抛出NotFoundException。
     * @param image
     * @return
     * @throws NotFoundException
     */
    private static String parseImage(BufferedImage image)
            throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        // 二维码读码器和写码器
        Result result = new MultiFormatReader().decode(bitmap,hints);
        // 这里丢掉了Result中其他一些数据
        return result.getText();
    }

    /**
     * 将一个BitMatrix对象转换成BufferedImage对象
     *
     * @param matrix
     * @return
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标。
     * @param srcImage 源图片
     * @param height 目标高度
     * @param width 目标宽度
     * @param hasFiller 比例不对时是否需要补白：true为补白; false为不补白;
     * @return
     * @throws IOException
     */
    private static BufferedImage scaleImage(BufferedImage srcImage, int height,
                                            int width, boolean hasFiller) throws IOException {
        double ratio = 0.0; // 缩放比例
        Image destImage = srcImage.getScaledInstance(
                width, height, BufferedImage.SCALE_SMOOTH);
        // 计算比例
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
            if (srcImage.getHeight() > srcImage.getWidth()) {
                ratio = (new Integer(height)).doubleValue() / srcImage.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue() / srcImage.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(
                    AffineTransform.getScaleInstance(ratio, ratio), null);
            destImage = op.filter(srcImage, null);
        }
        if (hasFiller) {// 补白
            BufferedImage image = new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = image.createGraphics();
            graphic.setColor(Color.white);
            graphic.fillRect(0, 0, width, height);
            if (width == destImage.getWidth(null)) {
                graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2,
                        destImage.getWidth(null), destImage.getHeight(null), Color.white, null);
            } else {
                graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0,
                        destImage.getWidth(null), destImage.getHeight(null), Color.white, null);
            }
            graphic.dispose();
            destImage = image;
        }
        return (BufferedImage) destImage;
    }

    /**
     * 将BufferedImage对象输出到指定的目录中,返回fileName
     * @param image
     * @param path 指定目录
     * @return
     * @throws IOException
     */
    private static String writeToPath(BufferedImage image, String path) throws IOException {
        String fileName = UniqueID.uuid() + "." + FORMAT;
        if (path.endsWith("/")) {
            path = path.substring(0,path.length()-1);
        }
        path = path + "/" + fileName;
        File destFile = new File(path);
        writeToFile(image, destFile);
        return fileName;
    }

    /**
     * 将BufferedImage对象输出到指定的文件中。
     *
     * @param image
     * @param destFile
     * @throws IOException
     */
    private static void writeToFile(BufferedImage image, File destFile)
            throws IOException {
        ImageIO.write(image, FORMAT, destFile);
    }

    /**
     * 将BufferedImage对象输出成字节数组，便于传输。
     * @param image
     * @return
     * @throws IOException
     */
    private static byte[] writeToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT, os);
        return os.toByteArray();
    }

    /**
     * 将输入字节数组输出成BufferedImage
     * @param srcBytes
     * @return
     * @throws IOException
     */
    private static BufferedImage readByBytes(byte[] srcBytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(srcBytes);
        return ImageIO.read(in);
    }

    /**
     * 将目标文件输出成BufferedImage
     * @param srcImageFile 目标文件
     * @return
     * @throws IOException
     */
    private static BufferedImage readByBytes(String srcImageFile) throws IOException {
        File file = new File(srcImageFile);
        return ImageIO.read(file);
    }

}
