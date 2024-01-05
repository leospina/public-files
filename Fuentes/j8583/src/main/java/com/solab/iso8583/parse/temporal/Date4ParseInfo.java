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

/** Parses fields of type DATE4, converting into a LocalDate,
 * always using the current year.
 */
public class Date4ParseInfo extends TemporalParseInfo<LocalDate> {

    public Date4ParseInfo() {
        super(IsoType.DATE4, 4);
    }

    @Override
    public <T> IsoValue<LocalDate> parse(final int field, final byte[] buf, final int pos,
                                         final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE4 field %d position %d",
                    field, pos), pos);
        }
        if (pos+4 > buf.length) {
            throw new ParseException(String.format(
                    "Insufficient data for DATE4 field %d, pos %d", field, pos), pos);
        }
        int month, day;
        //Set the month in the date
        if (forceStringDecoding) {
            month = Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10);
            day = Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10);
        } else {
            month = ((buf[pos] - 48) * 10) + buf[pos + 1] - 48;
            day = ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48;
        }
        return new IsoValue<>(type, LocalDate.now().withMonth(month).withDayOfMonth(day), null);
    }

    @Override
    public <T> IsoValue<LocalDate> parseBinary(final int field, final byte[] buf, final int pos,
                                          final CustomField<T> custom) throws ParseException {
        int[] tens = new int[2];
        int start = 0;
        if (buf.length-pos < 2) {
            throw new ParseException(String.format(
                    "Insufficient data to parse binary DATE4 field %d pos %d",
                    field, pos), pos);
        }
        for (int i = pos; i < pos + tens.length; i++) {
            tens[start++] = Bcd.parseBcdLength(buf[i]);
        }
        return new IsoValue<>(type, LocalDate.now().withMonth(tens[0]).withDayOfMonth(tens[1]), null);
    }

}
