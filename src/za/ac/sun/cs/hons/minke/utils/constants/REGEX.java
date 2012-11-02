package za.ac.sun.cs.hons.minke.utils.constants;

public class REGEX {
	public static final String DECIMALS_0 = "([1-9][0-9]*)+(\\.[0-9]{1,2}+)?";
	public static final String DECIMALS_1 = "[0-9]+(\\.[0-9][1-9])";
	public static final String DECIMALS_2 = "[0-9]+(\\.[1-9][0-9]?)";
	public static final String INTS = "([1-9][0-9]*)";
	public static final String STRING = "[a-zA-ZäöüßÄÖÜ\\s'-,]+";
}
