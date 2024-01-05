package com.solab.iso8583.temporal;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.util.HexCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

/** Copied from the TestBinaries in the parent package, this is just a subset
 * To make sure everything works fine with the new date/time API.
 */
public class TestBinaries {

    private MessageFactory<IsoMessage> mfactAscii = new MessageFactory<>();
    private MessageFactory<IsoMessage> mfactBin = new MessageFactory<>();

    @BeforeEach
    public void setup() throws IOException {
        mfactAscii.setCharacterEncoding("UTF-8");
        mfactAscii.setUseDateTimeApi(true);
        mfactAscii.setConfigPath("config.xml");
        mfactAscii.setAssignDate(true);
        mfactBin.setCharacterEncoding("UTF-8");
        mfactBin.setUseDateTimeApi(true);
        mfactBin.setConfigPath("config.xml");
        mfactBin.setAssignDate(true);
        mfactBin.setUseBinaryMessages(true);
    }

    void testParsed(IsoMessage m) {
        Assertions.assertEquals(m.getType(), 0x600);
        Assertions.assertEquals(new BigDecimal("1234.00"), m.getObjectValue(4));
        Assertions.assertTrue(m.hasField(7), "No field 7!");
    }

    @Test
    public void testMessages() throws ParseException, UnsupportedEncodingException {
        //Create a message with both factories
        IsoMessage ascii = mfactAscii.newMessage(0x600);
        IsoMessage bin = mfactBin.newMessage(0x600);
        Assertions.assertFalse(ascii.isBinaryHeader() || ascii.isBinaryFields() || ascii.isBinaryBitmap());
        Assertions.assertTrue(bin.isBinaryHeader() && bin.isBinaryFields());
        //HEXencode the binary message, headers should be similar to the ASCII version
        final byte[] _v = bin.writeData();
        String hexBin = HexCodec.hexEncode(_v, 0, _v.length);
        String hexAscii = new String(ascii.writeData()).toUpperCase();
        Assertions.assertEquals("0600", hexBin.substring(0, 4));
        //Should be the same up to the field 42 (first 80 chars)
        Assertions.assertEquals(hexAscii.substring(0, 88), hexBin.substring(0, 88));
        Assertions.assertEquals(ascii.getObjectValue(43), new String(_v, 44, 40).trim());
        //Parse both messages
        byte[] asciiBuf = ascii.writeData();
        IsoMessage ascii2 = mfactAscii.parseMessage(asciiBuf, 0);
        testParsed(ascii2);
        ZonedDateTime t = ascii.getObjectValue(7);
        Assertions.assertEquals(t.withNano(0), ascii2.getObjectValue(7));
        IsoMessage bin2 = mfactBin.parseMessage(bin.writeData(), 0);
        //Compare values, should be the same
        testParsed(bin2);
        t = bin.getObjectValue(7);
        Assertions.assertEquals(t.withNano(0), bin2.getObjectValue(7));
        //Test the debug string
        ascii.setValue(60, "XXX", IsoType.LLVAR, 0);
        bin.setValue(60, "XXX", IsoType.LLVAR, 0);
        Assertions.assertEquals(ascii.debugString(), bin.debugString(), "Debug strings differ");
        Assertions.assertTrue(ascii.debugString().contains("03XXX"), "LLVAR fields wrong");
    }

    @Test
    public void testTimeFields() throws ParseException, UnsupportedEncodingException {
        OffsetTime t = OffsetTime.now();
        LocalDate d = LocalDate.now();
        IsoMessage iso1 = mfactAscii.newMessage(0x210);
        iso1.setValue(12, t, IsoType.TIME, 0);
        iso1.setValue(13, d, IsoType.DATE4, 0);
        iso1.setValue(17, d, IsoType.DATE_EXP, 0);
        byte[] data1 = iso1.writeData();
        iso1 = mfactAscii.parseMessage(data1, mfactAscii.getIsoHeader(0x210).length());
        Assertions.assertEquals(t.withNano(0), iso1.getObjectValue(12));
        Assertions.assertEquals(d, iso1.getObjectValue(13));
        Assertions.assertEquals(d.withDayOfMonth(1), iso1.getObjectValue(17));
    }
}
