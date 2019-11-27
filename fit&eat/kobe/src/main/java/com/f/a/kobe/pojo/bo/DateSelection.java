package com.f.a.kobe.pojo.bo;

import java.util.Date;

public class DateSelection {
	private Date stratDate;

	private Date endDate;

	public Date getStratDate() {
		return stratDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setStratDate(Date stratDate) {
		this.stratDate = stratDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public DateSelection(Date stratDate, Date endDate) {
		super();
		this.stratDate = stratDate;
		this.endDate = endDate;
	}
}
