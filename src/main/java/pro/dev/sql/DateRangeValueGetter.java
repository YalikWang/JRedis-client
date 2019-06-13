package pro.dev.sql;

import java.util.Date;
import java.util.Random;

public class DateRangeValueGetter extends ValueGetter{
	
	private long begin;
	
	private int days;//开始日期与结束日期相差天数
	
	
	public DateRangeValueGetter(Date beginDate,Date endDate) {
		this.begin = beginDate.getTime();
		days = (int) ((beginDate.getTime()-endDate.getTime())/(1000*3600*24));
	}
	
	@Override
	public Object get() {
		Random random = new Random();
		int nextInt = random.nextInt(days);
		long mils = 1000*3600*24*nextInt;
		return new Date(begin+mils);
	}

}
