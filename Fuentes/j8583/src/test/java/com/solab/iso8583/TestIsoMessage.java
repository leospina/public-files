package com.solab.iso8583;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** These are very simple tests for creating and manipulating messages.
 *
 * @author Enrique Zamudio
 */
public class TestIsoMessage {

	private MessageFactory<IsoMessage> mf;

	@BeforeEach
	public void init() throws IOException {
		mf = new MessageFactory<>();
		mf.setCharacterEncoding("UTF-8");
		mf.setCustomField(48, new CustomField48());
		mf.setConfigPath("config.xml");
	}

	/** Creates a new message and checks that it has all the fields included in the config. */
	@Test
	public void testCreation() {
		IsoMessage iso = mf.newMessage(0x200);
        Assertions.assertEquals(0x200, iso.getType());
        Assertions.assertTrue(iso.hasEveryField(3, 32, 35, 43, 48, 49, 60, 61, 100, 102));
        Assertions.assertEquals(IsoType.NUMERIC, iso.getField(3).getType());
        Assertions.assertEquals("650000", iso.getObjectValue(3));
		Assertions.assertEquals("650000", iso.objectValue(3).orElse("FAIL!"));
        Assertions.assertEquals(IsoType.LLVAR, iso.getField(32).getType());
        Assertions.assertEquals(IsoType.LLVAR, iso.getField(35).getType());
        Assertions.assertEquals(IsoType.ALPHA, iso.getField(43).getType());
        Assertions.assertEquals(40, ((String) iso.getObjectValue(43)).length());
        Assertions.assertEquals(IsoType.LLLVAR, iso.getField(48).getType());
        Assertions.assertTrue(iso.getObjectValue(48) instanceof CustomField48);
		Assertions.assertTrue(iso.objectValue(48).filter(v -> v instanceof CustomField48).isPresent());
        Assertions.assertEquals(IsoType.ALPHA, iso.getField(49).getType());
        Assertions.assertEquals(IsoType.LLLVAR, iso.getField(60).getType());
        Assertions.assertEquals(IsoType.LLLVAR, iso.getField(61).getType());
        Assertions.assertEquals(IsoType.LLVAR, iso.getField(100).getType());
        Assertions.assertEquals(IsoType.LLVAR, iso.getField(102).getType());

		Assertions.assertFalse(iso.hasAnyField(4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16));
		Assertions.assertTrue(iso.hasEveryField(3, 32, 35, 43, 48, 49, 60, 61, 100, 102));

		for (int i = 4; i < 32; i++) {
            Assertions.assertFalse(iso.hasField(i), "ISO should not contain " + i);
			Assertions.assertFalse(iso.field(i).isPresent());
			Assertions.assertFalse(iso.objectValue(i).isPresent());
		}
		for (int i = 36; i < 43; i++) {
            Assertions.assertFalse(iso.hasField(i), "ISO should not contain " + i);
			Assertions.assertFalse(iso.field(i).isPresent());
			Assertions.assertFalse(iso.objectValue(i).isPresent());
		}
		for (int i = 50; i < 60; i++) {
            Assertions.assertFalse(iso.hasField(i), "ISO should not contain " + i);
			Assertions.assertFalse(iso.field(i).isPresent());
			Assertions.assertFalse(iso.objectValue(i).isPresent());
		}
		for (int i = 62; i < 100; i++) {
            Assertions.assertFalse(iso.hasField(i), "ISO should not contain " + i);
			Assertions.assertFalse(iso.field(i).isPresent());
			Assertions.assertFalse(iso.objectValue(i).isPresent());
		}
		for (int i = 103; i < 128; i++) {
            Assertions.assertFalse(iso.hasField(i), "ISO should not contain " + i);
			Assertions.assertFalse(iso.field(i).isPresent());
			Assertions.assertFalse(iso.objectValue(i).isPresent());
		}
	}

