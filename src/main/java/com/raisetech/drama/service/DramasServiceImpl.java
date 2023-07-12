package com.raisetech.drama.service;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.exception.ResourceNotFoundException;
import com.raisetech.drama.mapper.DramasMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DramasServiceImpl implements DramasService{
    private DramasMapper dramasMapper;

    public DramasServiceImpl(DramasMapper dramasMapper) {
        this.dramasMapper = dramasMapper;
    }

    @Override
    public List<Drama> getDramas(String priority){
        List<Drama> dramas = dramasMapper.findByPriority(priority);
        return dramas.stream().toList();
    }
    @Override
    public List<Drama> getAllDramas(){
        return dramasMapper.findAll().stream().toList();
    }
    @Override
    public int save(DramaDto dramaDto) {
        dramasMapper.save(dramaDto);
        return dramaDto.getId();
    }

    @Override
    public Drama update(int id, DramaDto dramaDto) {
        Drama drama = dramasMapper.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("id:" + id + "番のタイトルが見つかりません。"));
        if (dramaDto.getTitle() == null) {
            dramaDto.setTitle(drama.getTitle());
        }
        if (dramaDto.getYear() == null) {
            dramaDto.setYear(drama.getYear());
        }
        if (dramaDto.getPriority() == null) {
            dramaDto.setPriority(drama.getPriority());
        }
        dramasMapper.update(id, dramaDto);
        Drama updateDrama = dramasMapper.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("id:" + id + "番のタイトルが見つかりません。"));
        return updateDrama;
    }
}

