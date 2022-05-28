package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ServerStarter {
    public ServerStarter() {
        startServer();
    }


    private void startServer() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap() // to start and configure server
                    .group(group) //
                    .channel(NioServerSocketChannel.class) //
                    .localAddress(new InetSocketAddress("localhost", 8000)) // bind inetaddress
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new HelloServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind().sync(); // starting server
            System.out.println("Server started on port " + 8000);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        } finally {
            group.shutdownGracefully();
        }
    }
}
