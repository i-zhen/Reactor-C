import java.io.IOException;
import java.io.PushbackInputStream;

public class IO {

	public static PushbackInputStream stdin = new PushbackInputStream(System.in);
 	private static int IOERROR_CODE = 90;
	private static int NUMERROR_CODE = 95;
	
	public static char read_c() {
		char c = '\0';
		try {
			int b = stdin.read();
			c = (char)b;
			if (c == '\r') c = read_c();
		} catch (IOException e) {
			System.err.println("error: " + e.getMessage());
			System.exit(IOERROR_CODE);
		}
		return c;
	}
	
	public static int read_i() {
		int i = 0;
		StringBuffer sb = new StringBuffer();
		try {			
			int b = stdin.read();
			if (Character.compare((char)b, '-') == 0) { 
				sb.append((char)b);		
			} else {
				stdin.unread(b);
			}
			while (true) {
				b = stdin.read();
				if (Character.isDigit((char)b))
					sb.append((char)b);
				else {
					stdin.unread(b);
					break;
				}
			}
			i = Integer.parseInt(sb.toString());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(IOERROR_CODE);
		} catch (NumberFormatException e) {
			System.err.println("error: Expected 32-bit integer, but got: " + (sb.toString() == null || sb.toString().compareTo("") == 0 ? "(empty)" : sb.toString()));
			System.exit(NUMERROR_CODE);
		}
		return i;
	}

	public static void print_c(char c) {
		print_s(Character.toString(c));
	}
	
	public static void print_i(int i) {
		print_s(Integer.toString(i));
	}
	
	public static void print_s(String s) {
		System.out.print(s);
	}
}
