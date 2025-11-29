import axios from 'axios';
import { Project, ProjectRequest, DailyLog, DailyLogRequestDTO, DailyLogResponseDTO } from '../types/project'

// API baseURL - 환경변수 사용 (배포 시 자동으로 변경됨)
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
});

// JWT 토큰을 모든 요청에 자동 포함시키는 인터셉터 추가
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('jwt');
  if (token) {
    config.headers = config.headers || {};
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

// 인증 만료/실패 시 자동 로그아웃 및 로그인 화면 이동
apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      localStorage.removeItem('jwt');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 프로젝트 API
export const projectApi = {
  // 모든 프로젝트 조회
  getAll: (): Promise<Project[]> => {
    return apiClient.get<Project[]>('/projects').then(res => res.data)
  },

  // 특정 프로젝트 조회
  getById: (id: number): Promise<Project> => {
    return apiClient.get<Project>(`/projects/${id}`).then(res => res.data)
  },

  // 프로젝트 생성
  create: (data: ProjectRequest): Promise<Project> => {
    return apiClient.post<Project>('/projects', data).then(res => res.data)
  },

  // 프로젝트 수정
  update: (id: number, data: ProjectRequest): Promise<Project> => {
    return apiClient.put<Project>(`/projects/${id}`, data).then(res => res.data)
  },

  // 프로젝트 삭제
  delete: (id: number): Promise<void> => {
    return apiClient.delete<void>(`/projects/${id}`).then(res => res.data)
  },

  // 단수 증가/감소
  updateRows: (id: number, rows: number): Promise<Project> => {
    return apiClient.put<Project>(`/projects/${id}/rows`, { rows }).then(res => res.data)
  },

  // 프로젝트 실/바늘/도안 정보만 단독 조회
  getMaterialInfo: async (id: number): Promise<{
    yarnName?: string;
    needleType?: string;
    needleSize?: number;
    patternLinkUrl?: string;
    patternPdfUrl?: string;
  }> => {
    const res = await apiClient.get<Project>(`/projects/${id}`);
    return res.data;
  },

  // 작업 일지 전체 조회
  getLogs: (projectId: number): Promise<DailyLog[]> => {
    return apiClient.get<DailyLog[]>(`/projects/${projectId}/logs`).then(res => res.data)
  },

  // 작업 일지 추가
  addLog: (projectId: number, data: DailyLogRequestDTO): Promise<DailyLogResponseDTO> => {
    return apiClient.post<DailyLogResponseDTO>(`/projects/${projectId}/logs`, data).then(res => res.data)
  },

  // 작업 일지 삭제
  deleteLog: (projectId: number, id: number): Promise<void> => {
    return apiClient.delete<void>(`/projects/${projectId}/logs/${id}`).then(res => res.data)
  },
}

// 작업 일지 API
export const dailyLogApi = {
  // 프로젝트별 작업 일지 조회
  getByProject: async (projectId: number): Promise<DailyLog[]> => {
    const response = await apiClient.get<DailyLog[]>(`/projects/${projectId}/logs`)
    return response.data
  },

  // 작업 일지 생성/업데이트 (같은 날짜면 업데이트)
  createOrUpdate: async (projectId: number, data: DailyLogRequestDTO): Promise<DailyLogResponseDTO> => {
    const response = await apiClient.post<DailyLogResponseDTO>(`/projects/${projectId}/logs`, data)
    return response.data
  },

  // 작업 일지 삭제
  delete: async (projectId: number, id: number): Promise<void> => {
    await apiClient.delete<void>(`/projects/${projectId}/logs/${id}`)
  }
}

// 특정 프로젝트의 작업 일지(날짜별 합산) 조회
export async function fetchProjectDetail(projectId: number): Promise<Project> {
  const res = await fetch(`/api/projects/${projectId}`);
  if (!res.ok) throw new Error('프로젝트 조회 실패');
  return await res.json();
}

export async function fetchProjectList(): Promise<Project[]> {
  const res = await fetch('/api/projects');
  if (!res.ok) throw new Error('프로젝트 목록 조회 실패');
  return await res.json();
}

export async function fetchDailyLogs(projectId: number): Promise<DailyLog[]> {
  const res = await fetch(`/api/projects/${projectId}/logs`);
  if (!res.ok) throw new Error('작업 일지 조회 실패');
  return await res.json();
}

// 오늘의 작업 일지 등록/수정(UPSERT)
export async function upsertDailyLog(dto: DailyLogRequestDTO): Promise<DailyLogResponseDTO> {
  const res = await fetch(`/api/projects/${dto.projectId}/logs`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto),
  });
  if (!res.ok) throw new Error('작업 일지 등록/수정 실패');
  return await res.json();
}

// 한글 주석으로 모든 함수 설명 추가