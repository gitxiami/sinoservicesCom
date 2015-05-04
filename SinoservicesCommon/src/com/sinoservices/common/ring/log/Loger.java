package com.sinoservices.common.ring.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

import com.sinoservices.common.ring.global.Constants;
import com.sinoservices.common.ring.global.Utils;

import android.util.Log;

public class Loger {

	private final static boolean sIsLoggerEnable = true;
	private final static String tag = "[HycoDemo]";
	public static int logLevel = Log.VERBOSE;
	private static Hashtable<String, Loger> sLoggerTable;
	private PrintWriter pw = null;

	private static boolean logFlag = true;
	private static boolean logWriteToFile = true;

	static {
		sLoggerTable = new Hashtable<String, Loger>();
	}

	private String mClassName;

	public static Loger getLogger(String className) {
		Loger classLogger = (Loger) sLoggerTable.get(className);
		if (classLogger == null) {
			classLogger = new Loger(className);
			sLoggerTable.put(className, classLogger);
		}
		return classLogger;
	}

	private Loger(String name) {
		mClassName = name;
	}

	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();

		if (sts == null) {
			return null;

		}

		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}

			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}

			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}

			return "[ " + Thread.currentThread().getName() + ": "
					+ st.getFileName() + ":" + st.getLineNumber() + " ]";
		}

		return null;
	}

	public void info(Object str) {
		if (!logFlag)
			return;
		if (logWriteToFile) {
			writeLogToFile(str);
		}
		if (logLevel <= Log.INFO) {
			String name = getFunctionName();
			if (name != null) {
				Log.i(tag, name + " - " + str);
			} else {
				Log.i(tag, str.toString());
			}
		}
	}

	public void i(Object str) {
		if (!logFlag)
			return;
		if (logWriteToFile) {
			writeLogToFile(str);
		}
		info(str);
	}

	public void verbose(Object str) {
		if (!logFlag)
			return;
		if (logLevel <= Log.VERBOSE) {
			String name = getFunctionName();
			if (name != null) {
				Log.v(tag, name + " - " + str);
			} else {
				Log.v(tag, str.toString());
			}

		}
	}

	public void v(Object str) {
		if (!logFlag)
			return;
		if (logWriteToFile) {
			writeLogToFile(str);
		}
		verbose(str);
	}

	public void warn(Object str) {
		if (!logFlag)
			return;
		if (logLevel <= Log.WARN) {
			String name = getFunctionName();

			if (name != null) {
				Log.w(tag, name + " - " + str);
			} else {
				Log.w(tag, str.toString());
			}

		}
	}

	public void w(Object str) {
		if (!logFlag)
			return;
		if (logWriteToFile) {
			writeLogToFile(str);
		}
		warn(str);
	}

	public void error(Object str) {
		if (!logFlag)
			return;
		if (logLevel <= Log.ERROR) {

			String name = getFunctionName();
			if (name != null) {
				Log.e(tag, name + " - " + str);
			} else {
				Log.e(tag, str.toString());
			}
		}
	}

	public void error(Exception ex) {
		if (!logFlag)
			return;
		if (logLevel <= Log.ERROR) {
			Log.e(tag, "error", ex);
		}
	}

	public void e(Object str) {
		if (!logFlag)
			return;
		if (logWriteToFile) {
			writeLogToFile(str);
		}
		error(str);
	}

	public void e(Exception ex) {
		if (!logFlag)
			return;
		if (logWriteToFile) {
			writeLogToFile(ex);
		}
		error(ex);
	}

	public void e(String log, Throwable tr) {
		if (!logFlag)
			return;
		String line = getFunctionName();
		if (sIsLoggerEnable) {
			Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"
					+ "[" + mClassName + line + ":] " + log + "\n", tr);
			if (logWriteToFile) {
				writeLogToFile("{Thread:" + Thread.currentThread().getName()
						+ "}" + "[" + mClassName + line + ":] " + log + "\n"
						+ Log.getStackTraceString(tr));
			}
		}
	}

	public void debug(Object str) {
		if (!logFlag)
			return;
		if (logLevel <= Log.DEBUG) {
			String name = getFunctionName();
			if (name != null) {
				Log.d(tag, name + " - " + str);
			} else {
				Log.d(tag, str.toString());
			}

		}
	}

	public void d(Object str) {
		if (!logFlag)
			return;
		if (logWriteToFile) {
			writeLogToFile(str);
		}
		debug(str);
	}

	private void writeLogToFile(Object str) {
		File fil = new File(Constants.LOG_DIR + File.separator + "log"
				+ Utils.getFormatDate("yyyyMMdd") + ".txt");
		if (!fil.exists()) {
			try {
				fil.createNewFile();
			} catch (Exception e) {

			}
		}
		try {
			pw = new PrintWriter(new FileOutputStream(fil, true));
		} catch (Exception e) {
		}
		if (pw != null) {
			pw.print(str + "\r\n");
			pw.flush();
		}
	}

}
