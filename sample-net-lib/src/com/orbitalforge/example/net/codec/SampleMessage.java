package com.orbitalforge.example.net.codec;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SampleMessage {
		
	private final int opcode;
	private final Map<Integer, SampleMessageAttribute> attributes;
	
	public SampleMessage(int opcode) {
		this.opcode = opcode;
		this.attributes = new HashMap<Integer, SampleMessageAttribute>();
	}
	
	public int getOpcode() {
		return this.opcode;
	}
	
	public SampleMessageAttribute getAttribute(Integer id) {
		if(attributes.containsKey(id)) {
			return attributes.get(id);
		}
		
		return null;
	}
	
	public void addAttribute(SampleMessageAttribute attribute) {
		if(!attributes.containsKey(attribute.getFieldId())) {
			this.attributes.put(attribute.getFieldId(), attribute);
		}
	}
	
	public void addAttribute(int fieldId, byte[] data) {
		if(!attributes.containsKey(fieldId)) {
			this.attributes.put(fieldId, new SampleMessageAttribute(fieldId, data));
		}
	}
	
	public void addAttribute(int fieldId, String data) {
		if(!attributes.containsKey(fieldId)) {
			this.attributes.put(fieldId, new SampleMessageAttribute(fieldId, data.getBytes()));
		}
	}
	
	public int getNumAttributes() {
		return this.attributes.size();
	}
	
	public Set<Integer> getAttributeKeys() {
		return this.attributes.keySet();
	}
}
