package com.solab.iso8583.temporal;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TestTimezones {

    private final ZoneId utc = ZoneId.of("UTC");
    private final ZoneId gmt5 = ZoneId.of("GMT-5");
    private MessageFactory<IsoMessage> mf;

    @BeforeEach
    public void init() throws IOException {
        mf = new MessageFactory<>();
        mf.setCharacterEncoding("UTF-8");
        mf.setUseDateTimeApi(true);
        mf.setConfigPath("timezones.xml");
        mf.setAssignDate(true);
    }

    @Test
    public void testParsingGuide() throws ParseException, IOException {
        String trama = "011002180000000000001231112233112233112233";
        mf.setZoneIdForParseGuide(0x110, 7, utc);
        IsoMessage m = mf.parseMessage(trama.getBytes(), 0);
        Assertions.assertTrue(m.hasEveryField(7, 12, 13));
        ZonedDateTime zdt = m.getObjectValue(7);
        Assertions.assertEquals(utc, zdt.getZone());
        OffsetTime lt = m.getObjectValue(12);
        var ref = OffsetTime.of(11, 22, 33, 0, lt.getOffset());
        Assertions.assertEquals(ref, lt);
        OffsetTime lt2 = m.getObjectValue(13);
        //Need to create again, with the offset of the other time
        ref = OffsetTime.of(11, 22, 33, 0, lt2.getOffset());
        Assertions.assertEquals(ref, lt2);
        Assertions.assertNotEquals(lt.getOffset(), lt2.getOffset());
    }

}
