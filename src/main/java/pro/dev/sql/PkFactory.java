package pro.dev.sql;

public class PkFactory {
	private static long currentPk = System.currentTimeMillis();
	public static long getPk() {
		return currentPk++;
	}
}
