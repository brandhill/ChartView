package com.michaelpardo.java.text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadSafeSimpleDateFormat {
	private DateFormat mDateFormat;

	public ThreadSafeSimpleDateFormat(String format) {
		mDateFormat = new SimpleDateFormat(format);
	}

	public synchronized String format(Date date) {
		return mDateFormat.format(date);
	}

	public synchronized Date parse(String string) throws ParseException {
		return mDateFormat.parse(string);
	}
}