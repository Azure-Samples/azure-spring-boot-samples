// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.web.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *  A client creates an ssl socket to an HTTPS server.
 */
public class SampleClient {
  /**
  * The TLS protocols.
   */
  private static final String[] PROTOCOLS = new String[]{"TLSv1.3"};

  /**
   * The cipher suites.
   */
  private static final String[] CIPHER_SUITES = new String[]{"TLS_AES_128_GCM_SHA256"};

  /**
   * Main method.
   *
   * @param args  Command line arguments.
   * @throws Exception thrown out by main method.
   */
  public static void main(String[] args) throws Exception {
    SSLSocket sslSocket = null;
    PrintWriter printWriter = null;
    BufferedReader bufferedReader = null;

    try {
      SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
      sslSocket = (SSLSocket) factory.createSocket("localhost", 8443);
      sslSocket.setEnabledProtocols(PROTOCOLS);
      sslSocket.setEnabledCipherSuites(CIPHER_SUITES);
      sslSocket.startHandshake();
      printWriter = new PrintWriter(
                      new BufferedWriter(
                            new OutputStreamWriter(
                                    sslSocket.getOutputStream())));
      printWriter.println("GET / HTTP/1.0");
      printWriter.println();
      printWriter.flush();
      if (printWriter.checkError()) {
        System.out.println("SampleClient: error in PrintWriter");
      }
      bufferedReader = new BufferedReader(
                         new InputStreamReader(
                                 sslSocket.getInputStream()));

      String inputLine;
      while ((inputLine = bufferedReader.readLine()) != null) {
        System.out.println(inputLine);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (sslSocket != null) {
        sslSocket.close();
      }
      if (printWriter != null) {
        printWriter.close();
      }
      if (bufferedReader != null) {
        bufferedReader.close();
      }
    }
  }
}
