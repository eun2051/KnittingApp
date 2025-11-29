-- ============================================
-- 뜨개질 앱 완전한 데이터베이스 스키마
-- PostgreSQL (Supabase용)
-- ============================================

-- 기존 테이블 삭제 (의존성 순서대로)
DROP TABLE IF EXISTS daily_log CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS yarn CASCADE;
DROP TABLE IF EXISTS needle CASCADE;
DROP TABLE IF EXISTS pattern CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 시퀀스 삭제
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
DROP SEQUENCE IF EXISTS project_id_seq CASCADE;
DROP SEQUENCE IF EXISTS daily_log_id_seq CASCADE;
DROP SEQUENCE IF EXISTS yarn_id_seq CASCADE;
DROP SEQUENCE IF EXISTS needle_id_seq CASCADE;
DROP SEQUENCE IF EXISTS pattern_id_seq CASCADE;

-- ============================================
-- 1. users 테이블 (가장 먼저 생성)
-- ============================================
CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX idx_users_email ON users(email);

-- ============================================
-- 2. yarn 테이블 (실 정보)
-- ============================================
CREATE SEQUENCE yarn_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE yarn (
    id BIGINT PRIMARY KEY DEFAULT nextval('yarn_id_seq'),
    name VARCHAR(255),
    brand VARCHAR(255),
    color VARCHAR(100),
    material VARCHAR(255)
);

-- ============================================
-- 3. needle 테이블 (바늘 정보)
-- ============================================
CREATE SEQUENCE needle_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE needle (
    id BIGINT PRIMARY KEY DEFAULT nextval('needle_id_seq'),
    type VARCHAR(100),
    size DOUBLE PRECISION
);

-- ============================================
-- 4. pattern 테이블 (도안 정보)
-- ============================================
CREATE SEQUENCE pattern_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE pattern (
    id BIGINT PRIMARY KEY DEFAULT nextval('pattern_id_seq'),
    name VARCHAR(255),
    image_url VARCHAR(500),
    description TEXT,
    pattern_pdf_url VARCHAR(500),
    pattern_link_url VARCHAR(500)
);

-- ============================================
-- 5. project 테이블 (프로젝트 - 핵심!)
-- ============================================
CREATE SEQUENCE project_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE project (
    id BIGINT PRIMARY KEY DEFAULT nextval('project_id_seq'),
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PLANNING', 'WIP', 'FINISHED', 'SUSPENDED')),
    start_date DATE,
    end_date DATE,
    target_rows INTEGER,
    current_rows INTEGER DEFAULT 0,
    
    -- Gauge 정보 (Embedded)
    gauge_stitches INTEGER,
    gauge_rows INTEGER,
    
    -- 프로젝트 메타데이터
    project_image_url VARCHAR(500),
    notes TEXT,
    
    -- 외래 키 (사용자 - 필수!)
    user_id BIGINT NOT NULL,
    
    -- 외래 키 (선택 사항)
    yarn_id BIGINT,
    needle_id BIGINT,
    pattern_id BIGINT,
    
    -- 제약 조건
    CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_yarn FOREIGN KEY (yarn_id) REFERENCES yarn(id) ON DELETE SET NULL,
    CONSTRAINT fk_project_needle FOREIGN KEY (needle_id) REFERENCES needle(id) ON DELETE SET NULL,
    CONSTRAINT fk_project_pattern FOREIGN KEY (pattern_id) REFERENCES pattern(id) ON DELETE SET NULL
);

-- 인덱스 생성 (성능 최적화)
CREATE INDEX idx_project_user_id ON project(user_id);
CREATE INDEX idx_project_status ON project(status);
CREATE INDEX idx_project_start_date ON project(start_date);

-- ============================================
-- 6. daily_log 테이블 (작업 일지)
-- ============================================
CREATE SEQUENCE daily_log_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE daily_log (
    id BIGINT PRIMARY KEY DEFAULT nextval('daily_log_id_seq'),
    project_id BIGINT NOT NULL,
    date DATE NOT NULL,
    rows_worked INTEGER NOT NULL DEFAULT 0,
    
    -- 제약 조건
    CONSTRAINT fk_daily_log_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT uq_daily_log_project_date UNIQUE (project_id, date)
);

-- 인덱스 생성
CREATE INDEX idx_daily_log_project_id ON daily_log(project_id);
CREATE INDEX idx_daily_log_date ON daily_log(date);

-- ============================================
-- 샘플 데이터 (테스트용 - 선택 사항)
-- ============================================

-- 테스트 사용자 생성
-- INSERT INTO users (email, password, username) VALUES 
-- ('test@example.com', '$2a$10$dummyHashedPassword', '테스트유저');

-- ============================================
-- 권한 설정 (Supabase RLS 비활성화 - 개발용)
-- ============================================
-- 프로덕션 환경에서는 Row Level Security를 활성화하세요!
ALTER TABLE users DISABLE ROW LEVEL SECURITY;
ALTER TABLE project DISABLE ROW LEVEL SECURITY;
ALTER TABLE daily_log DISABLE ROW LEVEL SECURITY;
ALTER TABLE yarn DISABLE ROW LEVEL SECURITY;
ALTER TABLE needle DISABLE ROW LEVEL SECURITY;
ALTER TABLE pattern DISABLE ROW LEVEL SECURITY;

-- ============================================
-- 스키마 생성 완료!
-- ============================================
-- 이제 Supabase SQL Editor에서 이 전체 스크립트를 복사해서 실행하세요.
-- 순서:
-- 1. Supabase Dashboard → SQL Editor
-- 2. 이 스크립트 전체 복사 & 붙여넣기
-- 3. RUN 버튼 클릭
-- 4. Render 백엔드 재시작 (Deploy 탭에서 Manual Deploy)
-- ============================================
