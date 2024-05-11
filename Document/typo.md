#### 오타교정

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


##### TF-IDF query dsl (Scripted similarity)
+ [painless Variable](https://www.elastic.co/guide/en/elasticsearch/painless/current/painless-similarity-context.html)               
```
   PUT tfidf_typo
{
  "settings": {
    "number_of_shards": 1,
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
      "correct_cndd": {
        "type": "text",
        "similarity": "scripted_tfidf"
      }
    }
  }
}
```     

+ put index 는 [typo_dictionary.py](https://github.com/jiyooonkim/data-engineer/blob/main/commerce/src/nlp/typo_dictionary.py) 참고
``` 



``` 

##### 오타 교정 API 
+ 유사도 알고리즘
  + BM25 score 부여 방식
    + 문서 자체에 출현빈도 높아야함 -> 사용자 질의는 대부분 토큰수가 짧다 
    + 다른 문서에 질의가 없을수록 높음 -> 사용자 질의가 많을수록 정타일 확률 높음  
  + TF-IDF
    + 문서에 등장횟수 높을수록 score 높음
    + 


#### 