package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class ClientStarter {

    private static boolean loginSuccess = false;


    public ClientStarter() {
        run();
    }

    private void run() {
        getFuture();
    }

    private void getFuture() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap clientBootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", 8000))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());

                            pipeline.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture channelFuture = clientBootstrap.connect("localhost", 8000).sync();
            Scanner scanner = new Scanner(System.in);

            auth(channelFuture.channel());

            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                Channel channel = channelFuture.channel();
                channel.writeAndFlush(msg);
                channel.flush();
            }
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }

    private void auth(Channel channel) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please login!");
        while (!loginSuccess) {
            String command = scanner.nextLine();
            channel.writeAndFlush(command);
            channel.flush();
        }
    }

    public static void setLoginSuccess(boolean loginSuccess) {
        ClientStarter.loginSuccess = loginSuccess;
    }
}
