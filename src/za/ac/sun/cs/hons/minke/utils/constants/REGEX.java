package za.ac.sun.cs.hons.minke.utils.constants;

import java.util.regex.Pattern;

public class REGEX {
	public static final Pattern DECIMALS_0 = Pattern.compile("([1-9][0-9]*)+(\\.[0-9]{1,2}+)?");
	public static final Pattern DECIMALS_1 = Pattern.compile("[0-9]+(\\.[0-9][1-9])");
	public static final Pattern DECIMALS_2 = Pattern.compile("[0-9]+(\\.[1-9][0-9]?)");
	public static final Pattern INTS = Pattern.compile("([1-9][0-9]*)");
	public static final Pattern STRING = Pattern.compile("[a-zA-ZäöüßÄÖÜ\\s'\\-,]+");
}
