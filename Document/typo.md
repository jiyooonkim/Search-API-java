### 오타교정/유사상품
#### 방식 
+ 유사도 + 토크나이징(ngram + jaso token 분리)
  + jamo 분리(가방 =>ㄱㅏㅂㅏㅇ) -> ngram -> (scripted)tifidf or bm25 -> 문서 개수 많은 것에 가중치 씌우기

##### QueryDSL
+ Tiral1. TF-IDF 
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

+ Tiral2. ngram(my_ngram_analyzer 사용) -> Score = 입력 토근(합)/전체 토큰개수
  + Index & mapping 정의
    ```
      PUT /typo_beta_search3
      {
      "settings": {
        "number_of_replicas": "0",
        "max_ngram_diff": 50,
        "index": {
          "similarity": {
            "scripted_tfidf": {
              "type": "scripted",
              "script": { 
                "source": "double idf =((term.docFreq+1.0)/(field.docCount+1.0))+1.0;return query.boost * idf;"
              }
            }
          },
          "analysis": {
            "filter": {
              "suggest_filter": {
                "type": "ngram",
                "min_gram": 2,
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
  + 문서 생성
    ```
        POST typo_beta_search3/_bulk
        {"index":{"_id":"1111"}}
        { "field":"스타"}
        {"index":{"_id":"51"}}
        { "field":"스타벅스"}
        {"index":{"_id":"52"}}
        { "field":"스타벅스"}
        {"index":{"_id":"53"}}
        { "field":"스타일"}
        {"index":{"_id":"54"}}
        { "field":"스타일러"}
    ```

  + 문서 검색
    + scripted_tfidf에 가중치 부여방법 : 질의^배수      ex) 사이다^3
      ```
        GET /typo_beta_search3/_search?explain=true
        {
          "query": {
          "query_string": {
            "query": "스일^2",
            "default_field": "field"
            }
          }
        }
      ```

+ Tiral3. jamo 분리 -> ngram -> Score = 입력 토근(합)/전체 토큰개수
  + Index & mapping 정의
    ```
      PUT /typo_beta_search3
      {
        "settings": {
          "number_of_replicas": "0",
          "max_ngram_diff": 50,
          "index": {
            "similarity": {
              "scripted_tfidf": {
                "type": "scripted",
                "script": { 
                  "source": "double idf =((term.docFreq+1.0)/(field.docCount+1.0))+1.0;return query.boost * idf;"
                }
              }
            },
            "analysis": {
              "filter": {
                "suggest_filter": {
                  "type": "ngram",
                  "min_gram": 2,
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
              "analyzer": "suggest_index_analyzer"
            }
          }
        }
      }
    ```

  + 문서 검색
    + scripted_tfidf에 가중치 부여방법 : 질의^배수      ex) 사이다^3
    ```
      GET /typo_beta_search3/_search?explain=true
      {
        "query": {
          "query_string": { 
            "query": "뉴란^3",
            "default_field": "field"
          }
        }
      }  
    ```
    



##### 수정사항 & 특이사항   
+ Tokenizer
  + [jaso-analyzer](https://github.com/jiyooonkim/elasticsearch-jaso-analyzer.git)
    + 자,소 분해 plugin 기능 사용 적용
    + 수정사항 
      + Es 버전 변경 : 8.12.1
        



