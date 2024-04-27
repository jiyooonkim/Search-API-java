### 불용어

#### ES 사용법 
+ 

#### Query dsl
``` 
 PUT sample_stopwd_snym
 {
    "settings": {
        "analysis": {
            "tokenizer": "standard",
            "filter": {
                "custom_stop": {  
                    "type": "stop",
                    "stopwords_path": "stopword.txt"
                },
                "suctom_synonym": { 
                    "type": "synonym",
                    "synonyms_path": "custom_synonym.txt"
                }
            }
            
        }
    } 
}
 
```  



