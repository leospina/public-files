/*
 * j8583 A Java implementation of the ISO8583 protocol
 * Copyright (C) 2007 Enrique Zamudio Lopez
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
package com.solab.iso8583.codecs;

import com.solab.iso8583.CustomBinaryField;
import com.solab.iso8583.util.Bcd;

/**
 * A custom field encoder/decoder to be used with LLBIN/LLLBIN fields
 * that contain Longs in BCD encoding.
 *
 * @author Enrique Zamudio
 *         Date: 07/05/13 13:02
 */
public class LongBcdCodec implements CustomBinaryField<Long> {

    private final boolean rightPadded;

    public LongBcdCodec() {
        this(false);
    }
    public LongBcdCodec(boolean rightPadding) {
        rightPadded = rightPadding;
    }

    @Override
    public Long decodeBinaryField(byte[] value, int pos, int length) {
        return rightPadded ? Bcd.decodeRightPaddedToLong(value, pos, length*2)
                : Bcd.decodeToLong(value, pos, length*2);
    }

    @Override
    public byte[] encodeBinaryField(Long value) {
        final var s = Long.toString(value, 10);
        final var buf = new byte[s.length() / 2 + s.length() % 2];
        if (rightPadded) {
            Bcd.encodeRightPadded(s, buf);
        } else {
            Bcd.encode(s, buf);
        }
        return buf;
    }

    @Override
    public Long decodeField(String value) {
        return Long.parseLong(value, 10);
    }

    @Override
    public String encodeField(Long value) {
        return value.toString();
    }
}
