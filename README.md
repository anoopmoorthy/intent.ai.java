
**Intent.ai assignment solution**
https://github.com/intent-ai/engineering-interview

**Working**
1. Capture the bid requests generated by fake-ad exchange.
2. Store the bid requests in embedded redis cache in queue.
3. Generate random Ad Campaigns to match with the bid request
4. Match ad campaigns to bid request pulled from redis cache
5. Select winning bid on below mehtioned criteria and send to ui.
6. Statistical data is also generated and stored in embedded H2 Database, which shows the maximum, minimum, and average amounts per bid, total number of campaigns per bid are shown as response rate, also winning IAB categories are also counted

![](https://github.com/anoopmoorthy/intent.ai.java/blob/1a88dac218fcf6b302120995a6e43017457fcad7/screenshot.png)

Bid capture api : http://localhost:5758/big/request [POST]
Response api http://localhost:5758/bid/response [SSE Emitter]
Api to access ui http://localhost:5758/ui/live

**Bid matching criteria logic explanation**
In the data generated by the fake-ad exchange certain fields were observed to show randomness and the other fields were constant
Random fields: "banner", "placementId", "device", "lat" and "lon" fields in  "geo", "ip", "bcat"
My solution generates random Ad campaigns per request between 1 to 50 based on the above mentioned random fields and matching is done based on the following steps:
1. All campaigns which match the IAB are excluded
2. Matching banner dimentions and placement id are selected
3. Device types are matched
4. Maximum price bid from remaining is selected
