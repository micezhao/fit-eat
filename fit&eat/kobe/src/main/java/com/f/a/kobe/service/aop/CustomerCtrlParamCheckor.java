package com.f.a.kobe.service.aop;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.util.CombinedParam;
import com.f.a.kobe.util.CombinedParamBuilderTest;
import com.f.a.kobe.util.CombinedParamCheckUtil;
import com.f.a.kobe.util.CombinedParamCheckor;
import com.f.a.kobe.util.ObjectTransUtils;

@Component("customerCtrlParamCheckor")
public class CustomerCtrlParamCheckor implements ParamCheckHandler {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> commonCheck(Object obj, String value) {
		try {
			Map<String, String> invoke = (Map<String, String>) this.getClass().getDeclaredMethod(value, Object.class)
					.invoke(this, obj);
			return invoke;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Map<String, String> addAddr(Object obj) {
		ParamRequest paramRequest = (ParamRequest) obj;
		// 要求不为空判断
		CombinedParamCheckUtil.checkEmpty(paramRequest.getConnectorName(), "ConnectorName", "联系人不能为空");
		CombinedParamCheckUtil.checkEmpty(paramRequest.getProvinceNo(), "ProvinceNo", "省号不能为空");
		CombinedParamCheckUtil.checkEmpty(paramRequest.getCityNo(), "CityNo", "市号不能为空");
		CombinedParamCheckUtil.checkEmpty(paramRequest.getDistrcNo(), "DistrictNo", "区号不能为空");
		Map<String, String> checkEmpty = CombinedParamCheckUtil.checkEmpty(paramRequest.getAddrDetail(), "AddrDetail",
				"详细地址区号不能为空");
		if (checkEmpty != null) {
			return checkEmpty;
		}

		CombinedParam combinedParam = new CombinedParam();
		ObjectTransUtils.copy(combinedParam, paramRequest);
		// 合法性判断
		CombinedParamCheckUtil checkor = new CombinedParamCheckUtil();
		CombinedParamCheckor combinedParamCheckor = new CombinedParamBuilderTest().setProvinceNo(paramRequest.getProvinceNo()).setCityNo(paramRequest.getCityNo())
				.setDistrictNo(paramRequest.getDistrcNo()).setStreetNo(paramRequest.getStreetNo())
				.setRealName(paramRequest.getConnectorName()).setMobile(paramRequest.getConnectorMobile()).build();
		checkor.setCombinedParamCheckor(combinedParamCheckor);
		try {
			Map<String, String> checkResult = checkor.check2();
			if (checkResult != null) {
				return checkResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Map<String, String> updateCustomerBaseInfo(Object obj) {
		ParamRequest paramRequest = (ParamRequest) obj;
		// 要求不为空判断

		CombinedParam combinedParam = new CombinedParam();
		ObjectTransUtils.copy(combinedParam, paramRequest);
		// 合法性判断
		CombinedParamCheckUtil checkor = new CombinedParamCheckUtil();
		checkor.setCombinedParam(combinedParam);
		CombinedParamCheckor combinedParamCheckor = new CombinedParamBuilderTest()
				.setRealName(paramRequest.getRealname()).setBirthday(paramRequest.getBirthday())
				.setGender(paramRequest.getGender()).setNickName(paramRequest.getNickname())
				.setWebUrl(paramRequest.getHeadimg()).build();
		checkor.setCombinedParamCheckor(combinedParamCheckor);
		try {
			Map<String, String> checkResult = checkor.check();
			if (checkResult != null) {
				return checkResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Map<String, String> test(Object obj) {
		ParamRequest paramRequest = (ParamRequest) obj;
		CombinedParamCheckUtil checkor = new CombinedParamCheckUtil();
		CombinedParamCheckor combinedParamCheckor = new CombinedParamBuilderTest()
				.setMobileArrays(paramRequest.getMobile(), paramRequest.getConnectorMobile()).build();
		checkor.setCombinedParamCheckor(combinedParamCheckor);
		try {
			Map<String, String> checkResult = checkor.check2();
			if (checkResult != null) {
				return checkResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> bodyinfo(Object obj) {
		ParamRequest paramRequest = (ParamRequest) obj;
		CombinedParamCheckUtil checkor = new CombinedParamCheckUtil();
		CombinedParamCheckor combinedParamCheckor = new CombinedParamBuilderTest()
				.setFloatArrays(paramRequest.getHeight(), paramRequest.getWeight(), paramRequest.getWaistline(),
						paramRequest.getHipline())
				.build();
		checkor.setCombinedParamCheckor(combinedParamCheckor);
		try {
			Map<String, String> checkResult = checkor.check2();
			if (checkResult != null) {
				return checkResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
