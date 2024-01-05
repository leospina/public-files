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
package com.solab.iso8583.parse.temporal;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.util.Bcd;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/** A converter for DATE10 fields, using ZonedDateTime, always in the past.
 */
public class Date10ParseInfo extends TemporalParseInfo<ZonedDateTime> {

    public Date10ParseInfo() {
        super(IsoType.DATE10, 10);
    }

    @Override
    public <T> IsoValue<ZonedDateTime> parse(final int field, final byte[] buf,
                                    final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE10 field %d position %d",
                    field, pos), pos);
        }
        if (pos+10 > buf.length) {
            throw new ParseException(String.format("Insufficient data for DATE10 field %d, pos %d",
                    field, pos), pos);
        }
        //Set the month in the date
        int month, day, hour, minute, second;
        if (forceStringDecoding) {
            month = Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10);
            day = Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10);
            hour = Integer.parseInt(new String(buf, pos+4, 2, getCharacterEncoding()), 10);
            minute = Integer.parseInt(new String(buf, pos+6, 2, getCharacterEncoding()), 10);
            second = Integer.parseInt(new String(buf, pos+8, 2, getCharacterEncoding()), 10);
        } else {
            month = ((buf[pos] - 48) * 10) + buf[pos + 1] - 48;
            day = ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48;
            hour = ((buf[pos + 4] - 48) * 10) + buf[pos + 5] - 48;
            minute = ((buf[pos + 6] - 48) * 10) + buf[pos + 7] - 48;
            second = ((buf[pos + 8] - 48) * 10) + buf[pos + 9] - 48;
        }
        var t = ZonedDateTime.of(LocalDate.now().getYear(), month, day, hour, minute, second, 0, getZoneId());
        return new IsoValue<>(IsoType.DATE10, adjustWithFutureTolerance(t), null);
    }

    @Override
    public <T> IsoValue<ZonedDateTime> parseBinary(final int field, final byte[] buf,
                                          final int pos, final CustomField<T> custom)
            throws ParseException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE10 field %d position %d",
                    field, pos), pos);
        }
        if (pos+5 > buf.length) {
            throw new ParseException(String.format("Insufficient data for DATE10 field %d, pos %d",
                    field, pos), pos);
        }
        int[] tens = new int[5];
        int start = 0;
        for (int i = pos; i < pos + tens.length; i++) {
            tens[start++] = Bcd.parseBcdLength(buf[i]);
        }
        var t = ZonedDateTime.of(LocalDate.now().getYear(), tens[0], tens[1], tens[2], tens[3], tens[4], 0, getZoneId());
        return new IsoValue<>(IsoType.DATE10, adjustWithFutureTolerance(t), null);
    }
}
