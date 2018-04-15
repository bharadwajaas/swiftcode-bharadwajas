package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FeedResponse;
import data.Message;
import data.NewsAgentResponse;
import services.FeedService;
import services.NewsAgentService;

import java.util.UUID;

public class MessageActor extends UntypedActor {

    //object of feed service
    //object of newsagent service
    public static Props props(ActorRef out) {
        return Props.create(MessageActor.class, out);
    }

    public MessageActor(ActorRef out) {
        this.out = out;
    }

    private final ActorRef out;
    public FeedService feedService = new FeedService();
    private NewsAgentService newsAgentService = new NewsAgentService();
    NewsAgentResponse newsAgentResponse = new NewsAgentResponse();
    FeedResponse feedResponse = new FeedResponse();

    @Override
    public void onReceive(Object message) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        Message messageObject = new Message();
        if (message instanceof String) {

            messageObject.text = (String) message;
            messageObject.sender = Message.Sender.USER;
            out.tell(objectMapper.writeValueAsString(messageObject), self());
            newsAgentResponse = newsAgentService.getNewsAgentResponse(messageObject.text, UUID.randomUUID());
            feedResponse = feedService.getFeedByQuery(newsAgentResponse.query);
            messageObject.text = (feedResponse.title == null) ? "No results found" : "Showing results for: " + newsAgentResponse.query;
            messageObject.feedResponse = feedResponse;
            messageObject.sender = Message.Sender.BOT;
            out.tell(objectMapper.writeValueAsString(messageObject), self());


        }


    }

}