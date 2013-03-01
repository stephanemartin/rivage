/*
 * Created on Jul 19, 2004
 */
package fr.inria.rivage.net.group;

/*import com.inria.geditorserver.network.packets.DataPacket;
import com.inria.geditorserver.network.packets.FileTransportPacket;
import com.inria.geditorserver.network.packets.MessageID;
import com.inria.geditorserver.network.packets.Packet;*/

import fr.inria.rivage.engine.manager.FileController;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Yves
 */
public final class OTServerNetwork/* implements GroupController*/{

	private InetAddress serverAddress;
	private int port;

	public static final int DATA_FILES = 0x000001;
	public static final int DOCUMENT_FILES = 0x000002;

	public OTServerNetwork(String serverAddress, int port) throws UnknownHostException, IOException,
			ClassNotFoundException {
		this(InetAddress.getByName(serverAddress), port);
	}

	public OTServerNetwork(InetAddress serverAddress, int port) throws IOException,
			ClassNotFoundException {
		this.serverAddress = serverAddress;
		this.port = port;

		testConnection();
	}

	private void testConnection() throws IOException, ClassNotFoundException {
		/*Socket socket = new Socket(serverAddress, port);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Packet p = new Packet(MessageID.IS_OK);
		oos.writeObject(p);
		oos.flush();
		p = (Packet) ois.readObject();
		if (p.getMessage() != MessageID.OK)
			throw new ConnectException("The server is not responding as expected.");
                        * 
                        */
            System.out.println("Oops Connectiontest");
        }

	/**
	 * Retrieves an array containing the filenames as string.
	 * 
	 * @param filetype
	 *            either DOCUMENT_FILES or DATA_FILES
	 * @return a list containing the filenames.
	 */
	public String[] getFileList(int filetype) throws IOException, ClassNotFoundException {
		/*Socket socket = new Socket(serverAddress, port);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Packet p;
		if (filetype == DOCUMENT_FILES)
			p = new Packet(MessageID.GET_DOC_LIST);
		else
			p = new Packet(MessageID.GET_IMAGE_LIST);
		oos.writeObject(p);
		oos.flush();
		return (String[]) ois.readObject();*/
            return new String[]{"toto","titi"};
	}
	
	public boolean saveFile(int filetype, String filename, byte[] content) throws IOException, ClassNotFoundException{
		/*
            Socket socket = new Socket(serverAddress, port);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		
		FileTransportPacket p;
		if (filetype == DOCUMENT_FILES)
			p = new FileTransportPacket(MessageID.SAVE_FILE_DOC,content,filename);
		else
			p = new FileTransportPacket(MessageID.SAVE_FILE_IMAGE,content,filename);
		
		oos.writeObject(p);
		oos.flush();
		return ((Packet)ois.readObject()).getMessage()==MessageID.OK;*/
            System.out.println("Oops saveFile "+filename+" "+filetype);
            return true;
	}
	
    
	public void saveFile(int filetype, String filename, String content) throws IOException, ClassNotFoundException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		pw.print(content);
		pw.close();
		saveFile(filetype, filename, baos.toByteArray());
	}
	
    
	public byte[] loadFileAsByteArray(int filetype, String filename) throws IOException, ClassNotFoundException{
		/*byte[] buffer = null;
		
		Socket socket = new Socket(serverAddress, port);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		
		DataPacket p = null;
		
		if (filetype == DOCUMENT_FILES)
			p = new DataPacket(MessageID.GET_DOC,filename);
		else
			p = new DataPacket(MessageID.GET_IMAGE,filename);
		
		oos.writeObject(p);		
		oos.flush();
		buffer = (byte[]) ((DataPacket) ois.readObject()).getData();
		*/
            System.out.println("Oops loadFileAsByteArray"+filetype+" "+filename);
		return null;
	}
	
	public String loadFileAsString(int filetype, String filename) throws IOException, ClassNotFoundException{
		String r = "";
		byte[] buffer = loadFileAsByteArray(filetype,filename);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		BufferedReader br = new BufferedReader(new InputStreamReader(bais));
		
		String tmp = br.readLine();
		while(tmp!=null){
			r += tmp;
			tmp = br.readLine();
		}
		
		return r;
	}
	
	/**
	 * The only possible type here is the DOCUMENT_FILES, so no need to specify a certain type.
	 * @param filename the name of the file to be created.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public boolean createNewFile(String filename) throws IOException, ClassNotFoundException {
		Socket socket = new Socket(serverAddress, port);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		
		//oos.writeObject(new DataPacket(MessageID.CREATE_NEW_DOC,filename));
                oos.flush();
		
		//return  ((Packet)ois.readObject()).getMessage()==MessageID.OK;
                return true;
            
		
	}

    public List<FileController> getFileList() throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void synchroniseFC(FileController fc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean registerNewFile(FileController fc) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void hasMessage(Message mess) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}