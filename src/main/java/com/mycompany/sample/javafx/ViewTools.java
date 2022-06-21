package com.mycompany.sample.javafx;

public class ViewTools {
	private static final String VIEW_NAME = "gls-view-name";

	public ViewTools() {
	}

	public static void storeViewName(View view, String name) {
		view.getProperties().put("gls-view-name", name);
	}

	public static String findViewName(View view) {
		return view != null && view.getProperties().get("gls-view-name") != null ? String.valueOf(view.getProperties().get("gls-view-name")) : null;
	}
}
