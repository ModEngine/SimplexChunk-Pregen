/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package noisework;

import java.awt.Point;
import java.io.IOException;
import net.nexustools.concurrent.PropList;
import net.nexustools.io.net.PacketRegistry;
import net.nexustools.io.net.Server.Protocol;
import net.nexustools.net.work.WorkAppDelegate;
import net.nexustools.net.work.WorkClient;
import net.nexustools.net.work.WorkPacket;
import net.nexustools.net.work.WorkServer;
import net.nexustools.utils.Pair;

/**
 *
 * @author aero
 */
public class NoiseWorkDelegate extends WorkAppDelegate {

    PropList<Point> work = new PropList<Point>() {
        {
            for(int x = -64; x <64; x++){
                for(int y=  -64; y < 64; y++){
                    push(new Point(x,y));
                }
            }
        }
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new NoiseWorkDelegate(args);
    }

    public NoiseWorkDelegate(String[] args) {
        super(args, "NoiseWork", "NexusTools");
    }

    @Override
    protected WorkClient createClient(String host, int port) throws IOException {
        return new NoiseClient(name + "-NoiseClient", host, port, Protocol.TCP, packetRegistry);
    }

    @Override
    protected WorkClient createClient(Pair socket, WorkServer server) throws IOException {
        return new NoiseClient(name + "-NoiseClient", socket, server);
    }

    @Override
    protected void populate(PacketRegistry registery) {
        try {
            registery.register(NoiseWork.class);
            registery.register(WorkResponse.class);
        } catch (NoSuchMethodException ex) {}
    }

    @Override
    public WorkPacket nextWork(WorkClient workClient) {
        NoiseClient client = (NoiseClient)workClient;
        NoiseWork noisePacket = new NoiseWork();
        noisePacket.finishedTracks = 2;
        Point wp = work.pop();
        noisePacket.trackx = (short) wp.getX();
        noisePacket.trackz = (short) wp.getY();
        
        return noisePacket;
    }
    
}