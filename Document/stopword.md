### [Stopword(불용어)](https://esbook.kimjmin.net/06-text-analysis/6.6-token-filter/6.6.2-stop)    
#### Refer    
+ [StopwordAPI](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-analyze.html)

#### ES 사용법 
+ Create Index        
``` 
    PUT stopword_test
    {
      "settings": {
        "analysis": {
          "analyzer": {
            "my_stop_filter": {
              "tokenizer": "whitespace",
              "filter": [ "stop_filter" ]
            }
          },
          "filter": {
            "stop_filter": {
              "type": "stop",
              "stopwords_path": "stopword.txt" 
            }
          }
        }
      }
    }
```
+ Search Index   
```
    GET stopword_test/_analyze
    {
      "tokenizer": "whitespace", 
      "filter": [ 
        "stop_filter"
      ],
      "text": [ "캐주얼 Around 피부 the W234orld in Eighty 귀여운 Days" ]
    }
```     

