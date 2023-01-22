package legend.integration.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.LiveChatMessageListResponse;
import legend.integration.core.LoDPlays;
import legend.integration.core.PlayInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Flow;

public class YouTubePublisher implements LoDPlays {
    List<Flow.Subscriber<? super PlayInput>> subscriptions;
    private final Thread runner;
    private static final Logger LOGGER = LogManager.getFormatterLogger(YouTubePublisher.class);
    private static final String CLIENT_SECRETS = "client_secret.json";
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");
    private static final String APPLICATION_NAME = "LoDPlays";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public YouTubePublisher() {
        this.runner = new Thread(this);
    }

    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
//        InputStream in = YouTubePublisher.class.getResourceAsStream(CLIENT_SECRETS);
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader((in)));
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES).build();
//        Credential credential = new AuthorizationCodeInstallApp(flow, new LocaleServerReceiver()).authorize("user");
//
//        return credential;

        // TODO: java imports don't make any sense to me
        return null;
    }

    private static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super PlayInput> subscriber) {
        subscriptions.add(subscriber);
    }

    @Override
    public void run() {
        final String liveChatId = "";

        while (true) {
            try {
                YouTube service = getService();
                YouTube.LiveChatMessages.List request = service.liveChatMessages().list(liveChatId, Arrays.asList("snippet"));
                LiveChatMessageListResponse response = request.execute();
                System.out.println(response);

            } catch (Exception e) {
                LOGGER.error(e);
                return;
            }
        }
    }

    @Override
    public void play() {
        this.runner.start();
    }
}
