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
import java.util.TimeZone;

public class CalDavCalendarClient {

    private final String username;

    private final String password;

    private final URL calendarUri;

    private HttpClient client;

    private TimeZone timeZone;

    public CalDavCalendarClient(URL calendarUri,
                                String login,
                                String password) {

        this(calendarUri, login, password, TimeZone.getDefault());
    }

    public CalDavCalendarClient(URL calendarUri,
                  String login,
                  String password,
                  TimeZone timeZone) {
        this.calendarUri = calendarUri;
        this.username = login;
        this.password = password;
        this.timeZone = timeZone;

        this.CreateHttpClient();
    }

    void CreateHttpClient(){
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials(this.username, this.password);
        provider.setCredentials(AuthScope.ANY, credentials);

        client = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(provider)
                .build();
    }

    public CalDavCalendar GetCalendar() throws UnauthorizedException, NotFoundCalendarException, ClientProtocolException, XMLDataException, IOException {

            var HttpPropfind = new HttpPropfind(this.calendarUri.toString());

            HttpResponse response = client.execute(HttpPropfind);

            if (response.getStatusLine().getStatusCode() == 401) {
                throw new UnauthorizedException("Wrong login or password");
            }
            if(response.getStatusLine().getStatusCode() == 404){
                throw new NotFoundCalendarException("Wrong url to calendar");
            }

            var caldavCalendar = new CalDavCalendar();

            caldavCalendar.setCalendarUrl(this.calendarUri);
            caldavCalendar.setTimeZone(this.timeZone);

            return Deserializer.DeserializeCalendar(EntityUtils.toString(response.getEntity()), caldavCalendar);
    }

    public String SaveEvent(CalDavEvent targetEvent) throws SaveEventException, ClientProtocolException, IOException{

        var eventAsString = Serializer.SerializeEventToString(targetEvent, this.timeZone, this.username);
        var uri = this.GetEventUri(targetEvent);

        var HttpPut = new HttpPut(uri);
        HttpPut.setEntity(new StringEntity(eventAsString, StandardCharsets.UTF_8));

        HttpResponse response = client.execute(HttpPut);

        if (response.getStatusLine().getStatusCode() == 500) {
            throw new SaveEventException("Invalid request");
        }

        this.CreateHttpClient();

        return targetEvent.getUid();
    }

    public String RemoveEvent(String eventUid) throws SaveEventException, ClientProtocolException, IOException {

        var uri = this.GetEventUri(eventUid);

        var HttpDelete = new HttpDelete(uri);

        HttpResponse response = client.execute(HttpDelete);

        if (response.getStatusLine().getStatusCode() == 500) {
            throw new SaveEventException("Invalid request");
        }

        return eventUid;
    }

    String GetEventUri(CalDavEvent targetEvent)
    {
        return  this.calendarUri + targetEvent.getUid() + ".ics";
    }

    String GetEventUri(String eventUid)
    {
        return  this.calendarUri + eventUid + ".ics";
    }
}
