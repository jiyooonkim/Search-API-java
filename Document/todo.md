#### Todo list
+ 오타 교정 API 
  + nori 사전 색인 
    + 키워드 입력시 정타 후보 return API      
     ex) http://localhost:8085/typo?keyword=아이펀 
     retrun > {아이폰 ...} 
    + 질의별 cc, sc, ctr 기반 weight 적용  
  + Custom Dictionary Analyzer 적용
    + 쇼핑사전 (브랜드, 모델명 ,시리즈 토크나이징)
+ 연관 상품 상품 API    
  ex) http://localhost:8085/realation?product=아이펀15
    retrun > {product_nm : 아이폰15, 아이폰15프로, 아이폰15미니, 아이폰, 아이폰15케이스, 아이폰필름 ....etc}
+ 연관 키워드 API
  + 




+ 자동완성