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