package com.orbitalforge.example.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SampleEncoder extends MessageToByteEncoder<SampleMessage> {

	@Override
	public void encode(ChannelHandlerContext ctx, SampleMessage msg,
			ByteBuf out) throws Exception {
		
		// Begin The Message Encoding
		out.markWriterIndex();
		out.writeShort(msg.getOpcode());
		out.writeShort(msg.getNumAttributes());
		
		// TODO: FIXME FIXME
		// This needs a better implementation in the event that a malicious request
		// is sent in - it could potentially bring the server to it's knees.
		for(Integer key : msg.getAttributeKeys()) {
			SampleMessageAttribute a = msg.getAttribute(key);
			out.writeShort(a.getFieldId());
			out.writeShort(a.getData().length);
			out.writeBytes(a.getData());
			// Sanity Variable - Makes Sure The Decoder Knows What It's Doing
			out.writeByte(SampleCodecConstant.RS.getCode());
		}
		
		// Ends the transmission
		out.writeByte(SampleCodecConstant.EOT.getCode());
		
		// Flushes data - TODO: Determine if this is bad or not.
		ctx.flush();
	}

}
