package com.rongdu.loans.risk.executor.rong360;

import com.rongdu.loans.baiqishi.vo.Contact;
import com.rongdu.loans.baiqishi.vo.DeviceContactVO;
import com.rongdu.loans.loan.option.rong360Model.OrderAppendInfo;
import com.rongdu.loans.loan.option.rong360Model.PhoneList;
import com.rongdu.loans.risk.common.AutoApproveContext;
import com.rongdu.loans.risk.common.AutoApproveUtils;
import com.rongdu.loans.risk.common.Executor;
import com.rongdu.loans.risk.common.RuleIds;
import com.rongdu.loans.risk.entity.HitRule;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备通讯录手机号码较少 数据来源于：融360附加信息
 * 
 * @author sunda
 * @version 2017-08-14
 */
public class R10030004Executor extends Executor {

	@Override
	public void init() {
		super.setRuleId(RuleIds.R10030004);
	}

	@Override
	public void doExecute(AutoApproveContext context) {

		// 加载风险分析数据
		OrderAppendInfo rongAdditional = getDataInvokeService().getRongAdditional(context);
		List<PhoneList> phoneList = rongAdditional.getContacts().getPhoneList();

		// 命中的规则
		HitRule hitRule = checkContactsInfo(phoneList);
		// 决策依据
		String evidence = hitRule.getRemark();
		// 命中规则的数量
		int hitNum = getHitNum();
		if (hitNum > 0) {
			addHitRule(context, hitRule);
		}
		logger.info("执行规则-【{}-{}】-{},{},命中规则的数量：{},决策依据：{}", getRuleId(), getRuleName(), context.getUserName(),
				context.getApplyId(), hitNum, evidence);

	}

	/**
	 * 设备通讯录11位手机号码联系人且不重复的人数<20人，数据来源于：本地设备通讯录
	 * 
	 * @param list
	 * @return
	 */
	private HitRule checkContactsInfo(List<PhoneList> list) {
		HitRule hitRule = createHitRule(getRiskRule());
		if (list == null) {
			setHitNum(1);
			hitRule.setRemark("设备通讯录为空");
			return hitRule;
		}
		// 手机号码数量
		int count = 0;
		int threshold = 20;
		String mobile = null;
		List<String> mobileList = new ArrayList<String>();
		for (PhoneList item : list) {
			mobile = AutoApproveUtils.parseContactMobile(item.getPhone());
			if (AutoApproveUtils.isMobile(mobile) && !mobileList.contains(mobile)) {
				mobileList.add(mobile);
				count++;
			}
		}
		if (count < threshold) {
			setHitNum(1);
		}
		String msg = String.format("电话号码总数：%s，手机号码数量：%s", list.size(), count);
		hitRule.setRemark(msg);
		return hitRule;
	}

}
