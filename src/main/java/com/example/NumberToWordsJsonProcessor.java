package com.example;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
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
        Object json;
        if (result.isEmpty()) {
            json = JSONNull.getInstance();
        } else if (result.size() == 1) {
            json = new JSONObject().element("item", toJsonValue(result.get(0)));
        } else {
            JSONArray items = new JSONArray();
            for (Object value : result) {
                items.element(toJsonValue(value));
            }
            json = items;
        }
        exchange.getIn().setBody(json.toString());
    }

    // XMLSerializer read the XML line by line (dropping raw line feeds; jacksonxml escapes CR) and rendered an empty element as [].
    private static Object toJsonValue(Object value) {
        String text = value == null ? "" : value.toString().replace("\n", "");
        return text.isEmpty() ? new JSONArray() : text;
    }
}
