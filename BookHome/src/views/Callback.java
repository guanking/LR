package views;

public interface Callback {
	void setValue(int value);

	void setMaxValue(int value);

	void setState(String msg);

	void setError(String error);

	void addValue();
}
