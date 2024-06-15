### 오타교정/유사상품
#### 방식 
+ 유사도 + 토크나이징(ngram + jaso token 분리)
  + jamo 분리(가방 =>ㄱㅏㅂㅏㅇ) -> ngram -> (scripted)tifidf or bm25 -> 문서 개수 많은 것에 가중치 씌우기

##### QueryDSL

+ Index & mapping 정의        
  ```
  PUT /typo_search
  {
    "settings": {
      "number_of_replicas": "0",
      "max_ngram_diff": 50,
      "index": {
        "similarity": {
          "scripted_tfidf": {
            "type": "scripted",
            "script": {
              "source": "double tf = Math.sqrt(doc.freq); double idf = Math.log((field.docCount+1.0)/(term.docFreq+1.0)) + 1.0; double norm = 1/Math.sqrt(doc.length); return query.boost * tf * idf * norm;"
            }
          }
        },
        "analysis": {
          "filter": {
            "suggest_filter": {
              "type": "ngram",
              "min_gram": 1,
              "max_gram": 50
            }
          },
          "analyzer": {
            "my_ngram_analyzer": {
              "tokenizer": "my_ngram_tokenizer"
            },
            "suggest_search_analyzer": {
              "type": "custom",
              "tokenizer": "jaso_search_tokenizer"
            },
            "suggest_index_analyzer": {
              "type": "custom",
              "tokenizer": "jaso_index_tokenizer",
              "filter": [
                "suggest_filter"
              ]
            }
          },
          "tokenizer": {
            "jaso_search_tokenizer": {
              "type": "jaso_tokenizer",
              "mistype": true,
              "chosung": false
            },
            "jaso_index_tokenizer": {
              "type": "jaso_tokenizer",
              "mistype": true,
              "chosung": true
            },
            "my_ngram_tokenizer": {
              "type": "ngram",
              "min_gram": "1",
              "max_gram": "10"
            }
          }
        }
      }
    },
    "mappings": {
      "properties": {
        "field": {
          "type": "text",
          "store": true,
          "similarity": "scripted_tfidf",
          "analyzer": "my_ngram_analyzer"
        }
      }
    }
  }
  ```
  
+ 인덱스 확인 & 모든 문서 검색
  ```
    GET /typo_beta_search2
    GET /typo_beta_search2/_search
  ```
+ 문서 생성
  ```
    POST typo_search/_bulk
    {"index":{"_id":"111"}}
    { "field":"빨래"}
    {"index":{"_id":"21"}}
    { "field":"빨간 "}
    {"index":{"_id":"31"}}
    { "field":"레드 빨간맛 레드 벨벳 티저"}
    {"index":{"_id":"41"}}
    { "field":"삼성 건조기 신혼"}
    {"index":{"_id":"1111"}}
    { "field":"대형 옷걸이 + 행거 "}
  ```

+ 문서 검색 
  + scripted_tfidf에 가중치 부여방법 : 질의^배수      ex) 사이다^3
    ```
      GET /typo_search/_search
      {
        "query": {
          "query_string": { 
            "query": "빨레^2",
            "default_field": "field"
          }
        }
      }
    ```


##### 
+ 유사도 알고리즘
  + BM25 score 부여 방식
    + 문서 자체에 출현빈도 높아야함 -> 사용자 질의는 대부분 토큰수가 짧다 
    + 다른 문서에 질의가 없을수록 높음 -> 사용자 질의가 많을수록 정타일 확률 높음  
  + TF-IDF
    + 문서에 등장횟수 높을수록 score 높음
  + Tokenizer
    + jaso-analyzer[https://github.com/jiyooonkim/elasticsearch-jaso-analyzer.git]
      + 자,소 분해 plugin 기능 사용 하고자
      + 수정사항 
        + Es 버전 변경 : 8.12.1
        



