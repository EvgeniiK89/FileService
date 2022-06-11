package server;

import commands.Command;
import commands.CommandResolver;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HelloServerHandler extends SimpleChannelInboundHandler<String> {

    private List<Channel> clients = new ArrayList<>();
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected!");
        clients.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Client send message = " + s);
        try {
            Command command = CommandResolver.resolveCommand(s.split(" ")[0]);
            for (Channel client : clients) {
                client.writeAndFlush("-> " + command.executeCommand(s.substring(s.indexOf(' '))) + "\n");
            }
        } catch (RuntimeException e) {
            for (Channel client : clients) {
                client.writeAndFlush("-> " + e.getMessage() + "\n");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
