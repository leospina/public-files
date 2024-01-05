package com.solab.iso8583.parse.temporal;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.util.Bcd;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalDate;

/** Converts fields of type DATE_EXP into LocalDate objects. */
public class DateExpParseInfo extends TemporalParseInfo<LocalDate> {

    public DateExpParseInfo() {
        super(IsoType.DATE_EXP, 4);
    }

    @Override
    public <T> IsoValue<LocalDate> parse(final int field, final byte[] buf,
                                    final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE_EXP field %d position %d",
                    field, pos), pos);
        }
        if (pos+4 > buf.length) {
            throw new ParseException(String.format(
                    "Insufficient data for DATE_EXP field %d pos %d", field, pos), pos);
        }
        var when = LocalDate.now();
        int year, month;
        if (forceStringDecoding) {
            year = when.getYear() - (when.getYear() % 100)
                    + Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10);
            month = Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10);
        } else {
            year = when.getYear() - (when.getYear() % 100)
                    + ((buf[pos] - 48) * 10) + buf[pos + 1] - 48;
            month = ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48;
        }
        when = LocalDate.of(year, month, 1);
        return new IsoValue<>(IsoType.DATE_EXP, when, null);
    }

    @Override
    public <T> IsoValue<LocalDate> parseBinary(final int field, final byte[] buf,
                                          final int pos, final CustomField<T> custom)
            throws ParseException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE_EXP field %d position %d",
                    field, pos), pos);
        }
        if (pos+2 > buf.length) {
            throw new ParseException(String.format(
                    "Insufficient data for DATE_EXP field %d pos %d", field, pos), pos);
        }
        int[] tens = new int[2];
        int start = 0;
        for (int i = pos; i < pos + tens.length; i++) {
            tens[start++] = Bcd.parseBcdLength(buf[i]);
        }
        //Set the month in the date
        var when = LocalDate.now();
        int year = when.getYear() - (when.getYear() % 100) + tens[0];
        when = LocalDate.of(year, tens[1], 1);
        return new IsoValue<>(IsoType.DATE_EXP, when, null);
    }
}
