// 프로젝트 진행 꺾은선 그래프 컴포넌트 (Recharts 기반)
// 한국어 주석 포함
import React from "react";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";

// 로그 데이터 타입
interface DailyLog {
  date: string; // yyyy-MM-dd
  currentRow: number;
}

interface Props {
  logs: DailyLog[];
}

const ProjectProgressChart: React.FC<Props> = ({ logs }) => {
  // 날짜 오름차순 정렬
  const sortedLogs = [...logs].sort((a, b) => a.date.localeCompare(b.date));
  return (
    <div className="w-full h-96 flex flex-col items-center justify-center rounded-[2.5rem] bg-gradient-to-br from-[#fff6fa] to-[#fbeaf3] border-[2px] border-[#fcd6e1] p-8">
      <h3 className="text-[#e573a7] font-extrabold mb-6 text-2xl tracking-wide drop-shadow-lg">단수 기록 그래프</h3>
      <ResponsiveContainer width="100%" height="80%">
        <LineChart data={sortedLogs} margin={{ top: 20, right: 30, left: 10, bottom: 20 }}>
          <CartesianGrid strokeDasharray="6 6" stroke="#fbeaf3" />
          <XAxis dataKey="date" tick={{ fontSize: 16, fill: '#e573a7', fontWeight: 700 }} axisLine={{ stroke: '#fcd6e1' }} tickLine={{ stroke: '#fcd6e1' }} />
          <YAxis tick={{ fontSize: 16, fill: '#e573a7', fontWeight: 700 }} axisLine={{ stroke: '#fcd6e1' }} tickLine={{ stroke: '#fcd6e1' }} />
          <Tooltip formatter={(value: number) => `${value}단`} labelFormatter={label => `날짜: ${label}`} contentStyle={{ background: '#fff6fa', borderRadius: '1.5rem', border: '1px solid #fcd6e1', color: '#e573a7', fontWeight: 700, fontSize: '1.1rem', boxShadow: '0 2px 12px #fbeaf3' }} />
          <Line type="monotone" dataKey="currentRow" stroke="#e573a7" strokeWidth={6} dot={{ r: 10, stroke: '#fcd6e1', strokeWidth: 3, fill: '#fff6fa', filter: 'drop-shadow(0 2px 8px #fbeaf3)' }} activeDot={{ r: 12, fill: '#e573a7', stroke: '#fcd6e1', strokeWidth: 3, filter: 'drop-shadow(0 2px 12px #fbeaf3)' }} />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default ProjectProgressChart;
