package com.orbitalforge.example.net.handler;

import com.orbitalforge.example.net.codec.SampleMessage;

import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public abstract class SampleBaseHandler extends ChannelInboundMessageHandlerAdapter<SampleMessage>  {
	/*
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
	public void messageReceived(ChannelHandlerContext ctx, DMForgeMessage msg)
			throws Exception {
		System.out.println(String.format("Data Received: %d", msg.getOpcode()));		
	}	
	*/
}