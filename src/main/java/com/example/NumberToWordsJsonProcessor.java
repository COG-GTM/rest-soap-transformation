package com.example;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Renders the SOAP result list as the JSON the former jacksonxml + xmljson marshal chain produced,
 * e.g. {"item":"forty two "}, without the intermediate XML document.
 */
public class NumberToWordsJsonProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        List<?> result = exchange.getIn().getMandatoryBody(List.class);
        JSONObject json = new JSONObject();
        for (Object value : result) {
            String text = value == null ? "" : value.toString();
            if (text.isEmpty()) {
                json.accumulate("item", new JSONArray());
            } else {
                json.accumulate("item", text);
            }
        }
        exchange.getIn().setBody(json.toString());
    }
}
