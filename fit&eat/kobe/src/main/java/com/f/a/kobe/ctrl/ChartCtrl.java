package com.f.a.kobe.ctrl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.pojo.bo.DateSelection;
import com.f.a.kobe.pojo.response.chart.LineChart;
import com.f.a.kobe.pojo.response.chart.WeightChart;
import com.f.a.kobe.service.CustomerBodyInfoService;
import com.f.a.kobe.util.DateUtils;
import com.f.a.kobe.view.UserAgent;

@RestController
@RequestMapping("/customer")
public class ChartCtrl {

	@Autowired
	private CustomerBodyInfoService customerBodyInfoService;
	
	@GetMapping("/weightChart/{seletcion}")
	public ResponseEntity<Object> weightChart(@PathVariable("seletcion") int seletcion,UserAgent userAgent){
		DateSelection selection = DateUtils.getDateSelection(seletcion);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<WeightChart> list = customerBodyInfoService.getWeightChart(userAgent.getCustomerId(),
				sdf.format(selection.getStratDate()),
				sdf.format(selection.getEndDate()));
		return new ResponseEntity<Object>(list,HttpStatus.OK);
	}
	
	@GetMapping("/lineChart/{seletcion}")
	public ResponseEntity<Object> lineChart(@PathVariable("seletcion") int seletcion,UserAgent userAgent){
		DateSelection selection = DateUtils.getDateSelection(seletcion);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<LineChart> list = customerBodyInfoService.getLineChart(userAgent.getCustomerId(),
				sdf.format(selection.getStratDate()),
				sdf.format(selection.getEndDate()));
		return new ResponseEntity<Object>(list,HttpStatus.OK);
	}
	
	
	
}
