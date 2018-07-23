package com.hcb.hcbsdk.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.aztec.encoder.Encoder;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * 条码码操作类。生成时需要特别注意，条码格式对字符编码有要求，有的不支持中文、字母等，需要依据格式额外处理。
 * 注意，配置：
 * <p> {@link BarcodeFormat#RSS_EXPANDED, BarcodeFormat#RSS_14, BarcodeFormat#UPC_EAN_EXTENSION}
 * 暂未支持。
 */
public class BarcodeUtils {

    /**
     * 默认前景色黑色，背景色透明
     * 生成一二维码
     * @param content 二维码文字内容
     * @param width   图片像素宽
     * @param height  图片像素高
     * @param format  二维码图片的编码方式
     * @return 二维码bitmap
     */
    public static Bitmap createBarcode(String content, int width, int height, BarcodeFormat format) {
        return createBarcode(content, width, height, format, 0xff000000, 0x00000000);
    }
    /**
     * 生成二维码（部分一维码不能使用此方法）
     * @param content   二维码文字内容
     * @param width     图片像素宽
     * @param height    图片像素高
     * @param format    二维码图片的编码方式
     * @param foreColor 前景色
     * @param backColor 背景色
     * @return Bitmap对象
     */
    public static Bitmap createBarcode(String content, int width, int height, BarcodeFormat format, int foreColor, int backColor) {
        return createBarcode(content, "utf-8", width, height, format, foreColor, backColor);
    }

    /**
     * 生成一二维码
     * @param content     二维码文字内容
     * @param charset 字符编码
     * @param width       图片像素宽
     * @param height      图片像素高
     * @param format      二维码图片的编码方式
     * @param foreColor   前景色
     * @param backColor   背景色
     * @return Bitmap对象
     */
    public static Bitmap createBarcode(String content, String charset, int width, int height, BarcodeFormat format, int foreColor, int backColor) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (TextUtils.isEmpty(charset)){
            charset = "utf-8";
        }
        //配置参数
        HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, charset);//字符串编码

        //错误纠正，Aztec格式，pdf417格式和其它条码配置不同，不能通用，否则会产生错误。
        if (format == BarcodeFormat.AZTEC) {//错误校正词的最小百分比
            hints.put(EncodeHintType.ERROR_CORRECTION, Encoder.DEFAULT_EC_PERCENT);//默认，可以不设
        } else if (format == BarcodeFormat.PDF_417) {
            hints.put(EncodeHintType.ERROR_CORRECTION, 2);//纠错级别，允许为0到8。默认2，可以不设
        } else {
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//
        }
        //设置空白边距的宽度,默认值为4
        hints.put(EncodeHintType.MARGIN, 2);// 设置边距（默认值5）
        hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);

        return createBarcode(content, width, height, format, hints, foreColor, backColor);
    }

    /**
     * 生成一二维码。
     * @param content   文字内容
     * @param format    条码编码方式
     * @param hints     条码其他配置对象
     * @param foreColor 条码前景色
     * @param backColor 条码背景色
     * @return bitmap 条码bitmap对象
     */
    public static Bitmap createBarcode(String content, int width, int height, BarcodeFormat format, HashMap<EncodeHintType, Object> hints, int foreColor, int backColor) {
        return createBarcode(content, width, height, format, hints, foreColor, backColor, true);
    }

    /**
     * QR二维码生成
     *
     * @param content
     * @param widthAndHeight
     * @return 二维码bitmap
     */
    public static Bitmap createQRCode(String content, int widthAndHeight) {
        return createBarcode(content, widthAndHeight, widthAndHeight, BarcodeFormat.QR_CODE);
    }

    /**
     * 128条形码
     *
     * @param content
     * @param width
     * @param height
     * @return 一维码bitmap
     */
    public static Bitmap create128Code(String content, int width, int height) {
        return createBarcode(content, width, height, BarcodeFormat.CODE_128);
    }

    /**
     * 条形码生成方法
     *
     * @param content     文字内容
     * @param width       宽
     * @param height      高
     * @param format      条形码格式，pdf417，code128，QRcode等
     * @param hints       条码的Map配置
     * @param foreColor   前景色
     * @param backColor   背景色
     * @param fitSizeFlag 是否自动适应到预期的宽高（data matrix格式的二维码，生成时非常小，需要放大，其他格式也可能不到预期的大小）
     *                    <p> true，使用等间采样算法，缩放bitmap到期望的尺寸
     *                    <p> false则不处理
     * @return 条码
     */
    public static Bitmap createBarcode(String content, int width, int height, BarcodeFormat format,
                                       Map<EncodeHintType, Object> hints, int foreColor, int backColor, boolean fitSizeFlag) {
        // 图像数据转换，使用了矩阵转换
        BitMatrix bitMatrix = null;
        try {
            DataMatrixWriter multiFormatWriter = new DataMatrixWriter();
            bitMatrix = multiFormatWriter.encode(content, format, width, height, hints);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitMatrix == null) {
            return null;
        }
        //注，以下代码修正DATA_MATRIX和PDF_417生成错误。
        //网络上广泛使用预设的width和height生成像素矩阵，因这两种格式的二维码，其bitMatrix不一定等于预期的像素矩阵（width * height）
        //会导致数组下标异常
        int bitWidth = bitMatrix.getWidth();
        int bitHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitWidth * bitHeight];

        //遍历bitmatrix，为像素矩阵按一行行（横列）设置像素颜色。
        for (int y = 0; y < bitHeight; y++) {
            for (int x = 0; x < bitWidth; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * (bitWidth) + x] = foreColor;
                } else {
                    pixels[y * bitWidth + x] = backColor;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitWidth, bitHeight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, bitWidth, 0, 0, bitWidth, bitHeight);

        Log.d(">>>", "预期宽高：" + width + " * " + height + "  矩阵宽高：" + bitWidth + " * " + bitHeight);
        if (fitSizeFlag) {
            //因为bitmap可能并不等于预先设置的width和height，需要进行等比缩放，尤其是BarcodeFormat.DATA_MATRIX格式，小的不可想象
            float wMultiple = ((float) bitWidth) / (float) width;//生成的bitmap的宽除以预期的宽
            float hMultiple = ((float) bitHeight) / (float) height;//生成的bitmap的高除以预期的高
            Log.d(">>>", wMultiple + "--" + hMultiple);
            if (wMultiple == 1f || hMultiple == 1f) {//说明生成的条形码符合预期，不需要缩放
                Log.d(">>>", "...re1");
                return bitmap;
            }

            if (wMultiple > hMultiple) {//说明宽超出范围更多，以宽的比例为标准进行缩放。
                int dstWidth = width;// bitWidth / wMultiple
                int dstHeight = height;//(int) (bitHeight / wMultiple);
//          bitmap = Bitmap.createScaledBitmap(bitmap,dstWidth,dstHeight,true);//安卓的这个方法不行

                bitmap = BitmapFlex.flex(bitmap, dstWidth, dstHeight);//等间采样算法进行缩放
            } else {//说明相当或高超出范围更多，以高的比例为标准进行缩放。
                int dstHeight = height;// bitHeight / hMultiple
                int dstWidth = (int) (bitWidth / hMultiple);

                bitmap = BitmapFlex.flex(bitmap, dstWidth, dstHeight);//等间采样算法进行缩放
            }
        }
        return bitmap;
    }

    /**
     * 一、二维码的解析
     * @param bitmap 图片对象
     * @param charaterset 编码。以此文字编码解析一二维码文字内容
     * @return 解析结果
     */
    public static Result parseCode(Bitmap bitmap, String charaterset) {
        Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, charaterset);
        return parseBarCode(bitmap,hints);
    }

    /**
     * 一二维码的解析
     * @param bitmap 图片对象
     * @param hints 解码map配置
     * @return 解析结果
     */
    public static Result parseBarCode(Bitmap bitmap, Map<DecodeHintType,?> hints){
        MultiFormatReader formatReader = new MultiFormatReader();

        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int[] pixels = new int[bw * bh];
        bitmap.getPixels(pixels, 0, bw, 0, 0, bw, bh);
        LuminanceSource source = new RGBLuminanceSource(bw, bh, pixels);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        Result result = null;
        try {
            result = formatReader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 在bitma中间添加Logo图案（用于QR二维码等场合）
     * @param src 原图
     * @param logo logo图
     */
    public static void drawLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return;
        }

        if (logo == null) {
            return;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        //		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(src);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
}
