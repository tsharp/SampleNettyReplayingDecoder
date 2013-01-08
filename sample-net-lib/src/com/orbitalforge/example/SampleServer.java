package com.orbitalforge.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.orbitalforge.example.net.codec.SampleMessage;
import com.orbitalforge.example.net.handler.SampleBasePipelineFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SampleServer {

    private class ShutdownTask implements Runnable {

    	private final SampleServer serverObject;
    	
    	public ShutdownTask(SampleServer serverObject) {
    		this.serverObject = serverObject;
    	}
    	
		@Override
		public void run() {
			serverObject.bootstrap.shutdown();
			System.out.println("Server Cleanup Finished.");
		}
    	
    }
	
	private final Thread shutdownThread;
	private final ShutdownTask shutdownTask = new ShutdownTask(this);
    private final int port;
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    static final ChannelGroup	allChannels	= new DefaultChannelGroup(
			"dmforge-server-channels");
    
    private SampleServer() {
        this.port = 8999;
        this.shutdownThread = new Thread(this.shutdownTask);
        Runtime.getRuntime().addShutdownHook(this.shutdownThread);
    }
    
    public void run() throws Exception {
        try {
        	System.out.println("Configuring ServerBootstrap...");
        	bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
        	.option(ChannelOption.TCP_NODELAY, true)	// No delay on TCP Operations
        	.option(ChannelOption.SO_KEEPALIVE, true)	// Keep Alive
            .channel(NioServerSocketChannel.class)
            .localAddress(port)
            .childHandler(new SampleBasePipelineFactory<SampleServerHandler>(new SampleServerHandler()));

        	System.out.println("Binding Server Channel...");
            Channel chan = bootstrap.bind().sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Server Started.");
            System.out.println("Console Mode Enabled.");
            for (;;) {
                String line = in.readLine();
                if (line.toLowerCase().trim().equals("exit")) {
                    break;
                } else if(line.toLowerCase().trim().equals("test")) {
                	chan.write(new SampleMessage(0xFF));
                }
            }
        } finally {
        	cleanup();
        	System.out.println("Server Shutdown.");
        }
    }
    
    private void cleanup() throws InterruptedException {
    	allChannels.close().await();
    	bootstrap.shutdown();
    }

	public void stop() {
		try {
			cleanup();
			Runtime.getRuntime().exit(1);
		} catch (InterruptedException e) {
		}
	}
}
