package pl.pjatk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessengerTest {

    private static Messenger messenger;

    @BeforeAll
    static void setUp() {

    }

    @Test
    void test_send_message_successful() {
        // given
        TemplateEngine templateEngine = mock(TemplateEngine.class);
        MailServer mailServer = mock(MailServer.class);
        messenger = new Messenger(mailServer, templateEngine);

        String msgContent = "from: me, to: you";
        when(templateEngine.prepareMessage(any(),any())).thenReturn(msgContent);
        doNothing().when(mailServer).send(anyString(), eq(msgContent));

        Client client = new Client();
        client.setEmail("email@example.com");
        Template template = new Template();
        // when
        messenger.sendMessage(client, template);
        // then
        verify(mailServer, times(1)).send(client.getEmail(), msgContent);
    }

    @Test
    void test_send_message_successful_spy() {
        // given
        TemplateEngine templateEngineSpied = Mockito.spy(TemplateEngine.class);
        MailServer mailServerSpied = Mockito.spy(MailServer.class);
        messenger = new Messenger(mailServerSpied, templateEngineSpied);

        String msgContent = "from: me, to: you";
        when(templateEngineSpied.prepareMessage(any(),any())).thenReturn(msgContent);
        doNothing().when(mailServerSpied).send(anyString(), eq(msgContent));

        Client client = new Client();
        client.setEmail("email@example.com");
        Template template = new Template();

        // when
        messenger.sendMessage(client, template);

        // then
        verify(mailServerSpied, times(1)).send(client.getEmail(), msgContent);
    }

    @Captor
    ArgumentCaptor<String> argCaptor;

    @Test
    public void test_send_message_successful_captor() {
        // given
        TemplateEngine templateEngine = mock(TemplateEngine.class);
        MailServer mailServer = mock(MailServer.class);
        Messenger messenger = new Messenger(mailServer, templateEngine);
        argCaptor = ArgumentCaptor.forClass(String.class);

        String msgContent = "from: me, to: you";
        when(templateEngine.prepareMessage(any(), any())).thenReturn(msgContent);
        doNothing().when(mailServer).send(anyString(), eq(msgContent));

        Client client = new Client();
        client.setEmail("email@example.com");
        Template template = new Template();

        // when
        messenger.sendMessage(client, template);

        // then
        verify(mailServer).send(eq(client.getEmail()), argCaptor.capture());
        assertEquals(msgContent, argCaptor.getValue());
    }

    @Mock
    private MailServer mailServer;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private Messenger messengerInjectMocks;

    @Test
    public void test_send_message_successful_injectmocks() {
        // given
        String msgContent = "from: me, to: you";
        when(templateEngine.prepareMessage(any(), any())).thenReturn(msgContent);
        doNothing().when(mailServer).send(anyString(), eq(msgContent));

        Client client = new Client();
        client.setEmail("email@example.com");
        Template template = new Template();

        // when
        messengerInjectMocks.sendMessage(client, template);

        // then
        verify(mailServer).send(eq(client.getEmail()), eq(msgContent));
    }
}