package com.f.a.allan.utils;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

import com.alibaba.fastjson.JSONObject;

public class MongoAggrerationUtils {
	
public static AggregationOperation aggregateAddFields(final String field, final String valueExpresion) {
		
		AggregationOperation ao = new AggregationOperation() {
			@Override
			public Document toDocument(AggregationOperationContext context) {
				return new Document("$addFields", new Document(field,valueExpresion));
			}
		};
		return ao;
	}
	
public static AggregationOperation aggregateAddFields(final String field, String ops,String fieldExpresion) {
		
		AggregationOperation ao = new AggregationOperation() {
			@Override
			public Document toDocument(AggregationOperationContext context) {
				JSONObject json = new JSONObject();
				json.put(ops, fieldExpresion);
				return new Document("$addFields", new Document(field,json));
			}
		};
		return ao;
	}
}
