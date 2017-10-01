package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Represents the result of an OCR parse
 */
public class ReceiptSuggestionResponse {
    @JsonProperty
    public final String merchantName;

    @JsonProperty
    public final BigDecimal amount;

    @JsonProperty
    public final Integer xVal1;

    @JsonProperty
    public final Integer xVal2;

    @JsonProperty
    public final Integer yVal1;

    @JsonProperty
    public final Integer yVal2;

    public ReceiptSuggestionResponse(String merchantName, BigDecimal amount, Integer xVal1, Integer xVal2, Integer yVal1, Integer yVal2) {
        this.merchantName = merchantName;
        this.amount = amount;
        this.xVal1 = xVal1;
        this.xVal2 = xVal2;
        this.yVal1 = yVal1;
        this.yVal2 = yVal2;
    }
}
