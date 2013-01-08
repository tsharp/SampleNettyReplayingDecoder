package com.orbitalforge.example.net.codec;

public final class SampleMessageAttribute {
	private final int fieldId;
	private final byte[] data;
	
	public SampleMessageAttribute(int fieldId, byte[] data) {
		this.fieldId = fieldId;
		this.data = data;
	}
	
	public int getFieldId() {
		return this.fieldId;
	}
	
	public byte[] getData() {
		return this.data;
	}
	
}