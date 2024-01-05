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
package com.solab.iso8583.util;

import com.solab.iso8583.IsoType;

import java.time.format.DateTimeFormatter;
import java.util.Map;

/** A handy map for date formats. */
public class DateFormats {
    public static final Map<IsoType, DateTimeFormatter> FORMATS = Map.of(
            IsoType.DATE10, DateTimeFormatter.ofPattern("MMddHHmmss"),
            IsoType.DATE4, DateTimeFormatter.ofPattern("MMdd"),
            IsoType.DATE_EXP, DateTimeFormatter.ofPattern("yyMM"),
            IsoType.TIME, DateTimeFormatter.ofPattern("HHmmss"),
            IsoType.DATE12, DateTimeFormatter.ofPattern("yyMMddHHmmss"),
            IsoType.DATE14, DateTimeFormatter.ofPattern("YYYYMMddHHmmss"),
            IsoType.DATE6, DateTimeFormatter.ofPattern("yyMMdd")
    );
}
