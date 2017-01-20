package org.everit.json.schema.loader.internal;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.everit.json.schema.AbstractFormatValidator;
import org.everit.json.schema.FormatValidator;

public class WrappingFormatValidator extends AbstractFormatValidator {

    private final String formatName;
    private final FormatValidator formatValidator;

    public WrappingFormatValidator(String formatName, FormatValidator wrappedValidator) {
        this.formatName = Preconditions.checkNotNull(formatName, "formatName cannot be null");
        this.formatValidator = Preconditions.checkNotNull(wrappedValidator, "wrappedValidator cannot be null");
    }

    @Override
    public Optional<String> validate(String subject) {
        return formatValidator.validate(subject);
    }

    @Override
    public String formatName() {
        return formatName;
    }
}
