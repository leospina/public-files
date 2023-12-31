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

public class Date6ParseInfo extends TemporalParseInfo<LocalDate> {

    public Date6ParseInfo() {
        super(IsoType.DATE6, 6);
    }

    @Override
    public <T> IsoValue<LocalDate> parse(int field, byte[] buf, int pos, CustomField<T> custom) throws ParseException, UnsupportedEncodingException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE6 field %d position %d",
                    field, pos), pos);
        }
        if (pos+6 > buf.length) {
            throw new ParseException(String.format(
                    "Insufficient data for DATE6 field %d, pos %d", field, pos), pos);
        }
        //Set the month in the date
        int year, month, day;
        if (forceStringDecoding) {
            year = Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10);
            month = Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10);
            day = Integer.parseInt(new String(buf, pos+4, 2, getCharacterEncoding()), 10);
        } else {
            year = ((buf[pos] - 48) * 10) + buf[pos + 1] - 48;
            month = ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48;
            day = ((buf[pos + 4] - 48) * 10) + buf[pos + 5] - 48;
        }
        if (year > 50) {
            year += 1900;
        } else {
            year += 2000;
        }
        return new IsoValue<>(IsoType.DATE6, LocalDate.of(year, month, day), null);
    }

    @Override
    public <T> IsoValue<LocalDate> parseBinary(int field, byte[] buf, int pos, CustomField<T> custom) throws ParseException, UnsupportedEncodingException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE6 field %d position %d",
                    field, pos), pos);
        }
        if (pos+3 > buf.length) {
            throw new ParseException(String.format("Insufficient data for DATE6 field %d, pos %d",
                    field, pos), pos);
        }
        int[] tens = new int[3];
        int start = 0;
        for (int i = pos; i < pos + tens.length; i++) {
            tens[start++] = Bcd.parseBcdLength(buf[i]);
        }
        //A SimpleDateFormat in the case of dates won't help because of the missing data
        //we have to use the current date for reference and change what comes in the buffer
        //Set the month in the date
        int year, month, day;
        if (tens[0] > 50) {
            year = 1900 + tens[0];
        } else {
            year = 2000 + tens[0];
        }
        month = tens[1];
        day = tens[2];
        return new IsoValue<>(IsoType.DATE6, LocalDate.of(year, month, day), null);
    }
}
