package com.orbitalforge.example;
import com.orbitalforge.example.net.codec.SampleMessage;
import com.orbitalforge.example.net.handler.SampleBaseHandler;

import io.netty.channel.ChannelHandlerContext;

public class SampleClientHandler extends SampleBaseHandler {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		System.out.println("Channel Active.");	
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		System.out.println("Channel Inactive.");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
		System.out.println("Channel Exception.");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, SampleMessage msg)
			throws Exception {
		System.out.println(String.format("Data Received: %d", msg.getOpcode()));		
	}
}
