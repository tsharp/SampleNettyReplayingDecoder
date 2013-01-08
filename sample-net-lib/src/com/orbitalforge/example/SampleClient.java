package com.orbitalforge.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.orbitalforge.example.net.codec.SampleMessage;
import com.orbitalforge.example.net.handler.SampleBasePipelineFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SampleClient {

    private final String host;
    private final int port;

    public SampleClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
    	Bootstrap b = new Bootstrap();
    	Channel ch = null;
        try {
            b.group(new NioEventLoopGroup())
             .option(ChannelOption.TCP_NODELAY, true)
             .channel(NioSocketChannel.class)
             .remoteAddress(host, port)
             .handler(new SampleBasePipelineFactory<SampleClientHandler>(new SampleClientHandler()));

            // Start the connection attempt.
            ch = b.connect().sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null || !ch.isActive() || !ch.isOpen()) {
                    break;
                }

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                	lastWriteFuture = ch.write(new SampleMessage(0x01)).sync();
                    ch.closeFuture().sync();
                    break;
                }
                
                if ("kill-server".equals(line.toLowerCase())) {
                	lastWriteFuture = ch.write(new SampleMessage(0x00)).sync();
                    ch.closeFuture().sync();
                    break;
                }
                for(int i = 0; i < 100000; i++) { 
                	SampleMessage msg = new SampleMessage(0x02);
                	msg.addAttribute(0, line);
                
                	lastWriteFuture = ch.write(msg); // .sync();
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
        	if(ch != null) {
        		ch.close();
        	}
            b.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
          new SampleClient("localhost", 8999).run();
    }
}