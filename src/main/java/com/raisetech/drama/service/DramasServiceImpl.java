package com.raisetech.drama.service;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.exception.ResourceNotFoundException;
import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.mapper.DramasMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DramasServiceImpl implements DramasService{
    private DramasMapper dramasMapper;

    public DramasServiceImpl(DramasMapper dramasMapper) {
        this.dramasMapper = dramasMapper;
    }

    @Override
    public List<Drama> getDramas(String priority) throws Exception {
        if (Objects.isNull(priority)){
            return dramasMapper.findAll().stream().toList();
        }
        List<Drama> dramas = dramasMapper.findByPriority(priority);
        if (dramas.isEmpty()){
                throw new ResourceNotFoundException("優先度" + priority + "のものはありません");
        } else {
            return dramas.stream().toList();
        }
    }
    @Override
    public int save(DramaDto dramaDto) {
        dramasMapper.save(dramaDto);
        return dramaDto.getId();
    }
}

