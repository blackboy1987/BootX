
package com.bootx.common;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import java.math.BigDecimal;

public class BigDecimalNumericFieldBridge implements TwoWayFieldBridge {

	@Override
	public Object get(String name, Document document) {
		IndexableField field = document.getField(name);
		return field != null ? new BigDecimal(field.stringValue()) : null;
	}


	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		if (value != null) {
			BigDecimal decimalValue = (BigDecimal) value;
			luceneOptions.addNumericFieldToDocument(name, decimalValue.doubleValue(), document);
		} else if (luceneOptions.indexNullAs() != null) {
			luceneOptions.addFieldToDocument(name, luceneOptions.indexNullAs(), document);
		}
	}

	@Override
	public final String objectToString(Object object) {
		return object != null ? object.toString() : null;
	}

}