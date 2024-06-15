### 유사도
#### 



##### Fuzzy Query
``` 
PUT fuzzy_test
{
  "mappings": {
    "properties": {
      "test_data":{
        "type": "text",
        "fields": {
          "keyword" :{
            "type" : "keyword"
          }
        }
      }
    }
  }
}

```
```
# 사전 등록(오타사전에서 가져온 정타 등록하기)
POST fuzzy_test/_doc
{
  "test_data" : "노인과 바다"
}

``` 

``` 
GET fuzzy_test/_search?pretty
{
  "query": {
    "bool": {
      "should": [
        {"match": {
          "test_data": "노인ㅂ"
        }
          
        },
        {
          "fuzzy": {
            "test_data.keyword": {
              "value": "노인과 ㅂ",
              "fuzziness": 2
            }
          }
        }
      ]
    }
  }
} 
``` 


##### TF-IDF with N-gram (Scripted similarity)
```
PUT /scripted-similarity-index2 
{
  "settings": {
    "number_of_shards": 1,
    "analysis": {
      "analyzer": {
        "ngram_analyzer" :{
          "type": "custom",
              "filter": [
                  "lowercase"
              ],
              "tokenizer": "ngram"
        }
      }
    }, 
    "similarity": {
      "scripted_tfidf": {
        "type": "scripted",
        "script": {
          "source": "double tf = Math.sqrt(doc.freq); double idf = Math.log((field.docCount+1.0)/(term.docFreq+1.0)) + 1.0; double norm = 1/Math.sqrt(doc.length); return query.boost * tf * idf * norm;"
        }
      }
    }
  }, 
  
  "mappings": {
    "properties": {
      "field": {
        "type": "text",
        "similarity": "scripted_tfidf" ,
        "analyzer": "ngram_analyzer"
      }
    }
  }
}
```

```
GET /scripted-similarity-index2/_search
{
  "query": {
    "query_string": { 
      "query": "히알란^0.7",
      "default_field": "field"
    }
  }
}
```

+ put index 는 [typo_dictionary.py](https://github.com/jiyooonkim/data-engineer/blob/main/commerce/src/nlp/typo_dictionary.py) 참고
+ scripted 사용시 참고자료[painless Variable](https://www.elastic.co/guide/en/elasticsearch/painless/current/painless-similarity-context.html)
+ _score 튜닝 포인트 찾기 !! 


##### BM25 
```
 
```


