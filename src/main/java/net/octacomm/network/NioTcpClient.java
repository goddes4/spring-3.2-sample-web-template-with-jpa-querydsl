package net.octacomm.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

/**
 *
 * @author Taeyoung, Kim
 */
public class NioTcpClient extends SimpleChannelHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String localIP;
    private int localPort;
    private String serverIP;
    private int serverPort;
    private ChannelPipelineFactory pipelineFactory;

    private final ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
    		Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    
	private final Runnable retryConnect = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
			connect();
		}
	};

    
    @Autowired
    private TaskExecutor executor; 

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setPipelineFactory(ChannelPipelineFactory pipelineFactory) {
        this.pipelineFactory = pipelineFactory;
    }
    
    public void init() {
        bootstrap.setPipelineFactory(pipelineFactory);

        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        connect();
    }

	private void connect() {
		logger.info("Attempts a new connection to TCP Server.");
        ChannelFuture future = null;

        if (localPort == 0) {
            future = bootstrap.connect(new InetSocketAddress(serverIP, serverPort));
        } else {
            if (localIP == null || localIP.isEmpty()) {
                future = bootstrap.connect(new InetSocketAddress(serverIP, serverPort), new InetSocketAddress(localPort));
            } else {
                future = bootstrap.connect(new InetSocketAddress(serverIP, serverPort), new InetSocketAddress(localIP, localPort));
            }
        }
        future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					logger.info("Success!!!");

					future.getChannel().getCloseFuture().addListener(new ChannelFutureListener() {
			            @Override
			            public void operationComplete(ChannelFuture cf) throws Exception {
			            	logger.info("Channel Close!!!");
							executor.execute(retryConnect);
			            }
			        });
					
				} else {
					logger.info("Failre!!! Retry to connect");
					executor.execute(retryConnect);
				}
			}
		});
	}

    public static void main(String[] args) {
        NioTcpClient tcpClient = new NioTcpClient();
        tcpClient.setServerIP("127.0.0.1");
        tcpClient.setServerPort(9001);
        tcpClient.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.softCachingResolver(null)), new SimpleChannelHandler() {

                    @Override
                    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
                        ctx.getChannel().write("xxx");
                    }

                    @Override
                    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
                        System.out.println(e.getMessage().toString());
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
                        e.getCause().printStackTrace();
                        e.getChannel().close();
                    }
                });
            }
        });
        tcpClient.init();
    }
}
