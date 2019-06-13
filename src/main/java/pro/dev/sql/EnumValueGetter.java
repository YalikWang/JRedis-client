package pro.dev.sql;

import java.util.List;
import java.util.Random;

public class EnumValueGetter extends ValueGetter{
	
	private List<Object> list ;
	
	@Override
	public Object get() {
		Random random = new Random();
		int nextInt = random.nextInt(list.size());
		return list.get(nextInt);
	}
}
