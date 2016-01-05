package com.anacleto.struts;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class MultipartResponse {

    HttpServletResponse res;
    ServletOutputStream out;
    boolean endedLastResponse = true;

    public MultipartResponse(HttpServletResponse response) throws IOException {
      // Save the response object and output stream
      res = response;
      out = res.getOutputStream();

      // Set things up
      res.setContentType("multipart/x-mixed-replace;boundary=End");
      out.println();
      out.println("--End");
    }

    public void startResponse(String contentType) throws IOException {
      // End the last response if necessary
      if (!endedLastResponse) {
        endResponse();
      }
      // Start the next one
      out.println("Content-Type: " + contentType);
      out.println();
      endedLastResponse = false;
    }

    public void endResponse() throws IOException {
      // End the last response, and flush so the client sees the content
      out.println();
      out.println("--End");
      out.flush();
      endedLastResponse = true;
    }

    public void finish() throws IOException {
      out.println("--End--");
      out.flush();
    }
  }