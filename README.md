# 멍피디아
![Image](https://github.com/user-attachments/assets/2daed15c-dbd8-48ce-b32a-95f73d1c425f)

### 강아지 커뮤니티 사이트

## 목차
1. [사용 기술 스택](#사용-기술-스택)
2. [도메인 및 API 명세](#도메인-및-API-명세)
3. [프로젝트 개발 일정](#프로젝트-개발-일정)
4. [아키텍쳐](#아키텍쳐)
5. [ERD](#ERD)

# 사용 기술 스택
<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=Java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white">
  <img alt="Static Badge" src="https://img.shields.io/badge/JPA-brightgreen?style=for-the-badge&logo=JPA">
  <img alt="Static Badge" src="https://img.shields.io/badge/WebSocket-brightgreen?style=for-the-badge&logo=WebSocket&logoColor=white&color=red">

  <img alt="Static Badge" src="https://img.shields.io/badge/Elastic%20Search-brightgreen?style=for-the-badge&logo=Elastic%20Search&logoColor=white&color=orange">
  <img alt="Static Badge" src="https://img.shields.io/badge/OAuth%202-brightgreen?style=for-the-badge&logo=OAuth%202&logoColor=white&color=blue">
  

  <br>

  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=black">
  <img alt="Static Badge" src="https://img.shields.io/badge/Next.js-brightgreen?style=for-the-badge&logo=Next.js&logoColor=white">
  <img src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white">
  <img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white">
  <img alt="Static Badge" src="https://img.shields.io/badge/StompJs-brightgreen?style=for-the-badge&logo=StompJs&logoColor=white&color=red">
  <img alt="Static Badge" src="https://img.shields.io/badge/Embla%20Carousel-brightgreen?style=for-the-badge&logo=Embla%20Carousel&logoColor=white&color=orange">
  <img alt="Static Badge" src="https://img.shields.io/badge/KaKao%20Maps%20API-brightgreen?style=for-the-badge&logo=KaKao%20Maps%20API&logoColor=white&color=yellow">
  <img alt="Static Badge" src="https://img.shields.io/badge/ReactPaginate-brightgreen?style=for-the-badge&logo=ReactPaginate&logoColor=white&color=blue">



  <br>

  <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white">
  <img src="https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white">
  
  <br>

  <img alt="Static Badge" src="https://img.shields.io/badge/AWS%20EC2-brightgreen?style=for-the-badge&logo=AWS%20EC2&logoColor=white&color=orange">
  <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
  <img src="https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</div>

# 도메인 및 API 명세

## 백과 API
| Method     | Endpoint              | Description                     |
| ---------- | --------------------- | ------------------------------- |
| **POST**   | `/api/pedia`          | 백과 게시글 작성                       |
| **GET**    | `/api/pedia/{postId}` | 특정 백과 게시글 조회                    |
| **PUT**    | `/api/pedia/{postId}` | 백과 게시글 수정  |
| **DELETE** | `/api/pedia/{postId}` | 백과 게시글 삭제                       |
| **GET** | `/api/pedia/top12`      | 인기 백과 게시글 TOP 12 조회 |
| **GET** | `/api/pedia/all/{page}` | 전체 백과 게시글 페이지네이션 조회 |
| **POST**   | `/api/pedia/editRequest`                    | 백과 수정 요청 등록          |
| **GET**    | `/api/pedia/editRequest/all/{page}`         | 전체 수정 요청 목록 조회 |
| **GET**    | `/api/pedia/editRequest/{id}`               | 특정 수정 요청 상세 조회       |
| **POST**   | `/api/pedia/editRequest/accept`             | 수정 요청 수락 및 반영        |
| **DELETE** | `/api/pedia/editRequest/accept/{requestId}` | 수정 요청 거절         |

## 게시판 API
| Method     | Endpoint              | Description                  |
| ---------- | --------------------- | ---------------------------- |
| **POST**   | `/api/board`          | 게시글 작성                       |
| **GET**    | `/api/board/{postId}` | 특정 게시글 조회                    |
| **PUT**    | `/api/board/{postId}` | 게시글 수정 |
| **DELETE** | `/api/board/{postId}` | 게시글 삭제                       |
| **GET** | `/api/board/top2`       | 인기 게시글 TOP 2 조회 |
| **GET** | `/api/board/all/{page}` | 전체 게시글 페이지 조회   |
| **POST** | `/api/board/like/{postId}` | 게시글 좋아요/취소 토글 처리 |

## 마이페이지 API
| Method  | Endpoint                      | Description            |
| ------- | ----------------------------- | ---------------------- |
| **GET** | `/api/myPage/all/post/{page}` | 내가 작성한 게시글 전체 조회  |
| **GET** | `/api/myPage/all/likedPost/{page}` | 내가 좋아요한 게시글 전체 조회  |

## 로고 API
| Method  | Endpoint    | Description                                   |
| ------- | ----------- | --------------------------------------------- |
| **GET** | `/api/logo` | 현재 저장된 로고 이미지 조회                              |
| **PUT** | `/api/logo` | 로고 이미지 생성 또는 수정) |


## 이벤트 API
| Method     | Endpoint              | Description                      |
| ---------- | --------------------- | -------------------------------- |
| **POST**   | `/api/event`          | 이벤트 게시글 작성               |
| **PUT**    | `/api/event/{postId}` | 이벤트 게시글 수정  |
| **DELETE** | `/api/event/{postId}` | 이벤트 게시글 삭제                       |
| **GET**    | `/api/event/{postId}` | 특정 이벤트 게시글 조회                    |
| **GET** | `/api/event/allEvent` | 전체 이벤트 게시글 조회 |


## 검색 API
| Method  | Endpoint           | Description                 |
| ------- | ------------------ | --------------------------- |
| **GET** | `/api/search/both` | 게시판 + 백과 통합 검색 (keyword 기반) |

## 댓글 API
| Method     | Endpoint                   | Description      |
| ---------- | -------------------------- | ---------------- |
| **GET**    | `/api/comment/{postId}`    | 특정 게시글의 모든 댓글 조회 |
| **POST**   | `/api/comment`             | 댓글 작성            |
| **PUT**    | `/api/comment/{commentId}` | 특정 댓글 내용 수정      |
| **DELETE** | `/api/comment/{commentId}` | 특정 댓글 삭제         |

## 채팅 API
| Method   | Endpoint                  | Description            |
| -------- | ------------------------- | ---------------------- |
| **GET**  | `/api/chat`               | 현재 로그인 유저의 전체 메시지 조회   |
| **GET**  | `/api/chat/with/{userId}` | 특정 유저와 관리자 간 채팅 메시지 조회 |
| **POST** | `/api/chat/send`          | 채팅 메시지 전송              |
| **GET**  | `/api/chat/list`          | 현재 로그인 유저의 채팅방 목록 조회   |

## 회원 API
| Method  | Endpoint      | Description      |
| ------- | ------------- | ---------------- |
| **GET** | `/api/member` | 현재 로그인한 회원 정보 조회 |

## ADMIN API
| Method     | Endpoint                                    | Description          |
| ---------- | ------------------------------------------- | -------------------- |
| **GET**    | `/api/pedia/editRequest/all/{page}`         | 전체 수정 요청 목록 조회  |
| **GET**    | `/api/pedia/editRequest/{id}`               | 특정 수정 요청 상세 조회       |
| **POST**   | `/api/pedia/editRequest/accept`             | 수정 요청 수락 및 반영        |
| **DELETE** | `/api/pedia/editRequest/accept/{requestId}` | 수정 요청 거절         |
| **POST**   | `/api/event`          | 이벤트 게시글 작성  |
| **PUT**    | `/api/event/{postId}` | 이벤트 게시글 수정          |
| **DELETE** | `/api/event/{postId}` | 이벤트 게시글 삭제          |
| **PUT** | `/api/logo` | 로고 이미지 생성 또는 수정 |
| **GET** | `/api/chat/with/{userId}` | 특정 유저와 관리자 간 채팅 메시지 조회 |
| **GET** | `/api/chat/list`          | 관리자용 전체 채팅방 목록 조회      |




# 프로젝트 개발 일정
### Week 1 (04/28 ~ 05/04) | 기획 및 구조 설계
- **05/01 (목)**

  - 주제 선정, 기획 및 구조 설계

  - 코딩 컨벤션 정리

  - 요구사항 정리, ERD, 피그마 설계

### Week 2 (05/05 ~ 05/11) | 인증 기능 구축
- **05/02 (금)**

  - OAuth2.0 Login 기능 구현

  - *05/05 (월)*

  - Access/Refresh Token 생성 및 블랙리스트 처리 로직 구현

### Week 3 (05/12 ~ 05/18) | 인증 및 보안 고도화
- **05/13 (화)**

  - Access Token 재발급 기능

  - 로그아웃 및 사용자 정보 조회 기능 구현

- **05/14 (수)**

  - 리프레시 토큰 제거 스케줄러 추가

- **05/16 (금)**

  - S3 이미지 업로드 기능 추가

### Week 4 (05/19 ~ 05/25) | 백과 도메인 개발
 - **05/24 (토)**

  - 백과 기능 추가

### Week 5 (06/17 ~ 06/23) | 주요 기능 통합 개발
- **06/20 (금)**

  - 로고 기능 추가

  - 이벤트 기능 추가

  - 검색 기능 추가

### Week 6 (06/24 ~ 06/30) | 채팅 기능 개발
- **06/29 (일)**

  - 채팅(chat) 기능 추가

### Week 7 (07/07 ~ 07/13) | 수정 요청 기능 개발
- **07/10 (목)**

  - 수정 요청 반려 기능 추가

### Week 8 (07/14 ~ 07/20) | 배포 환경 준비
- **07/21 (월)**

  - 개발환경 → 배포환경 설정 변경

  - EC2, VPC, NGINX, 도메인 설정

### Week 9 (07/21 ~ 07/31) | 배포 및 점검
- **07/21 ~ 07/31**

  - 최종 배포 및 버그 수정

# 아키텍쳐
<img width="1013" height="639" alt="Image" src="https://github.com/user-attachments/assets/3a5d6792-f817-46b8-8c71-00940e90ab6f" />

# ERD
<img width="1798" height="926" alt="Image" src="https://github.com/user-attachments/assets/63a5ae6d-62fd-4d2e-a43b-d2cd71951e3b" />
