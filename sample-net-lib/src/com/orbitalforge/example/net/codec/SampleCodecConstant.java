package com.orbitalforge.example.net.codec;

public enum SampleCodecConstant {
	EOT(0x4),	// End of transmission
	RS(0x1E);	// Record Separator
	
	private final int code;
	
	SampleCodecConstant(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
	
}
