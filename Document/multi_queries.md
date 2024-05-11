#### [불용어 & 동의어](https://mallikarjuna91.medium.com/what-is-tokenizer-analyzer-and-filter-in-elasticsearch-317d4ec69ecc)
``` 
PUT sample_stopwd_snym
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_custom_analyzer": {
           "type":"custom",
            "tokenizer": "whitespace",
            "filter": ["lowercase", "my_synonyms", "stop_filter"]
          } 
      },
      "filter": {
        "my_synonyms": {
            "type": "synonym",
            "synonyms_path": "custom_synonym.txt"
          },
        "stop_filter": {
          "type": "stop",
          "stopwords_path": "stopword.txt" 
        }
      }
    }
  }
} 

```

``` 

GET sample_stopwd_snym/_analyze
{
  "analyzer": "my_custom_analyzer",
  "text": [
    "동해물과 귀여운  생님 제모 밸런스 백두산이"
  ]
} 
``` 
+ [불용어 & 동의어 참조2](https://kkm8257.tistory.com/113)