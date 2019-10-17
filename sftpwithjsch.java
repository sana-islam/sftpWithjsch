package sftpexample;

import java.io.File;
import java.io.FileInputStream;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 *
 * @author sana.islam
 */
public class SftpExample {

    private String host="hostIP";
    private Integer port=22;
    private String user="sftpuser";
    private String password="pass";
    public String hostname;
    public String date_mod;

    private JSch jsch;
    private Session session;
    private Channel channel;
    private ChannelSftp sftpChannel;


     public SftpExample(String remoteDir) throws UnknownHostException {
                  hostname=getHostname();
                  Date date= new Date();
                  date_mod = date.toString();


         String  date_format=String.format("%s%tY%<tm%<td%<tH","", date);
        // String fileName ="C:\\\\Users\\\\sana.islamold\\\\Desktop\\\\VMO\\\\vmo_documents\\\\atlas_user.txt";
         String fileName="/home/ngvs/testScript/dump_files/"+hostname+"_"+date_format+".csv";

                System.out.println(fileName);

                 //upload Upload = new upload("C:\\Users\\sana.islamold\\Desktop\\VMO\\vmo_documents\\atlas_user.txt");

                 FileInputStream fis = null;
		connect();
		try {
			// Change to output directory
			sftpChannel.cd(remoteDir);

			// Upload file
			File file = new File(fileName);
			fis = new FileInputStream(file);
                        System.out.println(file);
			sftpChannel.put(fis, file.getName());

			fis.close();
			System.out.println("File uploaded successfully - "+ file.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();

	}

     public String getHostname() throws UnknownHostException{

         InetAddress ip;
         ip = InetAddress.getLocalHost();
         hostname = ip.getHostName();
         return hostname;
     }

     public void connect() {

		System.out.println("connecting..."+host);
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host,port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

		} catch (JSchException e) {
			e.printStackTrace();
		}

	}

     public void disconnect() {
		System.out.println("disconnecting...");
		sftpChannel.disconnect();
		channel.disconnect();
		session.disconnect();
	}

     public static void main(String[] args) throws UnknownHostException {
        SftpExample sftp = new SftpExample("/home/sftpuser/Voucher_Server/Dump");

    }

  }
