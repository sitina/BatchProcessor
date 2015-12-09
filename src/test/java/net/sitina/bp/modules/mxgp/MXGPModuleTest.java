package net.sitina.bp.modules.mxgp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MXGPModuleTest {

    private final String USER_AGENT = "Mozilla/5.0";

    @Test
    public void simpleGetWorksLikeACharm() throws Exception {
        String url = "http://www.google.com/search?q=httpClient";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

        assertNotNull(result.toString());
    }

    @Test
    public void mxgpGetWorksLikeACharm() throws Exception {
        String url = "http://results.mxgp.com/reslists.aspx";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "" +
                "__EVENTTARGET=SelectResult" +
                "&__EVENTARGUMENT=" +
                "&__LASTFOCUS=" +
                "&__VIEWSTATE=%2FwEPDwUKMTc3NTUwNzEyOA9kFgICAw9kFgxmDxBkEBUMBDIwMTUEMjAxNAQyMDEzBDIwMTIEMjAxMQQyMDEwBDIwMDkEMjAwOAQyMDA3BDIwMDYEMjAwNQQyMDA0FQwEMjAxNQQyMDE0BDIwMTMEMjAxMgQyMDExBDIwMTAEMjAwOQQyMDA4BDIwMDcEMjAwNgQyMDA1BDIwMDQUKwMMZ2dnZ2dnZ2dnZ2dnFgFmZAIBDxBkEBUMA1NSRSBGSU0gTW90b2Nyb3NzIFdvcmxkIENoYW1waW9uc2hpcB9GSU0gV29tZW4ncyBTbm93Y3Jvc3MgV29ybGQgQ3VwGEZJTSBNb3RvY3Jvc3Mgb2YgTmF0aW9ucwlMb3NhaWwgTVgoRklNIFdvbWVuJ3MgTW90b2Nyb3NzIFdvcmxkIENoYW1waW9uc2hpcCBGSU0gVmV0ZXJhbnMgTW90b2Nyb3NzIFdvcmxkIEN1cCVXb21lbidzIE1vdG9jcm9zcyBvZiBFdXJvcGVhbiBOYXRpb25zH01vdG9jcm9zcyBFdXJvcGVhbiBDaGFtcGlvbnNoaXAnRklNIEp1bmlvciBNb3RvY3Jvc3MgV29ybGQgQ2hhbXBpb25zaGlwIEZJTSBTbm93Y3Jvc3MgV29ybGQgQ2hhbXBpb25zaGlwHU1vdG9jcm9zcyBvZiBFdXJvcGVhbiBOYXRpb25zFQwCMzUBMQI0MQE3AjM4AjE5AjExAjM3ATYCMTYCMTQBOBQrAwxnZ2dnZ2dnZ2dnZ2cWAQIBZAICDxBkEBUCBE1YR1ADTVgyFQIBOQE3FCsDAmdnFgFmZAIDDxBkEBUSDFJvdW5kIDE4LVVTQQ1Sb3VuZCAxNy1MZW9uGFJvdW5kIDE2LVRoZSBOZXRoZXJsYW5kcxhSb3VuZCAxNS1Mb21iYXJkaWEtSXRhbHkQUm91bmQgMTQtQmVsZ2l1bRdSb3VuZCAxMy1DemVjaCBSZXB1YmxpYw9Sb3VuZCAxMi1MYXR2aWEPUm91bmQgMTEtU3dlZGVuEFJvdW5kIDEwLUdlcm1hbnkNUm91bmQgOS1JdGFseQ5Sb3VuZCA4LUZyYW5jZRVSb3VuZCA3LUdyZWF0IEJyaXRhaW4NUm91bmQgNi1TcGFpbg5Sb3VuZCA1LUV1cm9wZRBSb3VuZCA0LVRyZW50aW5vG1JvdW5kIDMtUGF0YWdvbmlhIEFyZ2VudGluYRBSb3VuZCAyLVRoYWlsYW5kDVJvdW5kIDEtUWF0YXIVEgQzMDE4BDMwMTcEMzAxNgQzMDE1BDMwMTQEMzAxMwQzMDEyBDMwMTEEMzAxMAQzMDA5BDMwMDgEMzAwNwQzMDA2BDMwMDUEMzAwNAQzMDAzBDMwMDIEMzAwMRQrAxJnZ2dnZ2dnZ2dnZ2dnZ2dnZ2cWAQIFZAIEDxBkEBUGBlJhY2UgMgZSYWNlIDEHV2FybS11cA9RdWFsaWZ5aW5nIFJhY2UNVGltZSBQcmFjdGljZQ1GcmVlIFByYWN0aWNlFQYDMzUyAzM1MQMzNDEDMzIzAzMxMQMzMDUUKwMGZ2dnZ2dnFgFmZAIFDxBkEBUGDkNsYXNzaWZpY2F0aW9uCUxhcCBDaGFydAhBbmFseXNpcxZPdmVyYWxsIENsYXNzaWZpY2F0aW9uG0NoYW1waW9uc2hpcCBDbGFzc2lmaWNhdGlvbhxNYW51ZmFjdHVyZXJzIENsYXNzaWZpY2F0aW9uFQYBMQEyATMBNAE1ATYUKwMGZ2dnZ2dnFgFmZGT0rkzHOyKSAH5UQB4IRcMnNbcB8y6lWys0%2FTfMe3Kahg%3D%3D" +
                "&__VIEWSTATEGENERATOR=EC1AA293" +
                "&__EVENTVALIDATION=%2FwEdAD%2BZFUPRAG92CXv8Gye5DihJ7JjQ%2Biju7WJgotKMEiAtq5wXHe8nGPqTPhdGDDpww4om1pTft5SryJgRynns49BJyiT5L1aIKQ1dhKw7z8Cb%2FRSCYudc2E8gycPb0fFed87nULUnmk%2FftZIkSzMGQSZhmNJFLBT9cCrN%2B1nxVzWJ4abMtHyP62uAkWMStD4gsRIK0j82%2FGphz9RIF%2Brl7Jo%2Fa9USQ88uIfqeYPXsuRbCWA5CxmagTftjr6mLlnS%2FF9BHP%2FhXva74YTYqFk7Facvc6KCX9xWEc1kbFXOZCqgUzptWjVuXEhRtxHxKgBuOrn9ElTHDwNXvW9EIVY6BFfSJq%2FvmY2gU18acPHTYCkO8dkHy5U4HYDdp1QiB5HSUn6XQh6QFYQcvzRvPMNm%2B4EDeQcoRQVgJAUzwCL49idEYtWUCVTpGxW7tCCXtNJhu8uTYDHQHG5LZZHRq2fREXD68x31qOmggecbE8XlS19Bvv0dO1k1iLCt3DX6vW1qnjct9ZNUPvtuyZdFukv%2Bb5F%2Bi%2F2WMC%2FpKPfEW1jgCIQ3mGKAHY9MZ4ktl3kQesfnQbbWrgO7%2FDFSoDT5KtHdHdT8H3slbL8C801QVSspFVkJEAw47OCxO9gnxClPLmhKexS2%2BGPHIwupL3wfnk6IJ1ijwI9E0ztezJTdfsHRM4xizafEy7SUsbWluIIpRXSMzV%2Fu1WxhF2puvAb6U90P7u%2FYZbLVFN0MG5%2BNs%2FvaNQxHL0Av6eeQcmrQ%2B4fVTh9I3pJdhs58WlB6Z3fzTpAY349zE4QB0Fd1cDKBJjNXO84AXf4kTQL4joMymiKeBHOy1qZwWI78f0iPKzg6W%2BixclTvxOiE51726K6n8nK6KXXHborfGLNPAaavchGIcL84fBBiw%2BtRP1zxk4zfRAJXoi65DYHDigIqjj1WEmlYgh069mSr7U%2FFfjQhKj%2Frg7RZ9d%2FbZAHExsoJjA8v4G8Od9xFVW7Ch0jReJVYVXafjHRcWN5VmRCq%2FulbAOEoip4dXnRRfRFy0nePDwAsX%2BxjMgfRdvMahqWgT%2BUjfEEg7r4zY7Ne0LWVJOcgIPnz2DUuTkO7Lg5ynAYOVOjngyk4zO3A70sCuoI9cAJWSQ0N3cjMYIsqWsQbEfAhhAExzC3A38RZ0bzoPF6k8eFxBA9lACFEdBC3%2BBm3%2Fcrr4AoBwyufGbYFwyqHWx8DjI2P5qp4nUgspxZBFXqzLx3z5544KX1PQurYCK3KprFVMzLg63wiEV5BClIT%2B6m37mc07mCUcvBrkX3xg1rR6VqJ2DveW7HNTVHq%2B2zll1bMyPyBX4cnMgLRqp%2FM2orEREmYgRsCv%2FXEcmrl1kV0Nq3KUw5O%2Bky29Mt33UEJxFvIhwTBXFycXzw%3D%3D" +
                "&SelectYear=2015" +
                "&SelectCShip=1" +
                "&SelectClass=9" +
                "&SelectEvent=3013" +
                "&SelectRace=352" +
                "&SelectResult=5";

        HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

        assertNotNull(result.toString());
    }


}