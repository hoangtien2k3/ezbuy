package com.ezbuy.orderservice.utils;

import java.io.StringReader;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import reactor.core.publisher.Mono;

@Log4j2
public class CommonUtils {

    // Convert Mono<T> to Mono<Optional<T>>
    public static <T> Mono<Optional<T>> optional(Mono<T> mono) {
        return mono.map(Optional::of).defaultIfEmpty(Optional.empty());
    }

    public static String getValueFromXml(
            String xml, String startSpecial, String endSpecial, String splitChar, String replaceChar, String tagName) {
        xml = xml.replace(startSpecial, "");
        xml = xml.replace(endSpecial, "");
        xml = xml.replace(splitChar, replaceChar);
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            InputSource inputSource = new InputSource(new StringReader(xml));
            Document doc = dBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();

            return doc.getElementsByTagName(tagName).item(0).getTextContent();
        } catch (Exception e) {
            return null;
        }
    }
}
