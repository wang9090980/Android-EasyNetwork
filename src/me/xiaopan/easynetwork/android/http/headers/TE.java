package me.xiaopan.easynetwork.android.http.headers;

/**
 * 接受的文件类型
 */
public class TE extends HttpHeader {
	/**
	 * 名字
	 */
	public static final String NAME = "Accept";
	/**
	 * 值
	 */
	private String value;
	
	public TE(String value) {
		setValue(value);
	}
	
	public TE() {
		setValue("text/html, application/xml;q=0.9, application/xhtml xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
}