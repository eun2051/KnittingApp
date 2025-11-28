import axios from 'axios';
import { apiClient } from './client'
import { Project, ProjectRequest, DailyLog, DailyLogRequestDTO, DailyLogResponseDTO } from '../types/project'

// API baseURL에서 중복된 /api 제거
const apiClient = axios.create({
  baseURL: '/api', // 반드시 /api만 한 번만 붙도록!
});

// 프로젝트 API
export const projectApi = {
  // 모든 프로젝트 조회
  getAll: (): Promise<Project[]> => {
    return apiClient.get<Project[]>('/projects')
  },

  // 특정 프로젝트 조회
  getById: (id: number): Promise<Project> => {
    return apiClient.get<Project>(`/projects/${id}`)
  },

  // 프로젝트 생성
  create: (data: ProjectRequest): Promise<Project> => {
    return apiClient.post<Project>('/projects', data)
  },

  // 프로젝트 수정
  update: (id: number, data: ProjectRequest): Promise<Project> => {
    return apiClient.put<Project>(`/projects/${id}`, data)
  },

  // 프로젝트 삭제
  delete: (id: number): Promise<void> => {
    return apiClient.delete<void>(`/projects/${id}`)
  },

  // 단수 증가/감소
  updateRows: (id: number, rows: number): Promise<Project> => {
    return apiClient.put<Project>(`/projects/${id}/rows`, { rows })
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
    const p = res.data;
    return {
      yarnName: p.yarnName,
      needleType: p.needleType,
      needleSize: p.needleSize,
      patternLinkUrl: p.patternLinkUrl,
      patternPdfUrl: p.patternPdfUrl
    };
  }
}

// 작업 일지 API
export const dailyLogApi = {
  // 프로젝트별 작업 일지 조회
  getByProject: (projectId: number): Promise<DailyLog[]> => {
    return apiClient.get<DailyLog[]>(`/projects/${projectId}/logs`)
  },

  // 작업 일지 생성/업데이트 (같은 날짜면 업데이트)
  createOrUpdate: (projectId: number, data: DailyLogRequestDTO): Promise<DailyLogResponseDTO> => {
    return apiClient.post<DailyLogResponseDTO>(`/projects/${projectId}/logs`, data)
  },

  // 작업 일지 삭제
  delete: (projectId: number, id: number): Promise<void> => {
    return apiClient.delete<void>(`/projects/${projectId}/logs/${id}`)
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