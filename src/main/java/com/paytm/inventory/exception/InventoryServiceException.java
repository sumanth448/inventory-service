package com.paytm.inventory.exception;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
public class InventoryServiceException extends RuntimeException {
    private final String errorCode;

    private final String errorMessage;

    public InventoryServiceException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;

    }

    @ExceptionHandler({ WebExchangeBindException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleSmsServiceException(WebExchangeBindException ex) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("error_code", "TXN-400");
            obj.put("error_message", ex.getFieldError().getField() + " " + ex.getFieldError().getDefaultMessage());
            log.info("Formatting Exception response {}", obj.toString());
        } catch (JSONException e) {
            log.error("Error creating json obj {}", e);
        }
        return obj.toString();
    }
}
