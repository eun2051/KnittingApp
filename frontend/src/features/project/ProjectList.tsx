import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Project } from '../../types/project';
import { projectApi } from '../../api/projectApi';
import ProjectCard from './ProjectCard';
import CreateProjectModal from './CreateProjectModal';

const ProjectList = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [projects, setProjects] = useState<Project[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchProjects = async () => {
    try {
      const data = await projectApi.getAll();
      setProjects(data);
    } catch (e) {
      console.error('프로젝트 목록 조회 실패:', e);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('jwt');
    navigate('/login');
  };

  useEffect(() => { fetchProjects(); }, [location]);

  return (
    <div className="w-full max-w-md mx-auto min-h-screen bg-gradient-to-br from-yarn-pink/40 via-white to-yarn-pink/20 p-8 flex flex-col items-center relative">
      {/* 로그아웃 버튼 */}
      <button
        onClick={handleLogout}
        className="absolute top-4 right-4 px-4 py-2 bg-yarn-pink text-white text-sm rounded-lg font-bold hover:bg-yarn-pink/80 transition shadow-md"
      >
        로그아웃
      </button>

      <header className="mb-8 mt-4 text-center">
        <h1 className="text-4xl font-bold text-yarn-pink mb-2 font-cute flex items-center justify-center gap-2">
          My Knitting <span className="text-4xl">🧶</span>
        </h1>
        <p className="text-yarn-pink/80 font-cute">오늘도 포근한 뜨개 생활 되세요!</p>
      </header>

      {!isModalOpen && (
        <button 
          onClick={() => setIsModalOpen(true)}
          className="w-full py-4 bg-white border-2 border-yarn-pink text-yarn-pink rounded-2xl font-bold hover:bg-yarn-pink/30 hover:text-white hover:border-transparent transition mb-6 shadow-cute flex items-center justify-center gap-2 font-cute"
        >
          <span>+ 새로운 작품 시작하기</span>
        </button>
      )}

      <CreateProjectModal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)} 
        onCreated={fetchProjects}
      />

      <div className="pb-10 space-y-4 w-full">
        {projects.map(project => (
          <ProjectCard 
            key={project.id} 
            project={project} 
            onClick={() => navigate(`/project/${project.id}`)} 
          />
        ))}

        {projects.length === 0 && !isModalOpen && (
          <div className="text-center text-yarn-pink py-20 bg-white rounded-3xl border-2 border-yarn-pink shadow-cute font-cute">
            <div className="text-5xl mb-4">🧣🧁</div>
            아직 진행 중인 뜨개가 없어요.<br/>
            첫 작품을 등록해보세요!
          </div>
        )}
      </div>
    </div>
  );
};

export default ProjectList;