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
import java.time.ZonedDateTime;

public class Date14ParseInfo extends TemporalParseInfo<ZonedDateTime> {

    public Date14ParseInfo() {
        super(IsoType.DATE14, 14);
    }

    @Override
    public <T> IsoValue<ZonedDateTime> parse(final int field, final byte[] buf,
                                    final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE14 field %d position %d",
                    field, pos), pos);
        }
        if (pos+14 > buf.length) {
            throw new ParseException(String.format("Insufficient data for DATE14 field %d, pos %d",
                    field, pos), pos);
        }
        //Set the month in the date
        int year, month, day, hour, minute, second;
        if (forceStringDecoding) {
            year = Integer.parseInt(new String(buf, pos, 4, getCharacterEncoding()), 10);
            month = Integer.parseInt(new String(buf, pos + 4, 2, getCharacterEncoding()), 10);
            day = Integer.parseInt(new String(buf, pos + 6, 2, getCharacterEncoding()), 10);
            hour = Integer.parseInt(new String(buf, pos + 8, 2, getCharacterEncoding()), 10);
            minute = Integer.parseInt(new String(buf, pos + 10, 2, getCharacterEncoding()), 10);
            second = Integer.parseInt(new String(buf, pos + 12, 2, getCharacterEncoding()), 10);
        } else {
            year = ((buf[pos] - 48) * 1000) + ((buf[pos + 1] - 48) * 100) + ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48;
            month = ((buf[pos+4] - 48) * 10) + buf[pos + 5] - 48;
            day = ((buf[pos + 6] - 48) * 10) + buf[pos + 7] - 48;
            hour = ((buf[pos + 8] - 48) * 10) + buf[pos + 9] - 48;
            minute = ((buf[pos + 10] - 48) * 10) + buf[pos + 11] - 48;
            second = ((buf[pos + 12] - 48) * 10) + buf[pos + 13] - 48;
        }
        var t = ZonedDateTime.of(year, month, day, hour, minute, second, 0, getZoneId());
        return new IsoValue<>(IsoType.DATE14, adjustWithFutureTolerance(t), null);
    }

    @Override
    public <T> IsoValue<ZonedDateTime> parseBinary(final int field, final byte[] buf,
                                          final int pos, final CustomField<T> custom)
            throws ParseException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE14 field %d position %d",
                    field, pos), pos);
        }
        if (pos+7 > buf.length) {
            throw new ParseException(String.format("Insufficient data for DATE14 field %d, pos %d",
                    field, pos), pos);
        }
        int[] tens = new int[7];
        int start = 0;
        for (int i = pos; i < pos + tens.length; i++) {
            tens[start++] = Bcd.parseBcdLength(buf[i]);
        }
        //A SimpleDateFormat in the case of dates won't help because of the missing data
        //we have to use the current date for reference and change what comes in the buffer
        //Set the month in the date
        var t = ZonedDateTime.of((tens[0] * 100) + tens[1], tens[2], tens[3],
            tens[4], tens[5], tens[6], 0, getZoneId());
        return new IsoValue<>(IsoType.DATE14, adjustWithFutureTolerance(t), null);
    }
}
