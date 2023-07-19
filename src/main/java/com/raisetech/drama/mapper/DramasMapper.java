package com.raisetech.drama.mapper;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Insert("INSERT INTO dramas(title, year, priority) VALUES (#{title}, #{year}, #{priority})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(DramaDto dramaDto);

    @Update("UPDATE dramas SET title = #{title}, year = #{year}, priority = #{priority} "
            + "WHERE id = #{id}")
    void update(Drama drama);

}
