import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ProjectList from './features/project/ProjectList';
import ProjectDetailPage from './features/project/ProjectDetailPage';
import './index.css';

function App() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-yarn-cream via-yarn-pink/30 to-yarn-peach p-0 flex justify-center items-start font-cute">
      <BrowserRouter>
        <Routes>
          {/* 메인 화면 (목록) */}
          <Route path="/" element={<ProjectList />} />
          {/* 상세 화면 (주소 뒤에 id가 붙음) */}
          <Route path="/project/:id" element={<ProjectDetailPage />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;