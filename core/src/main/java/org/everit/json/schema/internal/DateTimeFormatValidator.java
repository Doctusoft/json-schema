/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.json.schema.internal;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import org.everit.json.schema.AbstractFormatValidator;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.temporal.ChronoField;

/**
 * Implementation of the "date-time" format value.
 */
public class DateTimeFormatValidator extends AbstractFormatValidator {
    
    private static final List<String> FORMATS_ACCEPTED = ImmutableList.of(
            "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,9}Z"
    );
    
    private static final String PARTIAL_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    
    private static final DateTimeFormatter FORMATTER_NO_COLONS;
    private static final DateTimeFormatter FORMATTER_WITH_COLONS;
    
    private static final String ZONE_OFFSET_PATTERN_NO_COLONS = "XX";
    private static final String ZONE_OFFSET_PATTERN_WITH_COLONS = "XXX";
    
    static {
        final DateTimeFormatterBuilder builderWithOffsetWithoutColons = getBaseFormatterBuilder().appendPattern(ZONE_OFFSET_PATTERN_NO_COLONS);
        final DateTimeFormatterBuilder builderWithOffsetWithColons = getBaseFormatterBuilder().appendPattern(ZONE_OFFSET_PATTERN_WITH_COLONS);
        
        FORMATTER_NO_COLONS = builderWithOffsetWithoutColons.toFormatter();
        FORMATTER_WITH_COLONS = builderWithOffsetWithColons.toFormatter();
    }
    
    @Override
    public Optional<String> validate(final String subject) {
        try {
            FORMATTER_NO_COLONS.parse(subject);
            
            return Optional.absent();
        } catch (DateTimeParseException e1) {
            try {
                FORMATTER_WITH_COLONS.parse(subject);
                return Optional.absent();
            } catch (DateTimeParseException e2) {
                return Optional.of(String.format("[%s] is not a valid date-time. Expected %s", subject, FORMATS_ACCEPTED));
            }
        }
    }
    
    @Override
    public String formatName() {
        return "date-time";
    }
    
    private static DateTimeFormatterBuilder getBaseFormatterBuilder() {
        final DateTimeFormatter secondsFractionFormatter = new DateTimeFormatterBuilder()
                .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                .toFormatter();
        
        return new DateTimeFormatterBuilder()
                .appendPattern(PARTIAL_DATETIME_PATTERN)
                .appendOptional(secondsFractionFormatter);
    }
}
