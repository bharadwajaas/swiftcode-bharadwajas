package services;

import data.FeedResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class FeedService {

    public FeedResponse getFeedByQuery(String Query)

    {

        FeedResponse obj = new FeedResponse();
        try {
            WSRequest feedRequest = WS.url("https://news.google.com/news?q=rcb&output=rss");
            CompletionStage<WSResponse> responsePromise = feedRequest
                    .setQueryParameter("q", Query)
                    .setQueryParameter("output", "rss")
                    .get();
            Document response = responsePromise.thenApply(WSResponse::asXml).toCompletableFuture().get();
            Node Item = response.getFirstChild().getFirstChild().getChildNodes().item(10);
            obj.title = Item.getChildNodes().item(0).getFirstChild().getNodeValue();
            obj.pubDate = Item.getChildNodes().item(3).getFirstChild().getNodeValue();
            obj.description = Item.getChildNodes().item(4).getFirstChild().getNodeValue();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;


    }
}