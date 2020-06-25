package com.f.a.allan.utils;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
/**
 * 图片上传工具类
 * 
 * @author micezhao
 *
 */
@Slf4j
public class FileUtils {

	 enum FileTypePathEnum {
		TYPE_IMP("img","/img/"),
		TYPE_GIF("git","/git/"),
		TYPE_VIDEO("video","/video/");
		
		private String type;
		
		private String relativePath;

		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getRelativePath() {
			return relativePath;
		}
		public void setRelativePath(String relativePath) {
			this.relativePath = relativePath;
		}
		private FileTypePathEnum(String type, String relativePath) {
			this.type = type;
			this.relativePath = relativePath;
		}
		public static FileTypePathEnum getPathByType(String fileType) {
			if(StringUtils.isBlank(fileType)) {
				return null;
			}else {
				for (FileTypePathEnum element : FileTypePathEnum.values()) {
					if(StringUtils.equals(fileType, element.type)) {
						return element;
					}
				}
			}
			return null;
		}
	};
	
	@Value("${storage.file.path}")
	private String storagePath;
	
	@Value("${storage.file.max_size}")
	private String max_size;
	
	private static final String [] FILE_CLASS_IMG = {".jpg",".png",".svg"};
	
	private static final String [] FILE_CLASS_GIF = {".gif"};
	
	private static final String [] FILE_CLASS_VIDEO = {".mp4",".avi",".flv",".mkv"};
	
	public String upload(String subPath, MultipartFile file) {
		File targetFile = null;
		String fileName = file.getOriginalFilename();
		String rootPath = getRootPath(fileName);
		File file1 = new File(rootPath + subPath + "/" + fileName);
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
	
	public void checkFileSize(long fileSize) {
		String maxSize = max_size.substring(0, max_size.length()-1);
		long limitSize = new BigDecimal(maxSize).multiply(new BigDecimal(1024)).longValue();
		if(fileSize > limitSize ) {
			throw new RuntimeException("对不起,您上传的文件不得超过"+max_size);
		}
		return;
	}
	
	public void checkAllowFile(String fileClass) {
		 String [] fileClassArr = (String[]) ArrayUtils.addAll(FILE_CLASS_IMG, FILE_CLASS_GIF,FILE_CLASS_VIDEO);
		 boolean contains = ArrayUtils.contains(fileClassArr, fileClass);
		 if(!contains) {
			 throw new RuntimeException("对不起,系统不支持当前文件类型");
		 }
		 return;
		
	}
	
	private String getRootPath(String fileName) {
		int begin = fileName.indexOf(".");
        int last = fileName.length();
		String fileClass = fileName.substring(begin, last);
		checkAllowFile(fileClass);
		String fileType = "";
		if(ArrayUtils.contains(FILE_CLASS_IMG, fileClass)) {
			fileType = "img";
		}else if(ArrayUtils.contains(FILE_CLASS_GIF, fileClass)) {
			fileType = "gif";
		}
		else if(ArrayUtils.contains(FILE_CLASS_VIDEO, fileClass)) {
			fileType = "video";
		}
		String path  = FileTypePathEnum.getPathByType(fileType).getRelativePath();
		String rootPath = storagePath+path;
		return rootPath;
	}

}
