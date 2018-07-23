package sdk.com.huacai.hcb_sdk.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 位图工具
 */
public class BitmapUtil {

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		//canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap createColorBitmap(int width,int height,int color){
		return createColorBitmap(width, height, color, Config.ARGB_8888);
	}
	public static Bitmap createColorBitmap(int width,int height,int color,Config config){
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(color);
		return bitmap;
	}

	/**
	 * <p> Config.RGB_565 ：图片由RGB 3 个通道构成，位深为 5+6+5 = 16
	 * <p> Config.ARGB_8888 ：图片由RGB 4 个通道构成，位深为 8*4 = 32
	 * @param path 图像文件路径
	 * @param options 图片加载配置
	 */
    private static void preprocessBitmap(String path,Options options){
        if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
            Log.w("图片加载越界", "there is an error trying to decode, outWidth will be set to -1.");
            return ;
        }
        double w = options.outWidth;
        double h = options.outHeight;

        long allowMemorySize = (Runtime.getRuntime().maxMemory())/8;//允许最大内存的8分之一来加载图片
		Log.d(">>>","最大准许内存："+allowMemorySize/1024/1024+"M"+"  w:"+w+" h:"+h);
        double pixlSizeMeo = w*h;//占用内存大小
        double allowPixl = pixlSizeMeo;

        if (StringRegex.endWith(path,"jpg",true)){
            options.inPreferredConfig = Config.RGB_565;//一个像素占2byte
            pixlSizeMeo = pixlSizeMeo*2;//实际占用内存 = 宽*高*像素点占内存位数
            if (pixlSizeMeo>allowMemorySize){//使像素不超过准许的内存大小
                allowPixl = allowMemorySize/2;//准许的像素大小 （宽 * 高）
            }
        }else {
            options.inPreferredConfig = Config.ARGB_8888;//一个像素4byte
            pixlSizeMeo = pixlSizeMeo*4;
            if (pixlSizeMeo>allowMemorySize){
                allowPixl = allowMemorySize/4;
            }
        }
        // w * (w* h/w ) = allow ,依据图片进行等比换算，准许的像素宽
        double w2 = Math.sqrt((allowPixl / (h/w) ));
        // h * (h* w/h) = allow 依据图片进行等比换算，准许的像素高
        double h2 = Math.sqrt( (allowPixl / (w/h) ));
        Log.d(">>>","最大准许像素："+w2+" x "+ h2+"");

        boolean flag = w*h > w2*h2;
        if (flag) {//如果图片实际像素比准许像素大
            double scale = (h>w)? (h/h2): (w/w2);
            //向上取整，使之始终不超过准许像素
            options.inSampleSize = (int) Math.ceil(scale);//4，则图片宽/4，高/4，像素=原像素/16
        }
        Log.d(">>>", "inSampleSize:" + options.inSampleSize+" 宽高："+options.outWidth+" x "+options.outHeight);
        options.inJustDecodeBounds = false;
		options.inPurgeable = true;
        options.inDither = false;

    }
	/**
	 * 此方法加载的图片，限制其占用的内存大小 < 应用允许使用的最大内存的8分之一。
	 * 只适用于普通场合。繁复的图片加载需要选用框架。
	 * <P> 图片占用内存大小（单位bit） = 像素*位深/8
	 * <p> 图像位深是指存储每个像素所用的位数（二进制bit）。它确定图像的每个像素可能具有的颜色数。例如8位深图片由最多由256（2的8次方）种颜色级别构成。
	 * <p> 可以据此验证：1024*1024 像素图在保存时，位深24，无压缩，无额外数据，文件大小等于3M。
	 * @param path
	 * @return bitmap
	 */
	 public static Bitmap loadBitmap(String path)  {
        File file = new File(path);
        if (!file.canRead()){
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        preprocessBitmap(path,options);
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * 此方法加载的图片，限制其占用的内存大小 < 应用允许使用的最大内存的8分之一。
	 *
	 * <P> 图片占用内存大小（单位bit） = 像素*位深/8
	 * <p> 图像位深是指存储每个像素所用的位数（二进制bit）。它确定图像的每个像素可能具有的颜色数。例如8位深图片由最多由256（2的8次方）种颜色级别构成。
	 * <p> 可以据此验证：1024*1024 像素图在保存时，位深32位，无压缩，无额外数据，文件大小等于4M。
	 * @param res
	 * @param id
	 * @return bitmap
	 */
	public static Bitmap loadBitmap(Resources res,int id){
        String path = res.getResourceName(id);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,id,options);
        preprocessBitmap(path,options);
        return BitmapFactory.decodeResource(res,id,options);
    }
	public static Bitmap drawBackgroundColor(Bitmap src,int color){
		if(src.getConfig().equals(Config.RGB_565)){//无透明通道，不必刷背景
			return src;
		}
		Bitmap re =Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(re);
		canvas.drawColor(color);
		canvas.drawBitmap(src, 0, 0, null);
		return re;
	}
	public static boolean saveBitmap(Bitmap bitmap, CompressFormat compressFormat,File file){
		try {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()){
				parentFile.mkdirs();
			}
			OutputStream os = new FileOutputStream(file);
			boolean flag = bitmap.compress(compressFormat,100,os);
			os.flush();
			os.close();
			return flag;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
