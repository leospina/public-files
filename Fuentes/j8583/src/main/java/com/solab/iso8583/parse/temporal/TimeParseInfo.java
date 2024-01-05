/*
j8583 A Java implementation of the ISO8583 protocol
Copyright (C) 2007 Enrique Zamudio Lopez

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/
package com.solab.iso8583.parse.temporal;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.util.Bcd;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetTime;

/** TIME field to OffsetTime converter */
public class TimeParseInfo extends TemporalParseInfo<OffsetTime> {

    public TimeParseInfo() {
        super(IsoType.TIME, 6);
    }

    @Override
    public <T> IsoValue<OffsetTime> parse(final int field, final byte[] buf,
                                    final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid TIME field %d pos %d",
                    field, pos), pos);
        } else if (pos+6 > buf.length) {
            throw new ParseException(String.format(
                    "Insufficient data for TIME field %d, pos %d", field, pos), pos);
        }
        int hour, minute, second;
        if (forceStringDecoding) {
            hour = Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10);
            minute = Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10);
            second = Integer.parseInt(new String(buf, pos+4, 2, getCharacterEncoding()), 10);
        } else {
            hour = ((buf[pos] - 48) * 10) + buf[pos + 1] - 48;
            minute = ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48;
            second = ((buf[pos + 4] - 48) * 10) + buf[pos + 5] - 48;
        }
        var lt = LocalTime.of(hour, minute, second);
        var offset = getZoneId().getRules().getOffset(Instant.now());
        return new IsoValue<>(IsoType.TIME, lt.atOffset(offset), null);
    }

    @Override
    public <T> IsoValue<OffsetTime> parseBinary(final int field, final byte[] buf,
                                          final int pos, final CustomField<T> custom)
            throws ParseException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid bin TIME field %d pos %d",
                    field, pos), pos);
        } else if (pos+3 > buf.length) {
            throw new ParseException(String.format(
                    "Insufficient data for bin TIME field %d, pos %d", field, pos), pos);
        }
        int[] tens = new int[3];
        int start = 0;
        for (int i = pos; i < pos + 3; i++) {
            tens[start++] = Bcd.parseBcdLength(buf[i]);
        }
        var lt = LocalTime.of(tens[0], tens[1], tens[2]);
        var offset = getZoneId().getRules().getOffset(Instant.now());
        return new IsoValue<>(IsoType.TIME, lt.atOffset(offset), null);
    }
}
