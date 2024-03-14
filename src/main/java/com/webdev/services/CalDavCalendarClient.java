package com.webdev.services;

import com.webdev.enteties.CalDavCalendar;
import com.webdev.enteties.CalDavEvent;
import com.webdev.exceptions.NotFoundCalendarException;
import com.webdev.exceptions.SaveEventException;
import com.webdev.exceptions.UnauthorizedException;
import com.webdev.exceptions.XMLDataException;
import com.webdev.modules.Deserializer;
import com.webdev.modules.Serializer;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;

public class CalDavCalendarClient {

    private final String username;

    private final String password;

    private final URL calendarUri;

    private HttpClient client;

    private ZoneId zoneId;

    public CalDavCalendarClient(URL calendarUri,
                                String login,
                                String password) {

        this(calendarUri, login, password, ZoneId.systemDefault());
    }

    public CalDavCalendarClient(URL calendarUri,
                  String login,
                  String password,
                  ZoneId zoneId) {
        this.calendarUri = calendarUri;
        this.username = login;
        this.password = password;
        this.zoneId = zoneId;

        this.createHttpClient();
    }

    void createHttpClient(){
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials(this.username, this.password);
        provider.setCredentials(AuthScope.ANY, credentials);

        client = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(provider)
                .build();
    }

    public CalDavCalendar getCalendar() throws UnauthorizedException, NotFoundCalendarException, ClientProtocolException, XMLDataException, IOException {

        HttpPropfind HttpPropfind = new HttpPropfind(this.calendarUri.toString());

        HttpResponse response = client.execute(HttpPropfind);

        if (response.getStatusLine().getStatusCode() == 401) {
            throw new UnauthorizedException("Wrong login or password");
        }
        if (response.getStatusLine().getStatusCode() == 404) {
            throw new NotFoundCalendarException("Wrong url to calendar");
        }

        CalDavCalendar caldavCalendar = new CalDavCalendar();

        caldavCalendar.setCalendarUrl(this.calendarUri);
        caldavCalendar.setZoneId(this.zoneId);

        return Deserializer.deserializeCalendar(EntityUtils.toString(response.getEntity()), caldavCalendar);
    }

    public String saveEvent(CalDavEvent targetEvent) throws UnauthorizedException, NotFoundCalendarException, SaveEventException, ClientProtocolException, IOException{

        String eventAsString = Serializer.serializeEventToString(targetEvent, this.zoneId, this.username);
        String uri = this.getEventUri(targetEvent);

        HttpPut HttpPut = new HttpPut(uri);
        HttpPut.setEntity(new StringEntity(eventAsString, StandardCharsets.UTF_8));

        HttpResponse response = client.execute(HttpPut);

        if (response.getStatusLine().getStatusCode() == 401) {
            throw new UnauthorizedException("Wrong login or password");
        }
        if(response.getStatusLine().getStatusCode() == 404){
            throw new NotFoundCalendarException("Wrong url to calendar");
        }
        if (response.getStatusLine().getStatusCode() == 500) {
            throw new SaveEventException("Invalid request");
        }

        this.createHttpClient();

        return targetEvent.getUid();
    }

    public String updateEvent(String eventUid, CalDavEvent targetEvent) throws UnauthorizedException, NotFoundCalendarException, SaveEventException, ClientProtocolException, IOException{

        targetEvent.setUid(eventUid);

        String eventAsString = Serializer.serializeEventToString(targetEvent, this.zoneId, this.username);
        String uri = this.getEventUri(eventUid);

        HttpPut HttpPut = new HttpPut(uri);
        HttpPut.setEntity(new StringEntity(eventAsString, StandardCharsets.UTF_8));

        HttpResponse response = client.execute(HttpPut);

        if (response.getStatusLine().getStatusCode() == 401) {
            throw new UnauthorizedException("Wrong login or password");
        }
        if(response.getStatusLine().getStatusCode() == 404){
            throw new NotFoundCalendarException("Wrong url to calendar");
        }
        if (response.getStatusLine().getStatusCode() == 500) {
            throw new SaveEventException("Invalid request");
        }

        this.createHttpClient();

        return targetEvent.getUid();
    }

    public String removeEvent(String eventUid) throws UnauthorizedException, NotFoundCalendarException, SaveEventException, ClientProtocolException, IOException {

        String uri = this.getEventUri(eventUid);

        HttpDelete HttpDelete = new HttpDelete(uri);

        HttpResponse response = client.execute(HttpDelete);

        if (response.getStatusLine().getStatusCode() == 401) {
            throw new UnauthorizedException("Wrong login or password");
        }
        if(response.getStatusLine().getStatusCode() == 404){
            throw new NotFoundCalendarException("Wrong url to calendar");
        }
        if (response.getStatusLine().getStatusCode() == 500) {
            throw new SaveEventException("Invalid request");
        }

        return eventUid;
    }

    String getEventUri(CalDavEvent targetEvent)
    {
        return  this.calendarUri + targetEvent.getUid() + ".ics";
    }

    String getEventUri(String eventUid)
    {
        return  this.calendarUri + eventUid + ".ics";
    }
}
