# i-market
판매자와 구매자 간의 상품 거래를 위한 샘플 애플리케이션

## 설명
- 편의상 애플리케이션 명을 imarket으로 정했습니다.
- 회원으로 등록한 한명의 회원이 구매자 또는 판매자가 될 수 있습니다.
- 샘플 애플리케이션이므로 지불에 대한 구체적인 구현은 생략합니다.
- 애플리케이션을 IntelliJ 같은 IDE 또는 jar 파일로 실행 시키기 위해서는 "spring.profiles.active=local"을 설정으로 해주어야 합니다.
- Profile "local"로 실행시킬 경우 MySQL DB 대신에 H2 in-memory DB가 실행이 되고, 애플리케이션 실행 시 테스트 데이터가 입력이 됩니다.

## Object Entity Diagram
![Object Entity Diagram 이미지](https://github.com/hjs6877/i-market/blob/main/imarket_entity_diagram.png)

## 테이블 생성 SQL 쿼리문
* SQL 쿼리문 링크: [https://github.com/hjs6877/i-market/blob/main/imarket.sql](https://github.com/hjs6877/i-market/blob/main/imarket.sql)
* H2 Database Brower 실행 URL: [http://localhost:8080/h2](http://localhost:8080/h2)

## REST API 문서
REST API 문서는 Swagger로 생성되는 API 문서 대신에 Spring Rest Docs를 위한 테스트 케이스 실행을 통해 생성된 API 문서로 대체하였습니다. 
최초에 Swagger 적용을 검토했으나 코드 베이스의 간소화와 슬라이스 테스트의 이점을 얻고자 Spring Rest Docs를 적용하였습니다.
* REST API 문서 파일 링크: [AMO client RPC specification](https://github.com/amolabs/docs/blob/master/rpc.md) 
* REST API 문서 실행 URL: [http://localhost:8080/docs/index.html](http://localhost:8080/docs/index.html)
* Spring Rest Docs 문서([marketapi/src/main/resources/static/docs/index.html](https://github.com/hjs6877/i-market/blob/main/market-api/src/main/resources/static/docs/index.html))는 Gradle Task중에서 'bootjar' Task 실행 시 생성됩니다. 

## TODO
* Controller별 접근 제어를 위한 VerifyAdvice를 구현해야됩니다.
* 회원 등록, 주문 등록 시 회원에게 이메일 발송이 필요합니다.
