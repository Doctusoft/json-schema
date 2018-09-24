package org.everit.json.schema.internal;

import com.google.common.base.Optional;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestDateTimeFormatValidator {
    
    private DateTimeFormatValidator validator = new DateTimeFormatValidator();
    
    @Test
    public void testValidFormat() {
        String[] inputData = new String[]{"2018-12-14T12:23:54.112Z",
                                          "2018-12-14T12:23:54.112+0200",
                                          "2018-12-14T12:23:54.112+02:00",
                                          "2018-12-14T12:23:54Z"};
        
        for (String input : inputData) {
            Optional<String> validationResult = validator.validate(input);
            
            assertThat("Validation failed for input: " + input, validationResult.isPresent(), equalTo(false));
        }
    }
    
    @Test
    public void testInvalidFormat() {
        String[] inputData = new String[]{"2018-12-14T12:23:54.112",
                                          "2018-12-14T12:23:54.112ZZ",
                                          "2018-12-14T12:23:54.112+02-00",
                                          "2018-12-14 12:23:54.112Z"};
        
        for (String input : inputData) {
            String validationResult = validator.validate(input).get();
            
            assertThat(validationResult, containsString("is not a valid date-time. Expected "));
        }
    }
}
