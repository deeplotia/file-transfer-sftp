package com.d3.file;

import com.jcraft.jsch.*;

public class SFTPFileTransfer {

    public static void main(String[] args) {

        final String REMOTE_HOST = args[0];
        final String USERNAME = args[1];
        final String PASSWORD = args[2];
        final int SESSION_TIMEOUT = 60000;
        final int CHANNEL_TIMEOUT = 30000;

        //String localFile = "C:/Deep/documents/sftp/local/TownhallReceipt.jpg";
        //String remoteFile = "C:/Deep/documents/sftp/remote/TownhallReceipt.jpg";

        String localFile = args[3];
        String remoteFile = args[4];

        Session jschSession = null;

        try {

            JSch jsch = new JSch();
            jsch.setKnownHosts(System.getProperty("user.home")+"/.ssh/known_hosts");
            jschSession = jsch.getSession(USERNAME, REMOTE_HOST);

            // authenticate using private key
            // jsch.addIdentity("/home/mkyong/.ssh/id_rsa");

            // authenticate using password
            jschSession.setPassword(PASSWORD);

            // 60 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 30 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;

            // transfer file from local to remote server
            channelSftp.put(localFile, remoteFile);

            // download file from remote server to local
            // channelSftp.get(remoteFile, localFile);

            channelSftp.exit();
            System.out.println("File Transfer Successful");
        } catch (JSchException | SftpException e) {

            e.printStackTrace();
            System.out.println("File Transfer Failed");
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }   
        }
    }
}
