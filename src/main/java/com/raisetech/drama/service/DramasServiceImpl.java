package com.raisetech.drama.service;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.exception.DuplicateTitleException;
import com.raisetech.drama.exception.ResourceNotFoundException;
import com.raisetech.drama.mapper.DramasMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        try {
            dramasMapper.save(dramaDto);
        } catch (DuplicateKeyException e) {
            throw new DuplicateTitleException(dramaDto.getTitle() + "は、すでに登録されています。");
        }
        return dramaDto.getId();
    }

    @Override
    public Drama update(DramaDto dramaDto) {
        Drama drama = dramasMapper.findById(dramaDto.getId()).orElseThrow(() ->
                new ResourceNotFoundException("id:" + dramaDto.getId() + "番のタイトルが見つかりません。"));
       //DramaクラスのupdateDramaメソッドを呼び出し
        drama.updateDrama(dramaDto);
        try {
            dramasMapper.update(drama);
        } catch (DuplicateKeyException e) {
            throw new DuplicateTitleException(drama.getTitle() + "は、すでに登録されています。");
        }
        return drama;
    }

    @Override
    public void deleteById(int id) {
        dramasMapper.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("id:" + id + "番のタイトルが見つかりません。"));
        dramasMapper.deleteById(id);
    }
}

