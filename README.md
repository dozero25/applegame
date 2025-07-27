# 🍎 사과게임 - 사과의 합을 맞추는 웹 기반 미니 게임
드래그로 사과를 선택해 제한 시간 내에 합이 10이 되도록 만드는 게임입니다.</br>
**다양한 방식의 코드 구성 연습**과 **Spring 기술 학습**을 목적으로 한 개인 프로젝트입니다.

</br>


### 프로젝트 요약
🍎 Apple Game - 제한 시간 안에 합 10 만들기 게임
 
 - 목적 : 다양한 방식으로 코드를 구성하는 연습과 구조적 설계 학습
 - 게임 방식 : 제한 시간 내에 사과들을 드래그하여 합이 10이 되도록 맞추는 간단한 미니 게임
 - 기술 스택 : Java, Spring Boot, HTML/CSS/JS, Swagger, Spring Security, JPA, QueryDSL
 - 현재 상태 : 게임 플레이 로직 구현 완료, 회원가입 및 로그인, 소셜 로그인(카카오, 네이버), QueryDSL 활용 데이터 처리, 마이페이지(유저 정보 변경 및 소셜 로그인 연동 해제)
 - 앞으로 추가할 기능 :  
   - 랭킹 시스템 (점수/등수/닉네임 기록)  
   - AWS 배포 예정


</br>

### 프로젝트 개요
- Java 기반 백엔드와 브라우저 환경의 HTML/JS로 구성된 웹 게임입니다.
- 기존 방식에 얽매이지 않고 코드 구조를 자유롭게 설계해보는 연습 프로젝트입니다.
- 게임 로직은 클라이언트에서 처리되며, 백엔드와의 연동은 향후 로그인/랭킹 기능 구현을 목표로 준비 중입니다.

</br>

### 게임 설명
- 제한 시간 동안 사과들을 드래그하여 선택합니다.
- 선택한 사과들의 숫자 합이 10이 되면 점수가 올라갑니다.
- 단순하면서도 판단력과 속도를 요구하는 게임입니다.


</br>

### 현재 구현된 기능
- 게임 UI 및 플레이 로직 구현 (HTML/CSS/JavaScript)
- 드래그 이벤트 처리 및 합산 로직
- 점수 계산 및 제한 시간 설정
- 로그인 및 소셜 로그인 기능 구현
- JPA를 활용한 회원가입 기능 구현  
- QueryDSL을 활용한 효율적인 데이터 쿼리 처리  

</br>

### 기술 스택
| 구분       | 기술                         |
|:---------:|:---------------------------|
| Language  | Java, JavaScript            |
| Frontend  | HTML, CSS, JavaScript       |
| Backend   | Spring Boot                 |
| Docs      | Swagger                     |
| ORM       | Spring Data JPA, QueryDSL   |
| DB        | MySQL                      |
| 인증      | OAuth2 (Naver, Kakao)       |
| 배포      | AWS EC2 or S3 (예정)        |


</br>

### 프로젝트 구조
``` bash
📦src
 ┣ 📂config              # Swagger, Spring Security 설정
 ┣ 📂entity              # JPA 엔티티 클래스 (MySQL 연동 예정)
 ┣ 📂exception           # 공통 예외 정의
 ┣ 📂repository          # Spring Data JPA Repository 인터페이스 (예정)
 ┣ 📂security            # 사용자 인증, Principal 처리 대비
 ┣ 📂web
 ┃ ┣ 📂advice            # 예외 처리 핸들러 (예: @RestControllerAdvice)
 ┃ ┣ 📂api               # JSON 데이터 응답용 API 컨트롤러
 ┃ ┣ 📂controller        # HTML 뷰 반환용 컨트롤러
 ┃ ┣ 📂dto               # 데이터 전송 객체 (DTO)
 ┃ ┗ 📂service           # 비즈니스 로직 처리
 ┗ 📂resources
   ┣ 📂static
   ┃ ┣ 📂css             # 스타일시트
   ┃ ┣ 📂images          # 정적 이미지 리소스
   ┃ ┗ 📂js              # JavaScript 파일
   ┣ 📂templates          # Thymeleaf 등의 템플릿 파일
   ┗ 📜application.yml   # 환경 설정 파일

```


</br>

### 앞으로 추가할 기능
- 로그인 시스템
  - 게스트 로그인
  - OAuth2 소셜 로그인 (카카오, 네이버 등)
- 관리자 시스템
  - 관리자 페이지에서 게임 난이도 조절
- 랭킹 시스템
  - 최고 점수 저장 및 등수, 닉네임, 점수 표시
- DB 연동 (MySQL)
  - 사용자 및 점수 데이터 저장
- AWS 배포
  - EC2 또는 S3를 통한 정식 배포

</br>

### 게임 화면 미리보기
![스크린샷 2025-05-23 214043](https://github.com/user-attachments/assets/a7cd90e6-f702-4e77-9861-1dca96db986b)
- 홈 화면

![스크린샷 2025-05-23 213834](https://github.com/user-attachments/assets/e583d03f-95d8-49b6-9131-8e2e94651019)
- 게임 실행 화면

![스크린샷 2025-05-23 214035](https://github.com/user-attachments/assets/c3aeb086-0239-4cc2-be9d-7807188ca21f)
- 게임 종료 화면

</br>

### 실제 플레이 영상
![palygame](https://github.com/user-attachments/assets/28d544de-25a8-4966-b49b-961554abd0d4)

</br>

### 통합 메인 화면  

![통합 MAIN UI](https://github.com/user-attachments/assets/34fddf3d-2545-4ded-b979-990e0cf5bed5)

**통합 main UI를 새로 추가했습니다. 앞으로 웹에서 즐길 수 있는 게임을 점차 확장하며 계속해서 기능을 추가할 예정입니다.**

</br>

### 회고
이번 프로젝트는 무엇보다 익숙한 방식에서 벗어나 다양한 구조로 코드를 작성해보는 것에 중점을 두었습니다.</br>

사과들의 합이 10이 되도록 제한 시간 내에 맞추는 게임이라는 단순한 규칙을 바탕으로, 드래그 이벤트 처리, 합산 로직, 상태 관리 등을 처음부터 스스로 설계하고 구현하며 로직을 어떻게 더 명확하고 유연하게 만들 수 있을지 깊이 고민하는 시간을 가졌습니다.</br>

게임의 흐름과 구조를 나누어보며 클라이언트 단에서의 상태 관리 방식, UI와 로직의 분리, 코드의 가독성과 유지보수성에 대해 한층 더 실감 있게 체득할 수 있었습니다.</br>

단순한 결과물 이상의 의미가 있었던 만큼, 앞으로 로그인, 랭킹, 관리자 기능 등 백엔드 연동과 확장을 통해 이 프로젝트를 더욱 발전시켜 나갈 계획입니다.
