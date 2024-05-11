### [Synonym(동의어)](https://esbook.kimjmin.net/06-text-analysis/6.6-token-filter/6.6.3-synonym)
#### Refer   
+ [SynonymAPI](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-analyze.html#java-rest-high-analyze-request)
 

#### ES 사용법
+ Synonym filter 적용
+ 동의어나 유의어에 해당하는 단어를 함께 저장해서 검색이 가능
+ 사전경로 : elasticsearch-8.12.1/config


#### Query dsl
+ Create Index
```
PUT /synonym_test
{
  "settings": {
    "index": {
      "analysis": {
        "analyzer": {
          "synonym_analyzer": {
            "tokenizer": "whitespace",
            "filter": ["my_synonyms"]
          }
        },
        "filter": {
          "my_synonyms": {
            "type": "synonym",
            "synonyms_path": "custom_synonym.txt",
            "updateable": true
          }
        }
      }
    }
  }
}
```     
+ Search Index   
```    
GET /synonym_test/_analyze
{
  "analyzer": "synonym_analyzer", 
  "text": "선생님 제모 크림 "
}
```    
