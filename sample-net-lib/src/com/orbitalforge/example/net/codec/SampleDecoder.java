package com.orbitalforge.example.net.codec;

import com.orbitalforge.example.net.exceptions.BadMessageException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class SampleDecoder extends ReplayingDecoder<SampleMessage, SampleDecoder.State> {

	private SampleMessage message;
	private int fieldCount;
	protected enum State {
		READ_HEADER,
		READ_FIELDS,
		FINISHED,
		BAD_PACKET
	}

	public SampleDecoder() {
		super(State.READ_HEADER);
	}
	
	@Override
	public SampleMessage decode(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		
		switch(state()) {
		case READ_HEADER:
			state(readHeader(in));
			checkpoint(state());
			if(state() == State.FINISHED) return reset();
			else if(state() == State.READ_FIELDS) {
				state(readFields(in));
				checkpoint(state());
			} else if(state() == State.BAD_PACKET) {
				throw new BadMessageException("Bad Message Packet.");
			}
			break;
		case READ_FIELDS:
			state(readFields(in));
			checkpoint(state());
			break;
		case FINISHED:
			break;
		default:
            throw new IllegalStateException("Unexpected State");
		}
		
		// Sanity Check
		int sep = in.readByte();
		if(sep != SampleCodecConstant.EOT.getCode()) {
			throw new BadMessageException("Bad Message Packet.");
		}
		
		return reset();
	}
	
	private SampleMessage reset() {
		SampleMessage message = this.message;
		this.message = null;
		this.fieldCount = 0;
		checkpoint(State.READ_HEADER);
		return message;
	}
	
	private State readHeader(ByteBuf buffer) {
		short opcode = buffer.readShort();
		this.message = new SampleMessage(opcode);
		this.fieldCount = buffer.readShort();
		if(fieldCount > 0) {
			return State.READ_FIELDS;
		} else {
			return State.FINISHED;
		}
	}

	private State readFields(ByteBuf buffer) {
		
		// TODO: FIXME FIXME
		// This needs a better implementation in the event that a malicious request
		// is sent in - it could potentially bring the server to it's knees.
		for(int ii = 0; ii < fieldCount; ii++) {
			int fieldId = buffer.readUnsignedShort();
			int fieldSize = buffer.readUnsignedShort();
			byte[] data = new byte[fieldSize];
			buffer.readBytes(data, 0, fieldSize);
			this.message.addAttribute(new SampleMessageAttribute(fieldId, data));
			
			// Sanity Check
			int sep = buffer.readByte();
			if(sep != SampleCodecConstant.RS.getCode()) {
				return State.BAD_PACKET;
			}
			
			checkpoint(State.READ_FIELDS);
		}
		
		return State.FINISHED;
	}
	
}
