package com.skymoe.light.http.netty;

import com.google.common.base.Preconditions;
import com.skymoe.light.http.exception.ExceptionLogger;
import com.skymoe.light.http.exception.ExceptionLoggerContext;
import com.skymoe.light.http.exception.SimpleExceptionLogger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http服务启动器
 * BossEventLoopGroup加工人EventLoopGroup方式
 *
 * 用法例子：
 *
	 ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:spring/spring-mvc.xml");
	 GsonObjectSerializer serializer = new GsonObjectSerializer();
	 RestRequestPathDispatcher dispatcher = new RestRequestPathDispatcher(serializer,ctx);
	 HttpServerInitializer init = new HttpServerInitializer(1,dispatcher);
	 HttpServer server = new HttpServer(init,port);
	 try {
	 server.run();
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
 *
 */
public class HttpServer {

	private final ChannelInitializer<SocketChannel> channelInitializer;

	private final int port;

	private final int tcpSoBacklog;

	public HttpServer(ChannelInitializer<SocketChannel> channelInitializer, int port) {
		this(channelInitializer, port, 256);
	}

	public HttpServer(ChannelInitializer<SocketChannel> channelInitializer, int port, int tcpSoBacklog) {
		super();
		this.channelInitializer = Preconditions.checkNotNull(channelInitializer);
		this.port = port;
		this.tcpSoBacklog = tcpSoBacklog;
	}

	public void setExceptionLogger(ExceptionLogger exceptionLogger) {
		ExceptionLoggerContext.register(exceptionLogger);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

	public void run() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("LightHttp服务器运行在端口: " + this.port);
		}
		// 初始化日记
		initExceptionLogger();
		// Boss线程组加工人线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, tcpSoBacklog);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(channelInitializer);

			Channel ch = b.bind(this.port).sync().channel();

			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	/**
	 * 初始化错误日记
	 */
	private void initExceptionLogger() {
		if (ExceptionLoggerContext.getExceptionLogger() == null) {
			ExceptionLoggerContext.register(new SimpleExceptionLogger());
		}
	}
}
