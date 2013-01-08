package com.orbitalforge.example.net.handler;

import com.orbitalforge.example.net.codec.SampleDecoder;
import com.orbitalforge.example.net.codec.SampleEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public final class SampleBasePipelineFactory<T extends SampleBaseHandler> extends ChannelInitializer<SocketChannel> {
	
	private final static SampleEncoder ENCODER = new SampleEncoder();
	private static SampleBaseHandler HANDLER;
	
	public SampleBasePipelineFactory(T handler) {
		if(HANDLER == null) {
			HANDLER = handler;
		}
	}
	
	@Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // This is NOT a sharable object!
        pipeline.addLast("decoder", new SampleDecoder());
        
        // These are sharable as they do not share state information
        pipeline.addLast("encoder", ENCODER);	// Encode Data To Be Sent
        pipeline.addLast("handler", HANDLER);	// Business Logic
    }
}
