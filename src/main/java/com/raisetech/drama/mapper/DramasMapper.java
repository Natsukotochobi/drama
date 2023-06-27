package com.raisetech.drama.mapper;

import com.raisetech.drama.entity.Drama;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DramasMapper {
    @Select("SELECT * FROM dramas")
    List<Drama> findAll();

    @Select("SELECT * FROM dramas WHERE priority = #{priority}")
    List<Drama> findByPriority(String priority);

    @Select("SELECT * FROM dramas WHERE id = #{id}")
    Optional<Drama> findById(int id);

}
