# 🧶 뜨개질 프로젝트 관리 앱

친구들과 함께 사용할 수 있는 귀여운 뜨개질 프로젝트 관리 웹 앱입니다!

## ✨ 주요 기능
- 🎯 프로젝트 생성 및 관리
- 📊 진행 상황 추적 (단수 카운터)
- 📝 작업 일지 기록
- 👥 개인별 프로젝트 관리 (회원가입/로그인)
- 💕 귀여운 핑크 테마 디자인

## 🌐 배포 정보

**프론트엔드**: https://knitting-app-drab.vercel.app  
**백엔드**: https://knittingapp-backend.onrender.com

### 📌 배포 구조
```
사용자 → Vercel (React) → Render (Spring Boot) → Supabase (PostgreSQL)
```

### 🚀 배포 과정
1. **MySQL → PostgreSQL 변경** (무료 호스팅을 위해)
   - `build.gradle`: PostgreSQL 드라이버로 변경
   - `User.java`: `@Table(name = "users")` 추가 (예약어 충돌 방지)

2. **프론트엔드 배포 (Vercel)**
   - GitHub 연동 → 자동 배포
   - Environment Variables: `VITE_API_URL` 설정

3. **데이터베이스 생성 (Supabase)**
   - PostgreSQL 무료 플랜 (500MB)
   - Connection String 복사

4. **백엔드 배포 (Render)**
   - Docker 기반 배포
   - Environment Variables: DB 연결 정보, CORS 설정
   - 무료 플랜 (Sleep 모드, 첫 접속 시 15-30초 대기)

5. **환경 변수 연결**
   - Vercel: 백엔드 URL 등록
   - Render: 프론트엔드 URL CORS 허용

### 💰 비용
- **완전 무료** (소규모 사용 시)
- 첫 접속 시 약간 느릴 수 있음 (Render Sleep 모드)

## 💻 로컬 개발

### 필요한 것들
- Node.js 18+
- Java 21

### 실행 방법

#### 1. 데이터베이스 설정
```bash
mysql -u root -p
CREATE DATABASE knitting;
```

#### 2. 백엔드 실행
```bash
cd backend
./gradlew bootRun
```

#### 3. 프론트엔드 실행
```bash
cd frontend
npm install
npm run dev
```

#### 4. 브라우저에서 열기
http://localhost:5174

## 📁 프로젝트 구조
```
KnittingApp/
├── backend/          # Spring Boot 백엔드
│   ├── src/
│   │   ├── controller/   # REST API
│   │   ├── service/      # 비즈니스 로직
│   │   ├── repository/   # DB 접근
│   │   └── domain/       # 엔티티
│   └── build.gradle
├── frontend/         # React 프론트엔드
│   ├── src/
│   │   ├── features/     # 기능별 컴포넌트
│   │   ├── api/          # API 클라이언트
│   │   └── types/        # TypeScript 타입
│   └── package.json
└── .github/
    └── copilot-instructions.md  # 개발 가이드
```

## 🛠 기술 스택

### 백엔드
- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL (Supabase)
- JWT 인증
- Docker

### 프론트엔드
- React 18
- TypeScript
- Tailwind CSS
- Axios
- Vite

### 배포
- **프론트엔드**: Vercel
- **백엔드**: Render (Docker)
- **데이터베이스**: Supabase (PostgreSQL)

## 📝 개발 가이드
- 모든 개발 규칙은 `.github/copilot-instructions.md` 참고
- Controller → Service → Repository 계층 분리
- DTO 사용 필수
- REST API 네이밍 규칙 준수

## 🐛 문제 해결
- **CORS 에러**: `backend/src/main/java/com/knittingapp/config/CorsConfig.java` 확인
- **DB 연결 실패**: `backend/src/main/resources/application.yml` 확인
- **빌드 실패**: 로컬에서 `npm run build` 또는 `./gradlew clean build` 테스트


## 📄 라이선스
MIT License

---

**만든 사람**: eun2051
**목적**: 내가 쓸 뜨개앱 배포하기!