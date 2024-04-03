팀원 소개 

- 김명찬(리더) |  [Github](https://github.com/mingtian-chan)
- 강다온(부리더) |   [Github](https://github.com/jays1144)   |   [Notion](https://www.notion.so/4f000a62c0884d558b1196e7de18e741?pvs=21)
- 김진욱 |   [Github](https://github.com/naraspc)  |   [Blog](https://www.notion.so/189d8ed0848d44c9b3a0718127f13b02?pvs=21)   |   [Notion](https://www.notion.so/d15f10c0996e4df89d2408f68c2565b3?pvs=21)
- 이진우 |   [leegitjw](https://github.com/leegitjw)  |  [Github](https://github.com/leegitjw)  |  [Notion](https://www.notion.so/a1855b8c5c4e4a98ba5cb782feb902d4?pvs=21)

---

# 서비스 소개

<aside>
💡 MarketHub

</aside>

- 백만건 이상의 대용량 트래픽을 처리하는 이커머스 쇼핑몰

---
## 🥃 Github 주소

‣https://github.com/hanghae-markethub/markethub

## 🥃 팀 노션 주소

[Market Hub](https://www.notion.so/Market-Hub-890957603b8d47249b5a1e469328d8ae?pvs=21) 

---

# 주요 기능

| 할인 특가 | Task Scheduler를 사용하여 특정 시간마다 할인&특가 상품 생성 |
| --- | --- |
| 비회원 장바구니 | Redis를 활용하여 비회원으로 장바구니 담기까지 가능, 결제는 회원가입을 유도하기위해 회원만 가능하게 설계 |
| 인증인가 | JWT, Security, Redis를 활용하여 Access Token, Refresh Token 구현 (CSRF, XSS를 통한 토큰탈취 대비) Access : 1시간, Refresh : 2주 |
| 결제 구현 | IamPort API를 사용하여 결제 API 구성, Redisson을 활용하여 RLock구현으로 동시성 제어, Request 변조공격을 대비한 가격 검증로직 작성 |
| HTTPS 적용 | HTTPS를 적용해 도메인 보안성/노출순위 향상 |
| 상품 검색 및 조회  | 서버 배포 시 Post Constructor를 사용하여 DB의 데이터를 Elastic Search와 Redis로 마이그레이션하고,  Elastic Search를 사용하여 키워드 를 포함하는 
모든 상품에 대해 페이징 된 검색기능을 제공 및 Redis를 활용하여 
아이템 검색성능을 최적화 하고 이후에 이루어진 수정, 삭제는 자동으로 업데이트되게 구현 |

# 트러블 슈팅 및 기술적 의사 결정 👇
![image](https://github.com/hanghae-markethub/markethub/assets/140101271/578da39e-5fa3-4ea4-86df-231d8c06c5f0)

위치 : [팀 노션](https://www.notion.so/Market-Hub-890957603b8d47249b5a1e469328d8ae?pvs=21) 내 중간부분 작업 정리 (트러블 슈팅)

# 서비스 아키텍처
<p align= "center">
<img src ="https://github.com/hanghae-markethub/markethub/assets/113502944/c6d8a44d-d88a-4c29-a93c-2af305f966d1">
</p>


# 서버 모니터링 환경
![2024-03-1115-26-47-ezgif com-video-to-gif-converter](https://github.com/hanghae-markethub/markethub/assets/140101271/e03bc729-ad63-40b3-aec9-f28998ce1854)


# 할인 & 특가 이벤트 생성
<p align= "center">
<img src ="https://github.com/hanghae-markethub/markethub/assets/113502944/19a2483e-9566-4a8f-9601-2e04487e3946">
</p>


# 비회원 장바구니 등록 및 로그인 후 구매 
<p align= "center">
<img src ="https://github.com/hanghae-markethub/markethub/assets/113502944/d7267389-729f-4995-b22e-f0495d430c86">
</p>

| 기술적 의사결정 | 상세 내용 |
| --- | --- |
| MySQL | 모놀리식으로 구성된 아키텍처에서 단일 관계형 DB중, 가장 숙련도가 높은 DB 선택 |
| Lock을 어디에 걸어야 할까? | 몰린 모든 트래픽을 1차로 서버에서 검증하고, 2차로 외부 API에서 결제한 뒤 3차로 후처리 트래픽을 감당하는 구조로 설계한다면 서버에서 감당해야할 트래픽이 감소할 것이라는 판단으로 구매 검증단에 Lock을 적용했다. |
| Redis (https://www.notion.so/Redis-623dea569e08432383780fa77fd90dff?pvs=21) | 인메모리 저장소로서, 조회 성능 최적화를 위해 사용, redisson RLock을 위해 사용 |
| Redisson | 온라인 이커머스 쇼핑몰의 특성 상, 할인 특가 시 과도한 트래픽이 몰릴 것이라 예상. 오토스케일링이 적용된 배포 환경 상, 여러 인스턴스의 분산 처리가 필요. Redis를 기존에 사용 중이었기 때문에 리소스 감소를 위해 Redisson을 선택 |
| S3 | 사진 업로드 전용 스토리지를 따로 구축하여, 데이터베이스를 효율적으로 사용 |
| HTTPS/Rout53 | SSL/TLS 발급을 통한 CSRF, XSS의 예방 및 최소한의 보안 진행 |
| GitAction & Docker, Beanstalk | Docker에 적합한 Jenkins가 있지만 기존 구현 경험이 있는 깃허브 액션즈로 진행. 빈스토크를 사용하면 쉽게 배포가 가능하지만 서버리스라 직접 서버를 관리하는 주체가 많이 AWS로 넘어가서 EC2와 Docker를 사용해서 구현. 현재 상황에서는 CodeDeploy까지 수정을 못해서 무중단 배포라고는 할 수 없다. 이 부분은 차후 카프카 도입 과정 후 수정 예정 |
| Grafana, Loki, Prometheus | 서버의 상태와 로그를 모니터링하기 위해 선택. ELK는 러닝커브가 길고, 비용이 청구되기 때문에 기각 |
| TDD 도입과 폐지 | **TDD의 도입 배경**: 테스트 코드를 먼저 작성함으로써 코드가 의도와 관계없이 작동하지 않게하기 위함. 기능을 추가할 때 전체적인 코드의 동작을 체크할 수 있음. <br>디버깅 시간의 단축: 각각의 모듈 별로 테스트를 자동화할 수 있는 코드가 없다면 특정 버그를 찾기 위해서 모든 레벨의 코드들을 살펴봐야 하는데, TDD를 도입함으로 인해 버그가 발생하는 위치를 특정하여 디버깅을 할 수 있음. <br>코드의 안정성 증가. <br> **TDD 폐지 배경**: 코드 구현 속도의 심각한 감소: Service에 연결된 여러 관계로 인해 각각의 repository를 주입해서 직접 값을 넣어주는 부분이 발생했다. <br>그로 인해 테스트 코드의 복잡도가 증가했고, 코드 구현 속도의 심각한 저하가 발생했다. <br>테스트 코드 방향성을 잡아줄 개발자의 부재: TDD의 장점을 활용하려면 모든 경우의 수를 생각하고, 이를 테스트 코드로 구현할 수 있어야 함. <br>우리 프로젝트의 경우 테스트 코드의 방향성을 잡아줄 경험있는 개발자가 존재하지 않아, 테스트 코드 작성의 한계가 뚜렷하게 다가왔다.<br> TDD의 폐지: 테스트 코드를 작성하며 생산성이 심각하게 감소하게 되었고, 테스트 코드의 방향을 잡아줄 개발자의 부재 때문에 퀄리티 높은 테스트 코드를 작성할 수 없다고 판단되는 바, 프로젝트에서 TDD를 폐지하고 기능 구현 후 테스트 코드를 작성하는 방식으로 변경하였다. <br>방식의 변경으로 인해 얻을 수 있는 기대값으론 기능 구현 이후 테스트 코드를 작성함으로 생산성을 증가시킬 수 있을 것으로 판단되며 구현된 기능에 맞춰 테스트 코드를 작성하기 때문에 방향성을 잡기 쉬울 것으로 예상. TDD를 폐지하되, 테스트 코드는 유지하여 TDD의 장점은 남기고 단점은 희석 시킬 수 있을 것으로 기대된다. |
| 리액티브 프로그래밍 도입과 폐지 | **리액티브 프로그래밍 도입 배경**: 대용량 실시간 시스템에서 모든 요청과 응답을 비동기로 처리함으로서 빠른 요청 처리가 가능하여 같은 스레드 수 에서 높은 효율을 보임. <br> **리액티브 프로그래밍 폐지 배경**: 대규모 트래픽이 발생하는 이커머스 사이트에서는 여러 서버와의 호출이 많아 비동기 처리시 요청을 빠르게 처리할 수 있으나 현재 마켓허브 프로젝트에서는 서버의 수가 적기 때문에 비동기 처리를 하기 위한 코드가 요청에 대한 응답을 지연시켜 효율을 떨어트리는 현상이 발생 |
| JPQL 도입 | **JPQL 도입 배경**: 쿼리를 직접 커스터마이징할 수 있어, 복잡한 검색 로직이나 N+1 문제를 해결하기 위해 도입하게 되었다. JPQL은 특정 DB에 종속적이지 않은 성질을 가지고 있기 때문에, JPQL 쿼리를 정의하면 JPA 프로바이더 (예: Hibernate, EclipseLink 등)가 JPQL 쿼리를 해당 데이터베이스에 맞는 SQL로 변환하여 실행한다. |
| 타 도메인의 Repository를 의존하지 말자. | Repository를 의존하게 되면 다른 도메인들을 쉽게 사용할 수 있다. <br>따라서 가벼운 어플리케이션을 만든다면 그냥 Service가 다른 도메인의 Repository를 의존하여 사용하는 것이 오히려 더 개발하는데 용이하다. <br>다만 어플리케이션의 규모가 커질수록 도메인도 커지고, 관리하는 Service도 많아진다. <br>이때 하나의 도메인 Service가 다양한 Repository를 관리하게 된다면 도메인 Service의 책임이 너무 무거워지게 되고, 해당 도메인을 관리하는 Service가 다른 도메인도 관리하게 되어 Service의 역할도 모호해진다. <br>따라서 하나의 Service가 하나의 Repository를 갖게하여 도메인을 관리하도록 하고 이들을 관리하는 상위 Service를 두어 트랜잭션 관리를 하는 것을 생각하게 되었다. |
| 외부 결제 API의 도입 | 포트원사에서 제공하는 결제 API인 Portone (구 아임포트) API는 B2B, B2C 모두에게 API, 연동모듈, 기술지원, 온보딩 서비스까지 모두 제공하여 선택했다.  <br>- 또한, 결제단계에서 트래픽을 포트원사 API쪽으로 넘기고, 실제로 결제가 완료된 요청에 한해서 락을걸고 서버에서 작업을 하도록 구성하여 부하를 조금이라도 줄일 수 있다고 판단하였다.|
| 로드밸런서 & 오토스케일링 | - 할인 & 특가 이벤트가 잦고, 이에 따른 특정 시간대에 트래픽이 집중되는 이커머스 쇼핑몰 특성 상, 과도한 트래픽을 동적으로 받아 줄 수 있는 서버가 필요하게되었다. |
| Elastic Search | - 백만건 이상 저장된 상품들을 효과적으로 검색하고 보여주기 위해 검색 성능을 개선할 필요가 있었다. <br>  - 검색 성능 개선을 위해 JPQL을 통한 커스텀 쿼리 작성, Elastic Search 도입, Redis와  같은 캐시를 활용한 빠른 검색이 후보에 올랐다. <br> - DB를 통한 조회의 경우 인덱스와 N+1문제 해결을통해 성능을 최적화 하였음에도 1100ms가 소요되게 되었다. <br> - Redis를 통한 조회 시, value를 역직렬화 하는 로직이 추가되어 DB보다 조회성능이 떨어지게 되었다. 1200ms <br> - 엘라스틱 서치는 (검색어가 될 수 있는) 문자들을 유연하게 토큰화하고 인덱싱하는데 특화되어 있음, 데이터가 적을땐 DB와 Redis보다 성능이 떨어지지만, 데이터가 증가할수록 기하급수적으로 차이가 벌어지기 시작함. <br> - DB의 과부하 방지 : 검색 기능을 Elasticsearch에 일임함으로 서 DB의 부하를 줄일 수 있음 <br> - 분산 아키텍처 : 엘라스틱서치는 분산 아키텍처를 기반으로 설계되어 있어서 대용량 데이터를 처리하고 확장하기 용이함 <br> - 대량의 데이터를 대상으로 한 검색 : 상품 검색은 일반적으로 개별 레코드 검색이 아니라 대량의 데이터를 대상으로 한 검색 작업이라 적합 <br>|
| JWT Token & Refresh Token | 쿠키 <br>  - 쿠키는 클라이언트의 로컬에 저장되는 키와 값이 들어있는 데이터파일 이다. <br >사용자 인증이 유효한 시간을 명시할 수 있으며, 유효시간이 정해지면 브라우저가 종료되어도 인증을 유지할 수 있다는 특징을 가진다. <br> 세션 <br>  - 세션은 쿠키를 기반으로 하지만, 사용자의 정보파일을 브라우저에 저장하는 쿠키방식과 달리, 서버측에서 관리한다. 사용자 정보를 서버에 두기 떄문에 쿠키보다 보안은 좋지만, 사용자가 많아질 수록 서버 메모리를 많이 차지하게 된다. <br> 토큰 <br> - 토큰은 서버만 만들 수 있는 토큰을 만들어서 사용자에게 발급해주고, 사용자는 쿠키에 토큰을 저장해서 필요할 때마다 토큰을 이용해서 사용자를 증명하는 방식이다. <br> 세션방식에선 사용자의 상태를 서버에서 관리하기 때문에 로그인 상태를 변경할 수 있지만, 토큰방식은 토큰을 발급하고 따로 서버에서는 상태를 기억하지 않는다. 그렇기에 사용자의 상태를 변경할 수 없다. <br> 이를 해결하기 위해서 만료기간을 정하여 XSS혹은 CSRF로 인해 탈취되었을 경우에 피해를 최소화 할 수 있다. <br> 토큰을 선택한 이유 <br> - 온라인 어커머스 특성 상 많은 유저가 사용하는 상황을 상정하였기에, 유저 정보를 세션으로 관리하면 서버 메모리에 부하를 줄 것으로 예상되어 Jwt토큰을 사용하기로 했다. <br> 또한, 토큰 탈취를 대비하여 refresh Token을 적용하고 Access Token의 유효기간을 짧게 설정하여, access Token이 탈취되더라도 만료 기한이 짧기에 탈취로 인한 피해를 최소화 하고자 적용 하였다. |
| 도메인 분리 (SOA, 예정) | - 다른 도메인에서 발생한 장애가 전파되지 않게 하기 위함 <br> - 트래픽이 몰릴것이라고 예상되는 특정 도메인 (아이템, 결제)의 스케일을 유동적으로 조절하여, 효율을 높이고자 하였다. <br> - 각 도메인별 테스트코드를 보장함으로서 생기는 긴 빌드시간의 단점을 도메인별로 분리하여 해결하고자 하였다. <br> - 기능의 수정이 잦을 것 이라고 판단되는 특정 도메인 (예시 : 아이템, 장바구니)이 수정될때, 기존 모놀리식 아키텍처의 경우 모든 서버가 꺼졌다 커지는과정에서  소요되는 시간과 발생하는 리소스를 SOA 를거쳐 MSA로 진화하며 해결하고자 하였다. <br> - 도메인 분리시 이루어져야 할 과정으로는 아래와 같이 정리하였다. <br> 1. Global 도메인 분리 <br> 2. API Gateway 서버 추가 <br> 3. Service Discovery 서버 추가 <br> 4. 참조중이던 다른 도메인 entity를 별도의 Table을 만들어서 관리 <br> 5. 다른 도메인과의 비동기적 통신을 하기위해  카프카를 사용 <br> 6. config 분리 <br> 7. 트랜잭션 보장|
| KAFKA (예정) | - 도메인을 분리하는 과정에서 도메인간의 통신을 위해 선택하게 되었다. <br> - 다른 Message Queue 방식의 스택보다 Kafka를 선택하게 된 이유는 Eventual Consistency 때문이다. 카프카의 경우 한번 Event Queue로 전달된 메세지는 무슨일이 있어도 반드시 구독중인 서비스에게 전달되는 특징을 가지고 있어, 예상치못한 서버 다운의 경우에도 빠르게 복구하여 요청을 처리할수있을거라 판단했다. <br> <br> - 고려한 또 한가지는 유연한 확장성이다. <br> 어떤 장치를 연결하여도, Kafka 클라이언트의 port만 맞춰주면 정상적으로 통신 할 수 있는 특성을 가지고있어 선택하고자 하였다.|

# [트러블 슈팅 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
### [Lock의 적용 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
### [N+1 성능 이슈 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
### [Elastic Seach 이슈 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
### [RefreshToken 이슈 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
### [EC2 + Docker 배포 이슈 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
### [Redis 값 변경 이슈 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
### [잘못된 매핑으로 인한 무한참조로 Java Heap메모리 초과 이슈 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
### [포트원 결제 토큰 API 50%의 성공 문제 - link ](https://www.notion.so/naraspc/7-0fd11fc489e94f9da0f5670c4cc3735f?pvs=4#36f6c23dc30e42c1bdad1814489cd48a) <br>
