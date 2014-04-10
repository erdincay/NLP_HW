import java.lang.String;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineProcessor {

    private static final String email_regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    //private static final String email_regex = "[_A-Za-z0-9-]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})";
    private static final String phone_regex = "^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$";
    private static final String number_regex = "\\d+";
    private Pattern email_pattern;
    private Pattern phone_pattern;
    private Pattern number_pattern;

    public LineProcessor() {
        // Constructor
        email_pattern = Pattern.compile(email_regex);
        phone_pattern = Pattern.compile(phone_regex);
        number_pattern = Pattern.compile(number_regex);
    }

    private HashSet<String> findEmails(String line) {

        HashSet<String> ret = new HashSet<String>();
        Matcher matcher = email_pattern.matcher(line);
        while (matcher.find()) {
            ret.add(matcher.group());
        }
        return ret;
    }

    private String formatPhoneNO(String phone_number) {

        String ret = "";
        String number = "";

        Matcher matcher = number_pattern.matcher(phone_number);
        while (matcher.find()) {
            number += matcher.group();
        }

        if (number.length() == 10) {
            ret = number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6);
        } else if (number.length() == 11) {
            ret = number.substring(1, 4) + "-" + number.substring(4, 7) + "-" + number.substring(7);
        }

        return ret;
    }

    private HashSet<String> findPhoneNumbers(final String line) {

        HashSet<String> ret = new HashSet<String>();
        Matcher matcher = phone_pattern.matcher(line);
        while (matcher.find()) {
            String formatNO = formatPhoneNO(matcher.group());
            if (formatNO != null && !formatNO.isEmpty()) {
                ret.add(formatNO);
            }
        }
        return ret;
    }

    public HashSet<String> processLine(String line) {
        // You should not be modifying this method.
        HashSet<String> email = findEmails(line);
        HashSet<String> phone = findPhoneNumbers(line);
        HashSet<String> email_n_phones = new HashSet<String>();
        for (String e : email) {
            email_n_phones.add("e\t" + e);
        }
        for (String p : phone) {
            email_n_phones.add("p\t" + p);
        }
        return email_n_phones;
    }
}
