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

import com.solab.iso8583.IsoType;
import com.solab.iso8583.parse.FieldParseInfo;

import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public abstract class TemporalParseInfo<T extends Temporal> extends FieldParseInfo {

    protected static final long FUTURE_TOLERANCE;
    private ZoneId zoneId;
    private static ZoneId defaultZoneId = ZoneId.systemDefault();

    static {
        FUTURE_TOLERANCE = Long.parseLong(System.getProperty("j8583.future.tolerance", "900000"));
    }

    public static void setDefaultZoneId(ZoneId tz) {
        defaultZoneId = tz;
    }
    public static ZoneId getDefaultZoneId() {
        return defaultZoneId;
    }

    protected TemporalParseInfo(IsoType type, int length) {
        super(type, length);
    }

    public void setZoneId(ZoneId value) {
        zoneId = value;
    }
    public ZoneId getZoneId() {
        return zoneId != null ? zoneId : defaultZoneId;
    }

    /** Returns a Date/Time parser with the old Date API. */
    public static TemporalParseInfo<?> createParserForType(IsoType t) {
        switch (t) {
            case DATE10: return new Date10ParseInfo();
            case DATE12: return new Date12ParseInfo();
            case DATE14: return new Date14ParseInfo();
            case DATE4: return new Date4ParseInfo();
            case DATE6: return new Date6ParseInfo();
            case DATE_EXP: return new DateExpParseInfo();
            case TIME: return new TimeParseInfo();
        }
        throw new IllegalArgumentException(t + " is not a date/time type");
    }

    @SuppressWarnings("unchecked")
    protected T adjustWithFutureTolerance(T cal) {
        //We need to handle a small tolerance into the future (a couple of minutes)
        long now = System.currentTimeMillis();
        long then = cal.getLong(ChronoField.INSTANT_SECONDS) * 1000L;
        if (then > now && then-now > FUTURE_TOLERANCE) {
            return (T)cal.minus(1, ChronoUnit.YEARS);
        }
        return cal;
    }
}
