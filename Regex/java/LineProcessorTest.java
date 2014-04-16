import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LineProcessorTest extends TestCase {
    private List<String> phoneLines;

    @Before
    public void setUp() throws Exception {
        phoneLines = new ArrayList<String>();
        phoneLines.add("<strong>Phone:</strong> (805) 756-2416");
        phoneLines.add("<strong>FAX:</strong> (805) 756-2956");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindEmails() throws Exception {

    }

    @Test
    public void testPhoneNO() throws Exception {
        HashSet<String> ret = new HashSet<String>();
        LineProcessor lp = new LineProcessor();
        for(String line : phoneLines) {
            ret.addAll(lp.findPhoneNumbers(line));
        }

        assertEquals(phoneLines.size(),ret.size());
        for(String phoneNO : ret) {
            System.out.println(phoneNO);
            assertEquals(12, phoneNO.length());
        }
    }
}
