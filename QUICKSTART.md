# 🧶 뜨개질 앱 - 빠른 배포 가이드

## 🚀 가장 쉬운 방법 (추천!)

### 1단계: GitHub에 올리기
```bash
cd /Users/eun/Desktop/KnittingApp
git add .
git commit -m "준비 완료! 배포하기"
git push origin main
```

### 2단계: Vercel에서 프론트엔드 배포 (무료!)
1. https://vercel.com 접속
2. GitHub 로그인
3. "New Project" 클릭
4. "KnittingApp" 저장소 선택
5. 설정:
   - Root Directory: **frontend** 입력
   - Build Command: `npm run build`
   - Deploy 클릭!

### 3단계: Railway에서 백엔드+DB 배포 (무료!)
1. https://railway.app 접속
2. GitHub 로그인
3. "New Project" 클릭
4. "Deploy MySQL" 선택 (DB 먼저!)
5. "New Service" → GitHub Repo 선택
6. 설정:
   - Root Directory: **backend** 입력
   - Environment Variables 추가:
     ```
     SPRING_DATASOURCE_URL=${{MySQL.DATABASE_URL}}
     SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
     SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
     ALLOWED_ORIGINS=https://your-vercel-app.vercel.app
     ```

### 4단계: Vercel에 백엔드 URL 추가
1. Vercel 프로젝트 → Settings → Environment Variables
2. 추가:
   ```
   VITE_API_URL=https://your-railway-app.railway.app/api
   ```
3. Redeploy 클릭!

## ✅ 완료!
이제 친구들에게 Vercel URL을 공유하면 끝! 🎉

예: `https://knitting-app-eun.vercel.app`

---

## 💰 비용
- **완전 무료!** (소규모 사용 시)
- Vercel: Hobby Plan (무료)
- Railway: 한 달에 $5 크레딧 제공 (충분함!)

---

## 🆘 문제 해결

### "CORS 에러" 발생?
→ Railway 환경변수에서 `ALLOWED_ORIGINS`에 Vercel URL 추가

### "DB 연결 실패"?
→ Railway에서 MySQL이 실행 중인지 확인

### "빌드 실패"?
→ 로컬에서 먼저 테스트:
```bash
# 프론트엔드
cd frontend
npm run build

# 백엔드
cd backend
./gradlew clean build
```

---

## 🎯 다음 단계 (선택사항)
- 커스텀 도메인 연결 (예: knitting.myname.com)
- 이메일 인증 추가
- 친구들과 공유! 💕
