package pro.dev.sql;

import java.util.Random;

public class RangeValueGetter extends ValueGetter{
	private long begin;
	private long end;
	@Override
	public Object get() {
		Random random = new Random();
		random.nextLong();
		return super.get();
	}
}
