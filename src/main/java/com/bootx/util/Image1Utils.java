
package com.bootx.util;

import net.coobird.thumbnailator.Thumbnails;
import org.im4java.core.*;

import java.io.File;

/**
 * Utils - 图片处理
 * 
 * @author blackboy
 * @version 1.0
 */
public final class Image1Utils {

	public static void zoom(File srcFile,File destFile,Integer width,Integer height){
		try {
			Thumbnails.of(srcFile)
					.forceSize(width, height)
					.toFile(destFile);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}