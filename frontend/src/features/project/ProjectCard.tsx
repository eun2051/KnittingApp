import React from 'react';
import { Project } from '../../types';

interface ProjectCardProps {
  project: Project;
  onClick: (project: Project) => void;
}

const ProjectCard = ({ project, onClick }: ProjectCardProps) => {
  return (
    <div 
      onClick={() => onClick(project)}
      className="bg-white p-5 rounded-3xl shadow-cute hover:shadow-cute-hover hover:-translate-y-1 transition-all duration-300 cursor-pointer border border-yarn-pink/30 flex justify-between items-center group"
    >
      <div>
        <h3 className="text-xl font-bold text-gray-800 group-hover:text-yarn-pink transition">
          {project.name}
        </h3>
        {/* 실/바늘 정보 명시적으로 표시 */}
        <div className="flex flex-col gap-1 mt-1">
          {project.yarnName && (
            <span className="text-pink-400 text-xs font-bold bg-white/60 rounded-full px-2 py-1 shadow">
              실: {project.yarnName}
            </span>
          )}
          {(project.needleType || project.needleSize) && (
            <span className="text-pink-400 text-xs font-bold bg-white/60 rounded-full px-2 py-1 shadow">
              바늘: {project.needleType}
              {project.needleSize ? ` (${project.needleSize}mm)` : ''}
            </span>
          )}
        </div>
        <p className="text-sm text-gray-500 mt-1 flex items-center gap-1">
          <span className="w-2 h-2 rounded-full bg-yarn-mint inline-block"></span>
          {project.currentRows}단 진행 중 
          <span className="text-gray-300">|</span> 
          목표 {project.targetRows}단
        </p>
      </div>
      <div className="w-12 h-12 bg-yarn-cream rounded-full flex items-center justify-center text-yarn-pink border border-yarn-pink group-hover:bg-yarn-pink group-hover:text-white transition">
        Go
      </div>
    </div>
  );
};

export default ProjectCard;