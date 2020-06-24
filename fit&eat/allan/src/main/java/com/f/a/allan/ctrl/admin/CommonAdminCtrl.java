package com.f.a.allan.ctrl.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.enums.MediaScopeEnum;
import com.f.a.allan.enums.MediaTypeEnum;
import com.f.a.allan.utils.FileUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "admin-通用功能")
@RestController
@RequestMapping("admin")
public class CommonAdminCtrl {
	
	protected final static String SYMBOL_AND = "-";
	
	private FileUtils fileUtils;
	
	@ApiOperation("获取多媒体类型选项列表")
	@GetMapping("/mediaType")
	public ResponseEntity<Object> getMediaTypes(){
		JSONArray arr = new JSONArray();
		for (MediaTypeEnum element : MediaTypeEnum.values()) {
			JSONObject json = new JSONObject();
			json.put(element.getCode(), element.getDescription());
			arr.add(json);
		}
		return new ResponseEntity<Object>(arr, HttpStatus.OK);
	}
	@ApiOperation("获取多媒体展示范围选项列表")
	@GetMapping("/mediaScope")
	public ResponseEntity<Object> getMediaScope(){
		JSONArray arr = new JSONArray();
		for (MediaScopeEnum element : MediaScopeEnum.values()) {
			JSONObject json = new JSONObject();
			json.put(element.getCode(), element.getDescription());
			arr.add(json);
		}
		return new ResponseEntity<Object>(arr, HttpStatus.OK);
	}
	
	@PostMapping("file/upload")
	public ResponseEntity<Object> downloadFile(@RequestParam("subPath") String subPath,@RequestParam("file") MultipartFile file){
		
		fileUtils.checkFileSize(file.getSize());
		fileUtils.upload(subPath, file);
	}
}