	@Test
	public void testEncoding() throws Exception {
		IsoMessage m1 = mf.newMessage(0x200);
		byte[] buf = m1.writeData();
		IsoMessage m2 = mf.parseMessage(buf, mf.getIsoHeader(0x200).length());
        Assertions.assertEquals(m2.getType(), m1.getType());
		for (int i = 2; i < 128; i++) {
			//Either both have the field or neither have it
			if (m1.hasField(i) && m2.hasField(i)) {
				Assertions.assertEquals(m1.getField(i).getType(), m2.getField(i).getType());
                Object objectValue = m1.getObjectValue(i);
                Object actual = m2.getObjectValue(i);
                Assertions.assertEquals(objectValue, actual);
			} else {
                Assertions.assertFalse(m1.hasField(i));
                Assertions.assertFalse(m2.hasField(i));
			}
		}
	}

	/** Parses a message from a file and checks the fields. */
	@Test
	public void testParsing() throws IOException, ParseException {
		final byte[] buf = new byte[400];
		try (InputStream ins = getClass().getResourceAsStream("/parse1.txt")) {
			int pos = 0;
			while (ins.available() > 0) {
				buf[pos++] = (byte)ins.read();
			}
		}
		IsoMessage iso = mf.parseMessage(buf, mf.getIsoHeader(0x210).length());
		Assertions.assertEquals(0x210, iso.getType());
		byte[] b2 = iso.writeData();
		
		//Remove leftover newline and stuff from the original buffer
		byte[] b3 = new byte[b2.length];
		System.arraycopy(buf, 0, b3, 0, b3.length);
		Assertions.assertArrayEquals(b3, b2);

        //Test it contains the correct fields
        final List<Integer> fields = Arrays.asList(3, 4, 7, 11, 12, 13, 15, 17, 32, 35, 37, 38, 39, 41, 43, 49, 60, 61, 100, 102, 126);
        testFields(iso, fields);
        //Again, but now with forced encoding
        mf.setForceStringEncoding(true);
        iso = mf.parseMessage(buf, mf.getIsoHeader(0x210).length());
        Assertions.assertEquals(0x210, iso.getType());
        testFields(iso, fields);
	}

	@Test
	public void testTemplating() {
		IsoMessage iso1 = mf.newMessage(0x200);
		IsoMessage iso2 = mf.newMessage(0x200);
        Assertions.assertNotSame(iso1, iso2);
        Assertions.assertSame(iso1.getObjectValue(3), iso2.getObjectValue(3));
        Assertions.assertNotSame(iso1.getField(3), iso2.getField(3));
        Assertions.assertNotSame(iso1.getField(48), iso2.getField(48));
		CustomField48 cf48_1 = iso1.getObjectValue(48);
		int origv = cf48_1.getValue2();
		cf48_1.setValue2(origv + 1000);
		CustomField48 cf48_2 = iso2.getObjectValue(48);
        Assertions.assertSame(cf48_1, cf48_2);
        Assertions.assertEquals(cf48_2.getValue2(), origv + 1000);
	}

    @Test
    public void testSimpleFieldSetter() {
        IsoMessage iso = mf.newMessage(0x200);
        IsoValue<String> f3 = iso.getField(3);
        iso.updateValue(3, "999999");
        Assertions.assertEquals("999999", iso.getObjectValue(3));
        IsoValue<String> nf3 = iso.getField(3);
        Assertions.assertNotSame(f3, nf3);
        Assertions.assertEquals(f3.getType(), nf3.getType());
        Assertions.assertEquals(f3.getLength(), nf3.getLength());
        Assertions.assertSame(f3.getEncoder(), nf3.getEncoder());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> iso.updateValue(4, "INVALID!"));
    }

    private void testFields(IsoMessage m, List<Integer> fields) {
        for (int i = 2; i < 128; i++) {
            if (fields.contains(i)) {
                Assertions.assertTrue(m.hasField(i));
            } else {
                Assertions.assertFalse(m.hasField(i));
            }
			Assertions.assertEquals(fields.contains(i), m.field(i).isPresent());
        }
    }
}
