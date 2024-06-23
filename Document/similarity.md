### 유사도[https://www.elastic.co/guide/en/elasticsearch/reference/current/index-modules-similarity.html]
#### 유사도 알고리즘
+ BM25 
    + BM : Best Matching N(25) 
    + 문서 자체에 출현빈도 높아야함 
    + 복잡도(O(n), n=문서의 단어수)
    + 한 단어가 문서에 유독 많을 수록 문서 길이 짧을 수록
+ TF-IDF
    + 계산 방법 간단, 복잡도(O(n), n=문서의 단어수) 낮음 
    + 빈도 높은 단어에 둔감, 매개변수 조정 어려움
    + 한 단어가 문서에 유독 많을 수록, 다른 문서 에는 적을 수록 높은 점수 

사용자 질의는 대부분 토큰수가 짧다

등장비율 = 해당토큰/전체토큰

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
+ 
```
 
```


