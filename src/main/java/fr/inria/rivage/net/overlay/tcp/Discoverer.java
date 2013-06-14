/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2012 LORIA / Inria / SCORE Team
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
package fr.inria.rivage.net.overlay.tcp;

import fr.inria.rivage.net.overlay.IOverlay;
import fr.inria.rivage.tools.Configuration;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public final class Discoverer implements Runnable {

    int port = Configuration.getConfiguration().SERVER_PORT;
    //int port = 12333;
    static final int buffSize = 1024;
    String group = "255.255.255.255";
    DatagramSocket multi;
    Thread th;
    IOverlay tcp;
    String wellcome;
    String wellcomeSplited[];
    public static final Logger logger = Logger.getLogger(Class.class.getName());

    public Discoverer(TCPServerWithDiscover tcp) throws IOException {
        this(tcp.getMe().getName());
        this.tcp = tcp;

    }

    public Discoverer(String name) throws IOException {
        wellcome = "Hello CRDTGEditor v" + Configuration.VERSION_NUMBER + " my name is " + name;
        wellcomeSplited = wellcome.split(" ");

        logger.log(Level.INFO, "try to listen UDP {0}", port);
        multi = new DatagramSocket(port);

        //multi.joinGroup(InetAddress.getByName(group));
        th = new Thread(this);
        th.start();
        sendRalliment();
    }

    public void sendRalliment() throws IOException {
        DatagramPacket sendpack;
        try {
            sendpack = new DatagramPacket(wellcome.getBytes(), wellcome.length(), InetAddress.getByName(group), port);
            multi.send(sendpack);
            //DatagramPacket sendpack;
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error general broadCast {0}", ex);
        }
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface net = interfaces.nextElement();
            if (net.isLoopback() && !net.isUp()) {
                continue;
            }
            for (InterfaceAddress ine : net.getInterfaceAddresses()) {
                InetAddress broadCast = ine.getBroadcast();
                if (broadCast != null) {
                    try {
                        sendpack = new DatagramPacket(wellcome.getBytes(), wellcome.length(), broadCast, port);
                        multi.send(sendpack);
                    } catch (Exception ex) {
                        logger.log(Level.WARNING, "Error broadCast {0} with {1}", new Object[]{broadCast.getHostAddress(), ex});
                    }
                }
            }
        }
    }

    public void run() {
        try {
            byte buff[] = new byte[1024];
            DatagramPacket pack = new DatagramPacket(buff, buff.length);
            while (true) {

                multi.receive(pack);
                String name = getName(pack.getData());
                Arrays.fill(buff, (byte) ' ');
                if (name != null) {
                    logger.log(Level.INFO, "Welcome {0}  {1}:{2}", new Object[]{name, pack.getAddress().getHostAddress(), pack.getPort()});
                    if (tcp != null) { //for test
                        Computer c = new Computer(tcp, pack.getAddress(), pack.getPort(), name);
                        tcp.addMachine(c);
                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Discoverer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String getName(byte[] intro) {
        String recvSplit[] = new String(intro).split(" ");
        if (recvSplit.length != wellcomeSplited.length) {
            return null;
        }
        for (int p = 0; p < recvSplit.length - 1; p++) {
            if (!wellcomeSplited[p].equals(recvSplit[p])) {
                return null;
            }
        }

        if (recvSplit[2].equals("v" + Configuration.VERSION_NUMBER)) {
        }
        return recvSplit[recvSplit.length - 1].trim();
    }

    public static void main(String... arg) throws Exception {
        Discoverer disc = new Discoverer(InetAddress.getLocalHost().getHostName());
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        while (true) {

            bf.readLine();
            disc.sendRalliment();

        }
    }
}
