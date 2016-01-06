package com.anacleto.util;

import java.util.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import java.io.*;
import java.util.LinkedList;
import java.lang.StringBuffer;
import java.util.regex.*;

public class Pager extends TagSupport {

	static final long serialVersionUID = 1L;

	/* total number of items */
	private int size;

	/* the first element of the set */
	private int offset;

	/* length of the set */
	private int length;

	private String url;

	private String separator;

	public void setSize(int s) {
		size = s;
	}

	public int getSize() {
		return size;
	}

	public void setOffset(int s) {
		offset = s;
	}

	public int getOffset() {
		return offset;
	}

	public void setLength(int s) {
		length = s;
	}

	public int getLength() {
		return length;
	}

	public void setUrl(String s) {
		url = s;
	}

	public String getUrl() {
		return url;
	}

	public void setSeparator(String s) {
		separator = s;
	}

	public String getSeparator() {
		return separator;
	}

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.print(getPager());
		} catch (IOException ioe) {
			System.out.println("Error writing to out: " + ioe);
		}
		return (SKIP_BODY);
	}

	public String getPager() {
		
		if(size == 0) {
			return "";
		}

		if (offset < 0) {
			offset = 0;
		}
		if (offset > size) {
			offset = size;
		}
		
		double d = size;
		int stepSize = (int) Math.log10(d) + 1;
		int steps[] = new int[stepSize];
		steps[0] = length;
		for (int i = 1; i < stepSize; i++) {
			steps[i] = (int) Math.pow(10, i) * length;
		}

		LinkedList numbersList = new LinkedList();

		numbersList.add(new Integer(offset));
		for (int j = 0; j < stepSize; j++) {
			for (int i = 1; i <= 3; i++) {
				int curr = steps[j] * i;
				if (offset - curr > 1)
					numbersList.addFirst(new Integer(offset - curr));
				if (offset + curr < size)
					numbersList.add(new Integer(offset + curr));
			}
		}

		if (offset > 0) {
			numbersList.addFirst(new Integer(0));
		}
		int lastInList = Integer.parseInt(numbersList.getLast().toString());
		/* lastEl = 2617 -> lastPage = 2601 */
		int lastPage = size - (size % length); 
		if (lastInList < lastPage) {
			numbersList.add(new Integer(lastPage));
		}

		String link;
		if(url != null){
			url = url.replaceAll("(^|\\?)(?:next|prev)Res\\.(?:x|y)=[0-9]+", "$1");
			url = url.replaceAll("&(?:next|prev)Res\\.(?:x|y)=[0-9]+", "");
			Pattern pattern = Pattern.compile("(^|[\\?&])offset=\\d*");
			Matcher matcher = pattern.matcher(url);
			if (matcher.find()) {
				url = matcher.replaceAll("");
			}
			link = "?" + url + "&offset=";
		}
		else {
			link = "?offset=";
		}
		link = link.replaceAll("&", "&amp;");

		StringBuffer bar = new StringBuffer();
		Iterator it = numbersList.iterator();
		int curr;
		while (it.hasNext()) {
			if (!bar.equals(""))
				bar.append(separator);
			curr = Integer.parseInt(it.next().toString());
			if (offset == curr) {
				bar.append("<b>" + (curr + 1));
				if (curr < size)
					bar.append("-");
				bar.append("</b>");
			} else {
				bar.append("<a href=\"" + link + curr + "\">" + (curr + 1));
				if (curr < size)
					bar.append("-");
				bar.append("</a>");
			}
		}

		return bar.toString();
	}
}
