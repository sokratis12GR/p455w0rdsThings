package p455w0rd.p455w0rdsthings.lib.tool;

public class Main {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		try {
			Class<?> c_toolMain = new StripClassLoader().loadClass("p455w0rd.p455w0rdsthings.lib.tool.ToolMain");
			c_toolMain.getDeclaredMethod("main", new Class[] {
					String[].class
			}).invoke(null, new Object[] {
					args
			});
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
