### Springboot 검색 API 만들기
#### env 
+ oracle java17
+ Spring boot 3.0.1
+ gradle-8.0

#### Project list 
+ 오타 교정 API 
+ 연관 상품 상품 API
+ 연관 키워드 API
+ [동의어 API](./src/main/java/org/analyzer/App.java)
+ [불용어 API](./src/main/java/org/analyzer/App.java)



#### ES
+ [ES Rest API(Low Level vs Hight Level)](./doc/es_rest_API.md)




#### Spring  
+ ES setting
  + SSL 풀어주기
  + ES 비밀번호 변경 command
    "./bin/elasticsearch-reset-password -u elastic -i "
+ [ES password 변경 & Kibana 적용 참고자료](https://www.elastic.co/guide/en/elasticsearch/reference/current/security-minimal-setup.html#add-built-in-users)
+ [version setting 참고자료](https://velog.io/@jollypyun/%EC%98%A4%EB%A5%98-%EA%B8%B0%EB%A1%9DCould-not-resolve-org.springframework.bootspring-boot-gradle-plugin3.0.1) 