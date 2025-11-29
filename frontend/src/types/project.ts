// 뜨개질 프로젝트 관리 앱 타입 정의
// 모든 필드는 백엔드 DTO와 완전히 일치해야 함

// 프로젝트 상태 타입
export type ProjectStatus = 'PLANNING' | 'WIP' | 'FINISHED' | 'SUSPENDED';

// 게이지 타입
export interface Gauge {
  stitchCount: number;  // 10cm당 코 수
  rowCount: number;     // 10cm당 단 수
}

// 프로젝트 타입
export interface Project {
  id: number;
  name: string;
  description?: string;
  status: ProjectStatus;
  // 전체 목표 단수
  targetRows: number;
  // 현재까지 작업한 단수
  currentRows: number;
  // 진행률 (0~100)
  progress: number;
  // 게이지 정보 (10cm 기준 코/단)
  gaugeSts: number; // 10cm 내 코 수
  gaugeRows: number; // 10cm 내 단 수
  // 작업 일지 배열 (날짜별 합산)
  logs: DailyLog[];
  // 시작일
  startDate?: string;
  // 종료일
  endDate?: string;
  // 실 이름
  yarnName?: string;
  // 바늘 종류
  needleType?: string;
  // 바늘 사이즈
  needleSize?: number;
  // 도안 이름
  patternName?: string;
  // 도안 PDF URL
  patternPdfUrl?: string;
  // 도안 링크 URL
  patternLinkUrl?: string;
  // 이미지 URL
  imageUrl?: string;
  // 노트
  notes?: string;
  // 생성일
  createdAt?: string;
  // 수정일
  updatedAt?: string;
}

// 프로젝트 생성/수정 요청 타입 (백엔드 ProjectRequestDTO와 일치)
export interface ProjectRequest {
  name: string;
  status: ProjectStatus;
  startDate?: string;
  endDate?: string;
  targetRows?: number;
  currentRows?: number;
  gauge?: Gauge;  // { stitchCount, rowCount }
  yarnName?: string;
  needleType?: string;
  needleSize?: number;
  patternName?: string;
  patternLinkUrl?: string;
  patternPdfUrl?: string;
  imageUrl?: string;
  notes?: string;
}

// 작업 일지 타입
export interface DailyLog {
  // 작업 일지 날짜 (YYYY-MM-DD)
  date: string;
  // 해당 날짜에 작업한 단수 (rowsWorked)
  rowsWorked: number;
}

// 작업 일지 등록/수정 요청 DTO
export interface DailyLogRequestDTO {
  projectId: number;
  date: string;
  rowsWorked: number;
}

// 작업 일지 응답 DTO
export interface DailyLogResponseDTO {
  id: number;
  projectId: number;
  date: string;
  rowsWorked: number;
}