package com.momo.api.base;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.HttpMethod;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.HttpErrorResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * *
 * Class HttpConnection
 */
public abstract class HttpConnection {

    protected HttpConfiguration config;
    protected HttpURLConnection connection;

    /**
     * *
     * Default constructor
     */
    public HttpConnection() {
    }

    /**
     * *
     * Configure HTTP connection before process
     *
     * @param clientConfiguration
     * @throws IOException
     */
    public abstract void createAndConfigureHttpConnection(HttpConfiguration clientConfiguration) throws IOException;

    /**
     * *
     *
     * @param url
     * @param payload
     * @param headers
     * @return
     * @throws IOException
     * @throws MoMoException
     */
    public HttpResponse execute(String url, String payload, Map<String, String> headers)
            throws IOException, MoMoException {
        BufferedReader reader = null;
        HttpResponse result = null;
        try {
            result = executeWithStream(url, payload, headers);

            if (result == null) {
                throw new MoMoException(
                        new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                                Constants.GENERIC_ERROR_CODE).errorDescription(Constants.NULL_VALUE_ERROR).build());
            } else if (result.getPayLoad() instanceof InputStream) {
                reader = new BufferedReader(
                        new InputStreamReader((InputStream) result.getPayLoad(), Constants.ENCODING_FORMAT));
                result.setPayLoad(read(reader));
            } else {
                result.setPayLoad(null);
            }
//        } catch (IOException e) {
//            //TODO MoMoException thrown. Need to verified.
//            throw new MoMoException("An error occurred while reading payload from response object", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return result;
    }

    /**
     * *
     *
     * @param url
     * @param payload
     * @param headers
     * @return
     * @throws ProtocolException
     * @throws MoMoException
     */
    public HttpResponse executeWithStream(String url, String payload, Map<String, String> headers)
            throws ProtocolException, MoMoException {
        HttpResponse requestResponse = null;
        OutputStreamWriter writer = null;

        this.overrideRequestMethod();

        String referenceId = headers.get(Constants.X_REFERENCE_ID);

        if (payload != null) {
            this.connection.setRequestProperty(Constants.HTTP_CONTENT_LENGTH, String.valueOf(payload.trim().length()));
        }

        try {
            setHttpHeaders(headers);

            //TODO remove after testing
//            Map<String, String> headers2 =  headers.entrySet().stream()
//                    .filter(m->m.getKey().equals("X-Callback-Url"))
//                    .collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
//            
////                    .map(k->k.getValue())
////                    .collect(Collectors.f)
////                    ;
//            System.out.println("X-Callback-Url::::"+url);
//            headers2.forEach((k,v)->System.out.println(k+" : "+v));

            int retry = 0;
            retryLoop:
            do {

                if (Arrays.asList("POST", "PUT", "PATCH").contains(this.config.getHttpMethod().toUpperCase())) {
                    writer = new OutputStreamWriter(this.connection.getOutputStream(),
                            Charset.forName(Constants.ENCODING_FORMAT));
                    if (payload != null) {
                        writer.write(payload);
                    }
                    writer.flush();
                }

                int responseCode = this.connection.getResponseCode();
                //TODO remove after testing
                String responseMessage = this.connection.getResponseMessage();
                System.out.println(responseCode + " : " + responseMessage);
                
                HttpStatusCode httpStatus = HttpStatusCode.getHttpStatus(responseCode);

//                try {
                    switch (httpStatus) {
                        case OK:
                        case CREATED:
                        case ACCEPTED:
                            requestResponse = HttpResponse.createResponse(this.connection.getInputStream(), true,
                                    httpStatus, this.connection.getHeaderFields(), referenceId);
                            break;
                        case BAD_REQUEST:
                        case UNAUTHORIZED:
                        case NOT_FOUND:
                        case INTERNAL_SERVER_ERROR:
                        case SERVICE_UNAVAILABLE:
                        case TOO_MANY_REQUESTS:
                        case CONFLICT:
                            //TODO remove after testing
                            System.out.println("url::::::"+url);
                            requestResponse = HttpResponse.createResponse(this.connection.getErrorStream(), false,
                                    httpStatus, this.connection.getHeaderFields(), referenceId);
                            break;
                        default:
                            //TODO remove after testing
                            System.out.println("************:::::::::::HttpStatus (default case): "+httpStatus);
                            requestResponse = HttpResponse.createResponse(this.connection.getErrorStream(), false,
                                    HttpStatusCode.BAD_REQUEST, this.connection.getHeaderFields(), referenceId);
                            break;
                    }
//                } catch (IOException e) {
//                    //TODO MoMoException thrown. Need to verified.
//                    throw new MoMoException("An error occurred while creating an HttpResponse object from the response received", e);
//                    // requestResponse = HttpResponse.createResponse(null, false,
//                    // HttpStatusCode.BAD_REQUEST);
//                }
                break retryLoop;
            } while (retry < this.config.getMaxRetry());
        } catch (IOException e) {
            //TODO MoMoException thrown. Need to verified.
//            throw new MoMoException("An error occurred while creating the request", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    //TODO MoMoException thrown. Need to verified.
//                    throw new MoMoException("An error occurred while completing the request", e);
                } finally {
                    writer = null;
                }
            }
        }
        return requestResponse;
    }

    /**
     * *
     *
     * @param headers
     */
    protected void setHttpHeaders(Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            Iterator<Map.Entry<String, String>> itr = headers.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> pairs = itr.next();
                String key = pairs.getKey();
                String value = pairs.getValue();
                if (value != null) {
                    this.connection.setRequestProperty(key, value);
                }
            }
        }
    }

    /**
     * *
     *
     * @param reader
     * @return
     * @throws IOException
     * @throws MoMoException
     */
    protected String read(BufferedReader reader) throws IOException, MoMoException {
        String inputLine;
        StringBuilder response = new StringBuilder();

        try {
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException ioe) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(ioe.getMessage()).build());
        } catch (Exception e) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.GENRAL_ERROR).build());
        }

        return response.toString();
    }

    /**
     * *
     *
     * @throws ProtocolException
     */
    private void overrideRequestMethod() throws ProtocolException {
        if (this.config.getHttpMethod().equals(HttpMethod.PATCH.toString())) {
            this.connection.setRequestProperty(Constants.HTTP_OVERRIDE_METHOD, HttpMethod.PATCH.toString());
            this.connection.setRequestMethod(HttpMethod.POST.toString());
        } else {
            this.connection.setRequestMethod(this.config.getHttpMethod());
        }
    }
}
