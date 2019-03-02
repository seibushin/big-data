curl -XPUT "http://localhost:9200/tw_countries"
curl -XPUT "http://localhost:9200/tw_countries/_mapping/countries" -d'
{
 "countries" : {
  "properties" : {
    "hashtag": {"type": "keyword"},
    "lang": {"type": "keyword"},
    "time": {"type": "date"},
    "count": {"type": "integer"},
    "location": {"type": "geo_point"}      
    }
 }
}' -H 'Content-Type: application/json'

curl -XPUT "http://localhost:9200/tw_follower"
curl -XPUT "http://localhost:9200/tw_follower/_mapping/follower" -d'
{
 "follower" : {
  "properties" : {
    "hashtag": {"type": "keyword"},
    "follower": {"type": "float"},
    "time": {"type": "date"}     
    }
 }
}' -H 'Content-Type: application/json'

curl -XPUT "http://localhost:9200/tw_count"
curl -XPUT "http://localhost:9200/tw_count/_mapping/count" -d'
{
 "count" : {
  "properties" : {
    "hashtag": {"type": "keyword"},
    "count": {"type": "integer"},
    "time": {"type": "date"}     
    }
 }
}' -H 'Content-Type: application/json'

curl -XPUT "http://localhost:9200/tw_verified"
curl -XPUT "http://localhost:9200/tw_verified/_mapping/verified" -d'
{
 "verified" : {
  "properties" : {
    "hashtag": {"type": "keyword"},
	"user": {"type": "keyword"},
    "follower": {"type": "integer"},
    "time": {"type": "date"}     
    }
 }
}' -H 'Content-Type: application/json'

curl -XPUT "http://localhost:9200/tw_friends"
curl -XPUT "http://localhost:9200/tw_friends/_mapping/friends" -d'
{
 "friends" : {
  "properties" : {
    "hashtag": {"type": "keyword"},
    "friends": {"type": "integer"},
    "time": {"type": "date"}     
    }
 }
}' -H 'Content-Type: application/json'

curl -XPUT "http://localhost:9200/tw_pred_verified"
curl -XPUT "http://localhost:9200/tw_pred_verified/_mapping/verified" -d'
{
 "verified" : {
  "properties" : {
    "type": {"type": "keyword"},
	"follower": {"type": "integer"},
    "verified": {"type": "float"},
    "time": {"type": "date"}     
    }
 }
}' -H 'Content-Type: application/json'

curl -XPUT "http://localhost:9200/tw_pred_follower"
curl -XPUT "http://localhost:9200/tw_pred_follower/_mapping/follower" -d'
{
 "follower" : {
  "properties" : {
    "type": {"type": "keyword"},
	"hashtags": {"type": "integer"},
    "follower": {"type": "integer"},
    "time": {"type": "date"}     
    }
 }
}' -H 'Content-Type: application/json'

curl -XPUT "http://localhost:9200/tw_pred_lang"
curl -XPUT "http://localhost:9200/tw_pred_lang/_mapping/lang" -d'
{
 "lang" : {
  "properties" : {
    "type": {"type": "keyword"},
    "count": {"type": "integer"},
    "time": {"type": "date"}     
    }
 }
}' -H 'Content-Type: application/json'