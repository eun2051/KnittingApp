# 🧶 뜨개질 프로젝트 관리 앱 배포 가이드

## 📋 목차
1. [프론트엔드 배포 (Vercel)](#1-프론트엔드-배포-vercel)
2. [백엔드 배포 (Railway)](#2-백엔드-배포-railway)
3. [데이터베이스 설정](#3-데이터베이스-설정)
4. [환경변수 설정](#4-환경변수-설정)
5. [최종 확인](#5-최종-확인)

---

## 1. 프론트엔드 배포 (Vercel) 

### ✅ 단계 1: GitHub에 코드 푸시
```bash
cd /Users/eun/Desktop/KnittingApp
git add .
git commit -m "Deploy ready: 배포 준비 완료"
git push origin main
```

### ✅ 단계 2: Vercel 배포
1. **Vercel 가입**: https://vercel.com (GitHub 계정으로 로그인)
2. **New Project 클릭**
3. **GitHub 저장소 선택**: `KnittingApp` 선택
4. **프로젝트 설정**:
   - Framework Preset: **Vite**
   - Root Directory: **frontend**
   - Build Command: `npm run build`
   - Output Directory: `dist`
5. **환경변수 추가**:
   - Name: `VITE_API_URL`
   - Value: `https://your-backend-url.railway.app/api` (Railway 배포 후 입력)
6. **Deploy 클릭**!

🎉 **배포 완료!** Vercel이 자동으로 `https://your-app.vercel.app` 같은 URL을 제공합니다.

---

## 2. 백엔드 배포 (Railway) 

### ✅ 단계 1: Railway 가입
1. https://railway.app 접속
2. GitHub 계정으로 로그인

### ✅ 단계 2: 프로젝트 생성
1. **New Project** 클릭
2. **Deploy from GitHub repo** 선택
3. **KnittingApp** 저장소 선택
4. **Add Service** → **Database** → **MySQL** 선택

### ✅ 단계 3: 백엔드 서비스 설정
1. **New Service** → **GitHub Repo** 선택
2. **Root Directory**: `backend` 입력
3. **Build Command**: `./gradlew clean build -x test`
4. **Start Command**: `java -jar build/libs/*.jar`

### ✅ 단계 4: 환경변수 설정
Railway 대시보드에서 **Variables** 탭:

```properties
# 데이터베이스 (Railway MySQL 자동 제공)
SPRING_DATASOURCE_URL=${{MySQL.DATABASE_URL}}
SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}

# JPA 설정
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# 서버 설정
SERVER_PORT=8080

# CORS 설정 (Vercel URL로 변경)
ALLOWED_ORIGINS=https://your-app.vercel.app
```

---

## 3. 데이터베이스 설정

Railway MySQL이 자동으로 생성됩니다!

### ✅ DB 초기 데이터 확인
Railway Console에서 MySQL 접속:
```sql
USE railway;
SHOW TABLES;
-- user, project, daily_log 등 테이블 자동 생성됨
```

---

## 4. 환경변수 설정

### 프론트엔드 (Vercel)
```env
VITE_API_URL=https://your-backend.railway.app/api
```

### 백엔드 (Railway)
```env
SPRING_DATASOURCE_URL=jdbc:mysql://your-db:3306/railway
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your-password
ALLOWED_ORIGINS=https://your-app.vercel.app
```

---

## 5. 최종 확인

### ✅ 체크리스트
- [ ] Vercel 프론트엔드 배포 완료
- [ ] Railway 백엔드 배포 완료
- [ ] Railway MySQL 연결 확인
- [ ] 환경변수 설정 완료
- [ ] CORS 설정 확인 (프론트엔드 URL)
- [ ] 회원가입/로그인 테스트
- [ ] 프로젝트 생성 테스트

### 🎉 완료!
이제 친구들에게 URL만 공유하면 됩니다!
- 프론트엔드: `https://your-app.vercel.app`

---

## 🔧 문제 해결

### CORS 에러 발생 시
Railway 백엔드 환경변수에서 `ALLOWED_ORIGINS`를 확인하세요.

### DB 연결 실패 시
Railway MySQL이 실행 중인지 확인하고, 환경변수가 올바른지 확인하세요.

### 빌드 실패 시
- Vercel: `npm run build` 로컬 테스트
- Railway: `./gradlew clean build` 로컬 테스트

---

## 💡 비용
- **Vercel**: 무료 (Hobby Plan)
- **Railway**: $5/month 크레딧 제공 (소규모 앱 무료)
- **총 비용**: 소규모 사용은 **무료**!

---

## 🚀 더 나은 배포 (선택사항)

### 커스텀 도메인 연결
1. Vercel에서 도메인 구매 또는 연결
2. `knitting.yourname.com` 같은 예쁜 URL 사용!

### HTTPS 자동 적용
Vercel과 Railway는 자동으로 HTTPS를 제공합니다! 🔒

---

**문의사항이나 오류가 있으면 GitHub Issues에 남겨주세요!** 💕
