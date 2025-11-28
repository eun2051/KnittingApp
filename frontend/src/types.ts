// src/types.ts
export interface DailyLog {
  id: number;
  date: string;
  rowsWorked: number;
}

export interface Project {
  id: number;
  name: string;
  yarn: string;
  needle: string;
  currentRows: number;
  targetRows: number;
  logs: DailyLog[];
}