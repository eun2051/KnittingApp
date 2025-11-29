import React, { useState } from 'react';
import axios from 'axios';

interface CreateProjectModalProps {
  isOpen: boolean;
  onClose: () => void;
  onCreated: () => void; // 생성 후 목록 새로고침용
}

const API_URL = 'http://localhost:8080/api/projects';

const CreateProjectModal = ({ isOpen, onClose, onCreated }: CreateProjectModalProps) => {
  const [form, setForm] = useState({ name: '', yarnName: '', needleType: '', needleSize: '', patternName: '', patternLinkUrl: '', patternPdfUrl: '', targetRows: '' });

  const handleCreate = async () => {
    if (!form.name) return alert("작품 이름을 알려주세요!");
    try {
      await axios.post(API_URL, {
        name: form.name,
        status: 'PLANNING',
        targetRows: Number(form.targetRows),
        currentRows: 0,
        startDate: null,
        endDate: null,
        gauge: null,
        yarnName: form.yarnName,
        needleType: form.needleType,
        needleSize: form.needleSize ? Number(form.needleSize) : null,
        patternName: form.patternName,
        patternLinkUrl: form.patternLinkUrl,
        patternPdfUrl: form.patternPdfUrl,
        imageUrl: null,
        notes: null
      });
      setForm({ name: '', yarnName: '', needleType: '', needleSize: '', patternName: '', patternLinkUrl: '', patternPdfUrl: '', targetRows: '' });
      onCreated(); // 부모에게 알림
      onClose();   // 창 닫기
    } catch (e) {
      alert("생성 실패! 백엔드를 확인해주세요.");
    }
  };

  if (!isOpen) return null;

  // 반복되는 클래스 변수화
  const inputClass =
    'w-full p-3 bg-white rounded-2xl border-2 border-yarn-pink focus:ring-2 focus:ring-yarn-pink font-cute text-yarn-pink placeholder-pink-300 outline-none transition shadow-cute';
  const buttonClass =
    'flex-1 py-3 font-bold rounded-2xl shadow-cute transition font-cute';

  return (
    <div className="bg-white p-8 rounded-3xl shadow-cute mb-8 border-2 border-yarn-pink animate-fade-in-down flex flex-col items-center">
      <h3 className="text-2xl font-bold text-yarn-pink mb-4 flex items-center gap-2 font-cute">
        <span>새 프로젝트 시작하기</span> <span className="text-3xl">🧶🧁</span>
      </h3>
      <div className="space-y-4 w-full">
        <input 
          className={inputClass}
          placeholder="작품 이름 (예: 구름 목도리)" 
          value={form.name} 
          onChange={e => setForm({...form, name: e.target.value})} 
        />
        <div className="flex gap-2">
          <input 
            className={inputClass}
            placeholder="실 이름" 
            value={form.yarnName} 
            onChange={e => setForm({...form, yarnName: e.target.value})} 
          />
          <input 
            className={inputClass}
            placeholder="바늘 종류 (예: 대바늘, 코바늘)" 
            value={form.needleType} 
            onChange={e => setForm({...form, needleType: e.target.value})} 
          />
          <input 
            className={inputClass}
            type="number"
            placeholder="바늘 사이즈 (mm)" 
            value={form.needleSize} 
            onChange={e => setForm({...form, needleSize: e.target.value})} 
          />
        </div>
        <div className="flex gap-2">
          <input 
            className={inputClass}
            placeholder="도안 이름" 
            value={form.patternName} 
            onChange={e => setForm({...form, patternName: e.target.value})} 
          />
          <input 
            className={inputClass}
            placeholder="도안 링크(URL)" 
            value={form.patternLinkUrl} 
            onChange={e => setForm({...form, patternLinkUrl: e.target.value})} 
          />
          <input 
            className={inputClass}
            placeholder="도안 PDF(URL)" 
            value={form.patternPdfUrl} 
            onChange={e => setForm({...form, patternPdfUrl: e.target.value})} 
          />
        </div>
        <input 
          className={inputClass}
          type="number" 
          placeholder="목표 단수" 
          value={form.targetRows} 
          onChange={e => setForm({...form, targetRows: e.target.value})} 
        />
        <div className="flex gap-2 mt-6">
          <button onClick={onClose} className={`${buttonClass} bg-yarn-pink text-white hover:bg-pink-300 hover:text-yarn-pink`}>
            취소
          </button>
          <button onClick={handleCreate} className={`${buttonClass} bg-yarn-pink text-white hover:bg-pink-300 shadow-cute-hover`}>
            시작하기 🌸
          </button>
        </div>
      </div>
      <div className="mt-6 text-yarn-pink text-sm font-cute text-center">
        <span className="text-xl">🌷</span> 몽글몽글 포근한 뜨개질의 시작을 응원해요!
      </div>
    </div>
  );
};

export default CreateProjectModal;