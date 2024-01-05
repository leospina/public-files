/*
 * j8583 A Java implementation of the ISO8583 protocol
 * Copyright (C) 2022 Enrique Zamudio Lopez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.solab.iso8583.parse;

import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.parse.temporal.Date10ParseInfo;
import com.solab.iso8583.parse.temporal.Date12ParseInfo;
import com.solab.iso8583.parse.temporal.Date14ParseInfo;
import com.solab.iso8583.parse.temporal.Date4ParseInfo;
import com.solab.iso8583.parse.temporal.Date6ParseInfo;
import com.solab.iso8583.parse.temporal.DateExpParseInfo;
import com.solab.iso8583.parse.temporal.TimeParseInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

public class TestTemporals {

    @ParameterizedTest
    @ValueSource(ints = { 1, -1 })
    public void testDate4(int days) throws ParseException, IOException {
        LocalDate when = LocalDate.now().plusDays(days);
        byte[] buf = IsoType.DATE4.format(when).getBytes();
        IsoValue<LocalDate> comp = new Date4ParseInfo().parse(0, buf, 0, null);
        Assertions.assertEquals(comp.getValue(), when);
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<LocalDate> bin = new Date4ParseInfo().parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(comp.getValue(), bin.getValue());
    }

    @Test
    public void testDate6() throws ParseException, IOException {
        LocalDate nye = LocalDate.of(2017, 12, 31);
        IsoValue<LocalDate> f = new IsoValue<>(IsoType.DATE6, nye);
        Assertions.assertEquals("171231", f.toString());
        Date6ParseInfo parser = new Date6ParseInfo();
        IsoValue<LocalDate> parsed = parser.parse(1, "171231".getBytes(), 0, null);
        Assertions.assertEquals("171231", parsed.toString());
        Assertions.assertEquals(f.getValue(), parsed.getValue());
        byte[] buf = new byte[3];
        buf[0] = 0x17;
        buf[1] = 0x12;
        buf[2] = 0x31;
        parsed = parser.parseBinary(2, buf, 0, null);
        Assertions.assertEquals("171231", parsed.toString());
        Assertions.assertEquals(nye, parsed.getValue());

        //now one in the 80's
        nye = LocalDate.of(1984, 12, 31);
        f = new IsoValue<>(IsoType.DATE6, nye);
        Assertions.assertEquals("841231", f.toString());
        parsed = parser.parse(1, "841231".getBytes(), 0, null);
        Assertions.assertEquals("841231", parsed.toString());
        Assertions.assertEquals(f.getValue(), parsed.getValue());
        buf = new byte[3];
        buf[0] = (byte)0x84;
        buf[1] = 0x12;
        buf[2] = 0x31;
        parsed = parser.parseBinary(2, buf, 0, null);
        Assertions.assertEquals("841231", parsed.toString());
        Assertions.assertEquals(nye, parsed.getValue());

        //again but with string decoding
        parser.setForceStringDecoding(true);
        nye = LocalDate.of(1984, 12, 31);
        f = new IsoValue<>(IsoType.DATE6, nye);
        Assertions.assertEquals("841231", f.toString());
        parsed = parser.parse(1, "841231".getBytes(), 0, null);
        Assertions.assertEquals("841231", parsed.toString());
        Assertions.assertEquals(f.getValue(), parsed.getValue());
        buf = new byte[3];
        buf[0] = (byte)0x84;
        buf[1] = 0x12;
        buf[2] = 0x31;
        parsed = parser.parseBinary(2, buf, 0, null);
        Assertions.assertEquals("841231", parsed.toString());
        Assertions.assertEquals(nye, parsed.getValue());
    }

    @Test
    public void testDate10() throws ParseException, IOException {
        ZonedDateTime when = ZonedDateTime.now().plusDays(2).withNano(0);
        byte[] buf = IsoType.DATE10.format(when).getBytes();
        IsoValue<ZonedDateTime> comp = new Date10ParseInfo().parse(0, buf, 0, null);
        Assertions.assertEquals(when.minusYears(1), comp.getValue());
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<ZonedDateTime> bin = new Date10ParseInfo().parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(when.minusYears(1), bin.getValue());

        when = when.minusDays(4);
        buf = IsoType.DATE10.format(when).getBytes();
        comp = new Date10ParseInfo().parse(0, buf, 0, null);
        Assertions.assertEquals(when, comp.getValue());
        //Now with the binary
        bout.reset();
        comp.write(bout, true, false);
        bin = new Date10ParseInfo().parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(when, bin.getValue());
    }

    @Test
    public void testDate10FutureTolerance() throws ParseException, IOException {
        ZonedDateTime soon = ZonedDateTime.now().plusSeconds(50).withNano(0);
        byte[] buf = IsoType.DATE10.format(soon).getBytes();
        IsoValue<ZonedDateTime> comp = new Date10ParseInfo().parse(0, buf, 0, null);
        Assertions.assertTrue(comp.getValue().isAfter(ZonedDateTime.now()));
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<ZonedDateTime> bin = new Date10ParseInfo().parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(soon, bin.getValue());
    }

    @Test
    public void testDate12FutureTolerance() throws ParseException, IOException {
        ZonedDateTime soon = ZonedDateTime.now().plusSeconds(50).withNano(0);
        byte[] buf = IsoType.DATE12.format(soon).getBytes();
        IsoValue<ZonedDateTime> comp = new Date12ParseInfo().parse(0, buf, 0, null);
        Assertions.assertTrue(comp.getValue().isAfter(ZonedDateTime.now()));
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<ZonedDateTime> bin = new Date12ParseInfo().parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(soon, bin.getValue());
    }

    @Test
    public void testDateExp() throws ParseException, IOException {
        DateExpParseInfo parser = new DateExpParseInfo();
        LocalDate soon = LocalDate.now().plusMonths(6);
        byte[] buf = IsoType.DATE_EXP.format(soon).getBytes();
        IsoValue<LocalDate> comp = parser.parse(1, buf, 0, null);
        Assertions.assertTrue(comp.getValue().isAfter(LocalDate.now()));
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<LocalDate> bin = parser.parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(soon.getYear(), bin.getValue().getYear());
        Assertions.assertEquals(soon.getMonth(), bin.getValue().getMonth());
    }

    @Test
    public void testDate14FutureTolerance() throws ParseException, IOException {
        ZonedDateTime soon = ZonedDateTime.now().plusSeconds(50);
        byte[] buf = IsoType.DATE14.format(soon).getBytes();
        Date14ParseInfo parser = new Date14ParseInfo();
        IsoValue<ZonedDateTime> comp = parser.parse(0, buf, 0, null);
        Assertions.assertTrue(comp.getValue().isAfter(ZonedDateTime.now()));
        //Now with the binary
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<ZonedDateTime> bin = parser.parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(soon.withNano(0), bin.getValue());

        //now with string encoding
        parser.setForceStringDecoding(true);
        comp = parser.parse(0, buf, 0, null);
        Assertions.assertTrue(comp.getValue().isAfter(ZonedDateTime.now()));
        //Now with the binary
        bout.reset();
        comp.write(bout, true, true);
        bin = parser.parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(soon.withNano(0), bin.getValue());
    }

    @Test
    public void testTime() throws ParseException, IOException {
        OffsetTime now = OffsetTime.now().withNano(0);
        byte[] buf = IsoType.TIME.format(now).getBytes();
        TimeParseInfo parser = new TimeParseInfo();
        IsoValue<OffsetTime> comp = parser.parse(1, buf, 0, null);
        Assertions.assertEquals(now, comp.getValue());
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        comp.write(bout, true, false);
        IsoValue<OffsetTime> bin = parser.parseBinary(1, bout.toByteArray(), 0, null);
        Assertions.assertEquals(now, bin.getValue());

        parser.setForceStringDecoding(true);
        comp = parser.parse(0, buf, 0, null);
        Assertions.assertEquals(now, comp.getValue());
        //Now with the binary
        bout.reset();
        comp.write(bout, true, true);
        bin = parser.parseBinary(0, bout.toByteArray(), 0, null);
        Assertions.assertEquals(now, bin.getValue());
    }
}
