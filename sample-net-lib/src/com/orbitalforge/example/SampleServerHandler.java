package com.orbitalforge.example;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import java.net.UnknownHostException;
import com.orbitalforge.example.net.codec.SampleMessage;
import com.orbitalforge.example.net.handler.SampleBaseHandler;

@Sharable
public class SampleServerHandler extends SampleBaseHandler {
	private static int numMessages = 0;
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("Unexpected exception from downstream. " + cause.getMessage()); 
		SampleServer.allChannels.remove(ctx.channel());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx)
			throws UnknownHostException {
		SampleServer.allChannels.add(ctx.channel());
		System.out.println("Channel Opened.");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		SampleServer.allChannels.remove(ctx.channel());
		System.out.println("Channel Closed.");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, SampleMessage msg)
			throws Exception {
		numMessages++;
		// Server Command - Shutdown
		if(msg.getOpcode() == 0x00) {
			System.exit(0); // Dirty Hack - Should be something better
		} else if(msg.getOpcode() == 0x01) {
			ctx.close().sync();
		}
		else if(msg.getAttribute(0) != null) {
			/*
			String str = new String(msg.getAttribute(0).getData(), "UTF-8");
			DMLog.instance().logger.info(String.format("Data Received: %s", str));
			*/
			System.out.println(String.format("Data Received: %d", numMessages));
		}
		
	}
}
