package com.f.a.allan.utils;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
/**
 * 图片上传工具类
 * 
 * @author micezhao
 *
 */
public class FileUploadUtils {

	private static final String IMG_ROOT_PAHT = "img";

	public String ImgUpload(String subPath, MultipartFile file) {
		File targetFile = null;
		String fileName = file.getOriginalFilename();
		File file1 = new File(IMG_ROOT_PAHT + subPath + "/" + fileName);
		if (!file1.exists() && !file1.isDirectory()) {
			file1.mkdir();
		}
		// 将图片存入文件夹
		targetFile = new File(file1, fileName);
		try {
			// 将上传的文件写到服务器上指定的文件。
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetFile.getAbsolutePath();
	}
	
	public String ImgUploadTest(int i) {
		if(i == 0) { // 商品图片
			return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589519819710&di=81436855f976957575f9511f464327ff&imgtype=0&src=http%3A%2F%2Fdingyue.ws.126.net%2F2020%2F0417%2F074fc762j00q8w9kt0028d200sm00xcg00gg00j5.jpg";
		}else if(i == 1 ){
			return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589520111591&di=80d1134950f56a84c25fa1e5065079b9&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170206%2Fe4c8e8d7a64b4fb6a0f6299931baec76_th.jpg";
		}else {
			return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589520166511&di=d5dff905d5b24ebb9ecaae002cc658e3&imgtype=0&src=http%3A%2F%2Fsearchfoto.ru%2Fimg%2FxyygpKbDS1y8pTjXUy83VS8rMS9fLSy3RL8mwz0yx9fcM0EtJ0S2PyCnOy8sOSTMOqQq38I23NHf0SHTNV0vMLbAutzUyNgCzMmwNzSGsomJbQzCjIDnHNgUMwNx8W1OIMNBoQz1DAA.jpg";
		}
		
		
	}

}
