# Big Data Techniques, Technologies and Trends
Lecturer Dr. Radu Tudoran<br>
<a href="mailto:Radu.Tudoran@huawei.com">Radu.Tudoran@huawei.com</a>

## Project
Sebastian Meyer (2280815)<br>
<a href="mailto:s.meyer@uni-duesseldorf.de">s.meyer@uni-duesseldorf.de

### Description
The project uses Twitter as a streaming source. The stream utilizes a custom endpoint (/1.1/statuses/filter.json)
to limit the data received from twitter. This allows us to retrieve only data containing the wanted
hashtags (cat, dog, fish, snake, bird). But since not only the hashtags are evaluated by Twitter there
might be some tweets not containing any of the defined hashtags.

The Stream, which emits Json-Strings is then parsed into Tweet-Objects.
On the Tweet-Objects we perform 5 different metrics for every hashtag.

1. Count of tweets every 10 minutes
2. Average number of follower every 10 minutes 
3. Average Count of Tweets from a various number of countries using sliding window (15, 5)
4. Most followed verified user
5. Sum of the users friends every 10 minutes

Additionally we make the following 3 predictions using a regressionModel:

1. predict if a user is verified based on his follower count
2. predict the follower count based on the number of hashtags the tweet has
3. predict the number of tweets based on the language

### Interesting Classes to look at
- TwitterStream.java<br>
  - Responsible for the stream processing
- TwitterBatch.java
  - Responsible for the batch processing
- Tweet.java
  - The POJO for the Tweet uses Annotations for the JSON parsing
- Lang.java
  - Allows the mapping between BCP47 code (Language) and the location
- Auth.java
  - Contains the Twitter Key configuration
- de.hhu.bigdata.processing.batch.reduce.*
  - Contains the classes for the batch processing implementing GroupReduceFunctions
- de.hhu.bigdata.processing.stream.apply.*
  - Contains the classes for the stream processing implementing WindowFunctions
- de.hhu.bigdata.prediction.*
  - Contains the prediction models
- de.hhu.bigdata.elasticsearch.*
  - Contains the classes needed to sink the data into an elasticsearch index

### Program Arguments
-collect [OUTPUT-FILE]<br>
If no file is configured "tweets_[TIMESTAMP]" will be used<br>
If using -collect you can not use stream or batch processing

-batch [INPUT_FILE]<br>
Performs batch processing on the given file<br>
The results will be print to console or to file "flink-app.log"<br>
The result of the batch processing can be used for comparision with the streaming data<br>

-stream [INPUT-FILE]<br>
Performs stream processing on the given file. If no file is given the Twitter online stream will be used instead.
The results will be put into ElasticSearch. You can use Kibana for visualization.

-compare<br>
If set and a batch was processed ("-batch [INPUT-FILE]" was set) for every output to the sink the computed value from
batch processing will be put into the sink aswell.

-ml<br>
Performs predictions on the data.
The model will be trained on the data from the stream.

-host [HOST]<br>
Change the host for elasticSearch from 127.0.0.1 to [HOST]

#### EXAMPLE FOR PROGRAM ARGUMENTS:<br>
-collect tweets<br>
Collects the data from twitter and writes them to "tweets"

-batch tweets -stream -compare<br>
Performs batch-processing on the file "tweets"<br>
Afterwards uses stream-processing on the online data from Twitter and compares them with the result from the batch

-stream tweets<br>
Performs stream-processing on the file "tweets" and emits the result to the sink (ElasticSearch)

-batch tweets -stream -ml<br>
Perform batch processing on the file "tweets" and refine the regression model used by -stream.
For the stream data we make predictions using the model and refine it if possible.

### Compile the project
The project can be build using mvn.<br>
1. open a terminal and navigate to the project dir<br>
2. build the project using 'mvn clean package'<br>
3. the jar is located under ./target/project-1.0.jar<br>

### Run the project
The project can be executed using Flink<br>
ElasticSearch is used as a sink, therefore please ensure that ElasticSearch is running.
If you are interested in the presentation of the data please make sure that Kibana is running.

To setup the index you can run "index.sh"<br>
If you want the cleanup the index you can delete the index by running "index_del.sh" and rerun "index.sh"

<b>ElasticSearch 6.4.0</b><br>
./bin/elasticsearch.bat<br>

<b>Kibana 6.4.0</b><br>
./bin/kibana.bat<br>

<b>Flink 1.6.0</b><br>
./bin/start-cluster.bat<br>
http://localhost:8081<br>

Submit the jar as a new job to flink.<br>
Make sure you set the correct program arguments. Check "Program Arguments" for reference.

The Job should run now. If you are interested in using the batch processing for comparision, make sure that the file is reachable from the flink environment. 
You will transfer the file to hdfs and configure the correct hdfs url to the file. 

### Used program versions
Flink 1.6.0
ElasticSearch 6.4.0
Kibana 6.4.0