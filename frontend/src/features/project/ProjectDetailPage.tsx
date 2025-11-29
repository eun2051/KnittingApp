import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchProjectDetail, upsertDailyLog, projectApi } from '../../api/projectApi';
import { Project, DailyLog } from '../../types/project';
import '../../index.css';
import ProjectProgressChart from './ProjectProgressChart';

const ProjectDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const numericProjectId = Number(id);
  const [project, setProject] = useState<Project | null>(null);
  const [logs, setLogs] = useState<DailyLog[]>([]);
  const [todayRows, setTodayRows] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [saveSuccess, setSaveSuccess] = useState(false); // 저장 완료 안내
  const [editOpen, setEditOpen] = useState(false);
  const [editForm, setEditForm] = useState({
    yarnName: '',
    needleType: '',
    needleSize: '',
    patternName: '',
    patternLinkUrl: '',
    patternPdfUrl: ''
  });

  // 오늘 날짜를 KST(한국 표준시) 기준 YYYY-MM-DD로 반환
  const getToday = () => {
    const now = new Date();
    // KST 기준으로 보정 (UTC+9)
    now.setHours(now.getHours() + 9);
    return now.toISOString().slice(0, 10);
  };

  // 프로젝트 정보/작업 기록 최신화
  const refreshProject = async () => {
    setLoading(true);
    const data = await fetchProjectDetail(numericProjectId);
    // [디버그] API 응답값 콘솔 출력
    console.log('[DEBUG] fetchProjectDetail 응답:', data);
    setProject(data);
    setLogs(data.logs || []);
    setLoading(false);
  };

  React.useEffect(() => {
    if (!numericProjectId) return;
    refreshProject();
  }, [numericProjectId]);

  // 프로젝트 정보 콘솔 출력 (실/바늘 정보 확인용)
  // React.useEffect(() => {
  //   if (project) {
  //     console.log('[DEBUG] 프로젝트 실/바늘 정보:', {
  //       yarnName: project.yarnName,
  //       needleType: project.needleType,
  //       needleSize: project.needleSize
  //     });
  //   }
  // }, [project]);

  // + 버튼 클릭 시 단수 증가
  const handleIncrement = () => {
    setTodayRows((prev) => String(Number(prev || '0') + 1));
  };

  // - 버튼 클릭 시: 현재 단수가 0이면 비활성화, 1 이상이면 -1까지 허용
  const handleDecrement = () => {
    if (project && project.currentRows > 0) {
      setTodayRows((prev) => String(Number(prev || '0') - 1));
    }
  };

  // 오늘의 기록 저장
  const handleSave = async () => {
    if (!numericProjectId || !todayRows) return;
    setSaving(true);
    setSaveSuccess(false);
    await upsertDailyLog({
      projectId: numericProjectId,
      date: getToday(),
      rowsWorked: Number(todayRows)
    });
    await refreshProject(); // 저장 후 강제 최신화
    setSaving(false);
    setSaveSuccess(true);
    setTodayRows('');
    // 안내 메시지 1.5초 후 자동 숨김
    setTimeout(() => setSaveSuccess(false), 1500);
  };

  const openEditModal = () => {
    if (!project) return;
    setEditForm({
      yarnName: project.yarnName || '',
      needleType: project.needleType || '',
      needleSize: project.needleSize ? String(project.needleSize) : '',
      patternName: project.patternName || '',
      patternLinkUrl: project.patternLinkUrl || '',
      patternPdfUrl: project.patternPdfUrl || ''
    });
    setEditOpen(true);
  };

  const handleEditSave = async () => {
    if (!project) return;
    try {
      await projectApi.update(project.id, {
        name: project.name,
        status: project.status,
        startDate: project.startDate,
        endDate: project.endDate,
        targetRows: project.targetRows,
        currentRows: project.currentRows,
        gauge: {
          stitchCount: project.gaugeSts,
          rowCount: project.gaugeRows
        },
        yarnName: editForm.yarnName,
        needleType: editForm.needleType,
        needleSize: editForm.needleSize ? Number(editForm.needleSize) : undefined,
        patternName: editForm.patternName,
        patternLinkUrl: editForm.patternLinkUrl,
        patternPdfUrl: editForm.patternPdfUrl,
        imageUrl: project.imageUrl,
        notes: project.notes
      });
      setEditOpen(false);
      refreshProject();
    } catch (e) {
      alert('수정에 실패했습니다.');
    }
  };

  if (loading) {
    return (
      <div className="w-full h-full flex justify-center items-center bg-gradient-to-br from-pink-100 to-pink-50">
        <span className="text-pink-400 text-lg font-bold">로딩 중...</span>
      </div>
    );
  }
  if (!project) {
    return (
      <div className="w-full h-full flex justify-center items-center bg-gradient-to-br from-pink-100 to-pink-50">
        <span className="text-pink-400 text-lg font-bold">잘못된 프로젝트 ID입니다.</span>
      </div>
    );
  }

  // 날짜별 누적 단수(currentRow) 계산
  const progressLogs = logs.reduce<{ date: string; currentRow: number }[]>((acc, log) => {
    const prev = acc.length > 0 ? acc[acc.length - 1].currentRow : 0;
    acc.push({ date: log.date, currentRow: prev + log.rowsWorked });
    return acc;
  }, []);
  // 최종 누적 단수(생성일부터 모든 기록 합계) 계산
  const finalRows = progressLogs.length > 0 ? progressLogs[progressLogs.length - 1].currentRow : 0;
  // 진행률도 최종 누적 단수 기준으로 계산
  const progress = project.targetRows > 0 ? Math.min(100, Math.round((finalRows / project.targetRows) * 100)) : 0;

  return (
    <div className="relative min-h-screen bg-gradient-to-br from-pink-100 to-pink-50 pb-8">
      {/* 구름 패턴 배경 */}
      <div className="cloud-bg" />
      <div className="max-w-xl mx-auto pt-8">
        {/* 상단 헤더: 뒤로가기, 제목, 실/바늘 정보 */}
        <div className="fluffy-card flex items-center justify-between mb-8 py-4 px-6">
          <button
            className="fluffy-btn px-4 py-2 text-lg mr-2"
            onClick={() => navigate(-1)}
            aria-label="뒤로가기"
          >
            ←
          </button>
          <div className="flex-1 text-center">
            <div className="text-2xl font-bold text-pink-500 mb-1">{project.name}</div>
          </div>
        </div>
        {/* 단수 큰 동그라미 + 버튼, - 버튼 세로 배치 */}
        <div className="flex items-center justify-center mb-6">
          <div className="fluffy-circle flex items-center justify-center text-pink-500 text-5xl font-bold mr-4 shadow-lg">
            {finalRows}
          </div>
          <div className="flex flex-col items-center ml-2">
            <button
              className="fluffy-btn text-2xl px-6 py-2 mb-2 mt-[-0.5rem]"
              style={{ marginBottom: '0.5rem', marginTop: '-0.5rem' }}
              onClick={handleIncrement}
            >
              +
            </button>
            <button
              className="fluffy-btn text-2xl px-6 py-2"
              onClick={handleDecrement}
              disabled={project && project.currentRows === 0}
            >
              -
            </button>
          </div>
        </div>
        {/* 폭신한 바 진행률 + 목표 단수 표시 */}
        <div className="w-full mb-8 relative">
          <div className="fluffy-progress-bar relative h-8 rounded-full bg-pink-200 shadow-inner">
            <div
              className="absolute left-0 top-0 h-8 rounded-full bg-pink-400 transition-all"
              style={{ width: `${progress}%` }}
            />
            <span className="absolute w-full text-center text-pink-600 font-bold z-10" style={{ lineHeight: '2rem' }}>
              {progress}%
            </span>
            {/* 목표 단수 표시 (오른쪽 끝) */}
            <span className="absolute right-3 top-1/2 -translate-y-1/2 text-pink-400 text-xs font-bold bg-white/60 rounded-full px-3 py-1 shadow">
              {project.targetRows}단 목표
            </span>
          </div>
        </div>
        {/* 오늘의 결과 기록 입력칸 + 저장 버튼 + 안내 메시지 */}
        <div className="fluffy-card flex items-center mb-8 relative">
          <input
            type="number"
            value={todayRows}
            min={project && project.currentRows === 0 ? 0 : -1}
            onChange={(e) => setTodayRows(e.target.value)}
            className="fluffy-input mr-4 text-lg"
            placeholder="오늘 작업한 단수를 입력하세요 (음수도 가능)"
            disabled={saving}
          />
          <button className="fluffy-btn" onClick={handleSave} disabled={saving || !todayRows}>
            {saving ? '저장 중...' : '저장'}
          </button>
          {saveSuccess && (
            <div className="absolute left-1/2 -translate-x-1/2 bottom-[-2.2rem] bg-pink-200 text-pink-600 rounded-full px-6 py-2 shadow-lg font-bold text-sm animate-fade-in">
              저장 완료!
            </div>
          )}
        </div>
        {/* 첨부 이미지와 완전히 동일한 연핑크 폭신폭신 프로젝트 정보 상자 */}
        <div className="w-full mx-auto mb-4 p-8 rounded-[2.5rem] bg-gradient-to-br from-[#fff6fa] to-[#fbeaf3] border-[2px] border-[#fcd6e1] flex flex-col items-start">
          <div className="flex flex-col gap-2 w-full items-start">
            <div className="text-[#d8a7b8] font-semibold text-lg leading-tight">
              실: <span className="text-[#e573a7] font-bold">{project.yarnName && project.yarnName.trim() !== "" ? project.yarnName : '입력 없음'}</span>
            </div>
            <div className="text-[#d8a7b8] font-semibold text-lg leading-tight">
              바늘: <span className="text-[#e573a7] font-bold">{project.needleType && project.needleType.trim() !== "" ? project.needleType : '입력 없음'}{project.needleSize ? `(${project.needleSize}mm)` : ''}</span>
            </div>
            <div className="text-[#d8a7b8] font-semibold text-lg leading-tight">
              도안: {project.patternName && project.patternName.trim() !== "" ? <span className="text-[#e573a7] font-bold">{project.patternName}</span> : null}
              {project.patternLinkUrl && (<a href={project.patternLinkUrl} target="_blank" rel="noopener noreferrer" className="text-[#e573a7] underline font-bold ml-1">링크</a>)}
              {project.patternPdfUrl && (<a href={project.patternPdfUrl} target="_blank" rel="noopener noreferrer" className="text-[#e573a7] underline font-bold ml-2">PDF</a>)}
              {!project.patternName && !project.patternLinkUrl && !project.patternPdfUrl && <span className="text-[#e573a7] font-bold">입력 없음</span>}
            </div>
          </div>
        </div>
        {/* 진행 단수 꺾은선 그래프: 내부 상자만 남김, 바깥 상자 제거 */}
        <ProjectProgressChart logs={progressLogs} />
        {/* 작업 기록 그래프 영역은 fluffy-progress-bar(진행률 바)와 기존 기록 입력/리스트 UI로 복원 */}
        {/* 편집/삭제 버튼 하단 중앙 정렬 */}
        <div className="flex justify-center gap-4 mt-8">
          <button
            className="fluffy-btn bg-pink-200 text-pink-600 font-bold px-6 py-2 rounded-2xl shadow-cute hover:bg-pink-300 hover:text-white transition"
            onClick={openEditModal}
          >
            편집
          </button>
          <button
            className="fluffy-btn bg-pink-100 text-pink-400 font-bold px-6 py-2 rounded-2xl shadow-cute hover:bg-pink-300 hover:text-white transition"
            onClick={async () => {
              if (window.confirm('정말 삭제하시겠습니까?')) {
                try {
                  await projectApi.delete(project.id);
                  alert('삭제되었습니다.');
                  navigate('/', { state: { refresh: true } });
                } catch (e) {
                  alert('삭제에 실패했습니다.');
                }
              }
            }}
          >
            삭제
          </button>
        </div>
        {editOpen && (
          <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center z-50">
            <div className="bg-white rounded-2xl p-8 shadow-lg w-[400px] flex flex-col gap-4">
              <h2 className="text-xl font-bold text-pink-600 mb-2">프로젝트 정보 수정</h2>
              <input className="input" placeholder="실 이름" value={editForm.yarnName} onChange={e => setEditForm(f => ({ ...f, yarnName: e.target.value }))} />
              <input className="input" placeholder="바늘 종류" value={editForm.needleType} onChange={e => setEditForm(f => ({ ...f, needleType: e.target.value }))} />
              <input className="input" placeholder="바늘 사이즈(mm)" value={editForm.needleSize} onChange={e => setEditForm(f => ({ ...f, needleSize: e.target.value }))} />
              <input className="input" placeholder="도안 이름" value={editForm.patternName} onChange={e => setEditForm(f => ({ ...f, patternName: e.target.value }))} />
              <input className="input" placeholder="도안 링크" value={editForm.patternLinkUrl} onChange={e => setEditForm(f => ({ ...f, patternLinkUrl: e.target.value }))} />
              <input className="input" placeholder="도안 PDF URL" value={editForm.patternPdfUrl} onChange={e => setEditForm(f => ({ ...f, patternPdfUrl: e.target.value }))} />
              <div className="flex gap-2 mt-4">
                <button className="fluffy-btn bg-pink-200 text-pink-600 font-bold px-4 py-2 rounded-xl" onClick={handleEditSave}>저장</button>
                <button className="fluffy-btn bg-gray-200 text-gray-600 font-bold px-4 py-2 rounded-xl" onClick={() => setEditOpen(false)}>취소</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProjectDetailPage;

/*
  모든 코드/주석/설명은 한글로 작성
  포근포근하고 몽실몽실한 핑크 테마 UI 완성
*/